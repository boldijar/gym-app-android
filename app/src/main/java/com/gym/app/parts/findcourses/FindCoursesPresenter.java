package com.gym.app.parts.findcourses;

import com.gym.app.data.SystemUtils;
import com.gym.app.data.model.Course;
import com.gym.app.data.model.Day;
import com.gym.app.data.observables.SaveCoursesObservable;
import com.gym.app.data.room.AppDatabase;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.CoursesService;
import com.gym.app.utils.MvpObserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Presenter for the FindCoursesFragment
 *
 * @author catalinradoiu
 * @since 2017.10.31
 */

public class FindCoursesPresenter extends Presenter<FindCoursesView> {

    private static final String TODAY = "today";
    private static final String TOMORROW = "tomorrow";
    private static final String MONDAY = "monday";
    private static final String TUESDAY = "tuesday";
    private static final String WEDNESDAY = "wednesday";
    private static final String THURSDAY = "thursday";
    private static final String FRIDAY = "friday";
    private static final String SATURDAY = "saturday";
    private static final String SUNDAY = "sunday";

    private static final long FIVE_DAYS_TIMESTAMP = 5 * 24 * 3600;
    private static final long ONE_DAY_TIMESTAMP = 24 * 3600;

    private List<Course> mCoursesList;

    private List<Day> mDaysList;

    @Inject
    CoursesService mCoursesService;

    @Inject
    SystemUtils mSystemUtils;

    @Inject
    AppDatabase mAppDatabase;

    FindCoursesPresenter(FindCoursesView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
        mCoursesList = new ArrayList<>();
        mDaysList = new ArrayList<>();
    }

    void initData() {

        //Initialize the days 

        long currentTime = System.currentTimeMillis() / 1000;
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        mDaysList = generateDaysList(today, calendar);
        getView().initDays(mDaysList);

        //Initialize the courses

        long endPeriod = currentTime + FIVE_DAYS_TIMESTAMP;
        loadCourses(currentTime, endPeriod);
    }

    List<Course> getCoursesForDay(long dayStartTime, long dayEndTime) {
        List<Course> result = new ArrayList<>();
        for (Course course : mCoursesList) {
            if (course.getCourseDate() >= dayStartTime && course.getCourseDate() <= dayEndTime) {
                result.add(course);
            }
        }
        return result;
    }

    private void loadCourses(long periodStart, long periodEnd) {
        if (mSystemUtils.isNetworkUnavailable()) {
            loadCoursesOffline();
        } else {
            mCoursesService.getCoursesForPeriod(periodStart, periodEnd)
                    .zipWith(mCoursesService.getCoursesForCurrentUser(true),
                            new BiFunction<List<Course>, List<Course>, List<Course>>() {

                                @Override
                                public List<Course> apply(@NonNull List<Course> firstList,
                                                          @NonNull List<Course> secondList) throws Exception {
                                    for (Course value : firstList) {
                                        if (secondList.contains(value)) {
                                            value.setIsRegistered(true);
                                        }
                                    }
                                    return firstList;
                                }
                            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MvpObserver<List<Course>>(this) {
                        @Override
                        public void onNext(List<Course> value) {
                            SaveCoursesObservable.newInstance(value).subscribe();
                            getView().setLoaded();
                            mCoursesList.addAll(value);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            getView().setError();
                        }
                    });
        }
    }

    private void loadCoursesOffline() {
        mAppDatabase.getCoursesDao().getAllCourses()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Course>>() {
                    @Override
                    public void accept(@NonNull List<Course> courseList) throws Exception {
                        getView().setLoaded();
                        mCoursesList.addAll(courseList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getView().setError();
                    }
                });
    }

    private List<Day> generateDaysList(int firstDay, Calendar currentCalendar) {
        List<String> daysNames = new ArrayList<>();
        switch (firstDay) {
            case Calendar.MONDAY:
                daysNames.addAll(Arrays.asList(TODAY, TOMORROW, WEDNESDAY, THURSDAY, FRIDAY));
                break;
            case Calendar.TUESDAY:
                daysNames.addAll(Arrays.asList(TODAY, TOMORROW, THURSDAY, FRIDAY, SATURDAY));
                break;
            case Calendar.WEDNESDAY:
                daysNames.addAll(Arrays.asList(TODAY, TOMORROW, FRIDAY, SATURDAY, SUNDAY));
                break;
            case Calendar.THURSDAY:
                daysNames.addAll(Arrays.asList(TODAY, TOMORROW, SATURDAY, SUNDAY, MONDAY));
                break;
            case Calendar.FRIDAY:
                daysNames.addAll(Arrays.asList(TODAY, TOMORROW, SUNDAY, MONDAY, TUESDAY));
                break;
            case Calendar.SATURDAY:
                daysNames.addAll(Arrays.asList(TODAY, TOMORROW, MONDAY, TUESDAY, WEDNESDAY));
                break;
            case Calendar.SUNDAY:
                daysNames.addAll(Arrays.asList(TODAY, TOMORROW, TUESDAY, WEDNESDAY, THURSDAY));
                break;
        }

        List<Day> days = new ArrayList<>();
        long currentDayStartTime = currentCalendar.getTimeInMillis() / 1000;
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);
        long currentDayEndTime = currentCalendar.getTimeInMillis() / 1000 + ONE_DAY_TIMESTAMP - 1;
        for (String name : daysNames) {
            days.add(new Day(name, currentDayStartTime, currentDayEndTime));
            currentDayStartTime = currentDayEndTime;
            currentDayEndTime += ONE_DAY_TIMESTAMP;
        }

        return days;
    }
}
