package com.gym.app.parts.findcourses;

import android.annotation.SuppressLint;

import com.gym.app.data.SystemUtils;
import com.gym.app.data.model.Course;
import com.gym.app.data.model.Day;
import com.gym.app.data.observables.SaveCoursesObservable;
import com.gym.app.data.room.AppDatabase;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.CoursesService;
import com.gym.app.utils.MvpObserver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    private static final String DAY_NAME_FORMAT = "EEEE";
    private static final long FIVE_DAYS_TIMESTAMP = 5 * 24 * 3600 * 1000;
    private static final long ONE_DAY_TIMESTAMP = 24 * 3600 * 1000;
    private static final int DAYS_NUMBER = 5;

    @Inject
    CoursesService mCoursesService;

    @Inject
    SystemUtils mSystemUtils;

    @Inject
    AppDatabase mAppDatabase;

    private List<Course> mCoursesList;
    private List<Day> mDaysList;
    private String mToday;
    private String mTomorrow;

    FindCoursesPresenter(FindCoursesView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
        mCoursesList = new ArrayList<>();
        mDaysList = new ArrayList<>();
    }

    void initData() {
        //Initialize the days
        long currentTime = System.currentTimeMillis() / 1000;
        mDaysList = generateDaysList();
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

    /**
     * Set the mToday and mTomorrow names in order to support internationalisation
     * Should be called from the corresponding view where it is used with the corresponding string values
     *
     * @param today    the name of mToday day in the language of the device
     * @param tomorrow the name of mTomorrow day in the language of the device
     */
    void setTodayTomorrow(String today, String tomorrow) {
        this.mToday = today;
        this.mTomorrow = tomorrow;
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

    private List<Day> generateDaysList() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat =
                new SimpleDateFormat(DAY_NAME_FORMAT);
        List<Day> days = new ArrayList<>();
        Calendar currentCalendar = Calendar.getInstance();
        long currentDayStartTime = currentCalendar.getTimeInMillis();
        String currentDayName = dateFormat.format(currentCalendar.getTime());
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);
        long currentDayEndTime = currentCalendar.getTimeInMillis() + ONE_DAY_TIMESTAMP - 1;
        for (int i = 0; i < DAYS_NUMBER; i++) {
            //For the day, the milliseconds are converted to seconds
            days.add(new Day(currentDayName, currentDayStartTime / 1000, currentDayEndTime / 1000));
            currentDayStartTime = currentDayEndTime + 1;
            currentDayEndTime += ONE_DAY_TIMESTAMP;
            currentDayName = dateFormat.format(new Date(currentDayStartTime));
        }
        days.get(0).setDayName(mToday);
        days.get(1).setDayName(mTomorrow);
        return days;
    }
}
