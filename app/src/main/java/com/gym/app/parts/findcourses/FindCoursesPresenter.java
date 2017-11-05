package com.gym.app.parts.findcourses;

import com.gym.app.data.SystemUtils;
import com.gym.app.data.model.Course;
import com.gym.app.data.model.Day;
import com.gym.app.data.model.Trainer;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.CoursesService;
import com.gym.app.utils.MvpObserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
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

    private static final long FIVE_DAYS_TIMESTAMP = 5 * 24 * 3600 * 1000;
    private static final long ONE_DAY_TIMESTAMP = 24 * 3600 * 1000;

    private List<Course> coursesList;

    private List<Day> daysList;

    @Inject
    CoursesService mCoursesService;

    @Inject
    SystemUtils systemUtils;

    public FindCoursesPresenter(FindCoursesView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
        coursesList = new ArrayList<>();
        daysList = new ArrayList<>();
    }

    public void initData() {

        //Initialize the days 

        long currentTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentTime));
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        daysList = generateDaysList(today, calendar);
        getView().initDays(daysList);

        //Initialize the courses

        long endPeriod = currentTime + FIVE_DAYS_TIMESTAMP;
        loadCourses(currentTime, endPeriod);
    }

    public List<Course> getCoursesForDay(long dayStartTime, long dayEndTime) {
        List<Course> result = new ArrayList<>();
        for (Course course : coursesList) {
            if (course.getCourseDate() >= dayStartTime && course.getCourseDate() <= dayEndTime) {
                result.add(course);
            }
        }
        return result;
    }

    private void loadCourses(long periodStart, long periodEnd) {
        if (systemUtils.isNetworkUnavailable()) {
            loadCoursesOffline();
        } else {
           /* mCoursesService.getCoursesForPeriod(periodStart, periodEnd)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MvpObserver<List<Course>>(this) {
                        @Override
                        public void onNext(List<Course> value) {
                            coursesList.addAll(value);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }
                    });*/
            String imageUrl = "http://alexedmans.com/wp-content/uploads/2017/03/Sports.jpg";
            coursesList.add(new Course(1, System.currentTimeMillis() + 100000, 30, 14,
                    new Trainer(1, "Popescu", "popescu@yahoo.com",
                            "http://alexedmans.com/wp-content/uploads/2017/03/Sports.jpg")));
            coursesList.add(new Course(3, System.currentTimeMillis() + 1000000, 30, 14,
                    new Trainer(2, "Ionescu", "popescu@yahoo.com",
                            "http://alexedmans.com/wp-content/uploads/2017/03/Sports.jpg")));
            coursesList.add(new Course(1, System.currentTimeMillis() + 100000, 30, 14,
                    new Trainer(1, "Popescu", "popescu@yahoo.com",
                            "http://alexedmans.com/wp-content/uploads/2017/03/Sports.jpg")));
            coursesList.add(new Course(3, System.currentTimeMillis() + 1000000, 30, 14,
                    new Trainer(2, "Ionescu", "popescu@yahoo.com",
                            "http://alexedmans.com/wp-content/uploads/2017/03/Sports.jpg")));
            coursesList.add(new Course(1, System.currentTimeMillis() + 100000, 30, 14,
                    new Trainer(1, "Popescu", "popescu@yahoo.com",
                            "http://alexedmans.com/wp-content/uploads/2017/03/Sports.jpg")));
            coursesList.add(new Course(3, System.currentTimeMillis() + 1000000, 30, 14,
                    new Trainer(2, "Ionescu", "popescu@yahoo.com",
                            "http://alexedmans.com/wp-content/uploads/2017/03/Sports.jpg")));
            coursesList.add(new Course(1, System.currentTimeMillis() + 100000, 30, 14,
                    new Trainer(1, "Popescu", "popescu@yahoo.com",
                            "http://alexedmans.com/wp-content/uploads/2017/03/Sports.jpg")));
            coursesList.add(new Course(3, System.currentTimeMillis() + 1000000, 30, 14,
                    new Trainer(2, "Ionescu", "popescu@yahoo.com",
                            "http://alexedmans.com/wp-content/uploads/2017/03/Sports.jpg")));
            coursesList.add(new Course(1, System.currentTimeMillis() + ONE_DAY_TIMESTAMP + 30000, 30, 14,
                    new Trainer(3, "Popescu", "popescu@yahoo.com",
                            "http://alexedmans.com/wp-content/uploads/2017/03/Sports.jpg")));
            coursesList.add(new Course(1, System.currentTimeMillis() + ONE_DAY_TIMESTAMP * 3 + 30000, 30, 14,
                    new Trainer(3, "Popescu", "popescu@yahoo.com",
                            "http://alexedmans.com/wp-content/uploads/2017/03/Sports.jpg")));
        }
    }

    private void loadCoursesOffline() {

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
        long currentDayStartTime = currentCalendar.getTimeInMillis();
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);
        long currentDayEndTime = currentCalendar.getTimeInMillis() + ONE_DAY_TIMESTAMP - 1;
        for (String name : daysNames) {
            days.add(new Day(name, currentDayStartTime, currentDayEndTime));
            currentDayStartTime = currentDayEndTime;
            currentDayEndTime += ONE_DAY_TIMESTAMP;
        }

        return days;
    }
}
