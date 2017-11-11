package com.gym.app.parts.findcourses.day_courses;

import com.gym.app.data.SystemUtils;
import com.gym.app.data.model.Course;
import com.gym.app.data.observables.UpdateCourseObservable;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.CoursesService;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catalinradoiu
 * @since 2017.11.05
 */

public class DayCoursesPresenter extends Presenter<DayCoursesView> {

    @Inject
    CoursesService mCoursesService;

    @Inject
    SystemUtils mSystemUtils;

    DayCoursesPresenter(DayCoursesView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    void registerToCourse(final Course course) {
        mCoursesService.registerToCourse(course.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        course.setIsRegistered(true);
                        course.setRegisteredUsersNumber(course.getRegisteredUsersNumber() + 1);
                        UpdateCourseObservable.newInstance(course).subscribe();
                        getView().displayOperationSuccessful(DayCoursesView.OperationType.REGISTER_TO_COURSE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getView().displayError(DayCoursesView.OperationType.REGISTER_TO_COURSE);
                    }
                });
    }

    void unregisterFromCourse(final Course course) {
        mCoursesService.unregisterFromCourse(course.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        course.setIsRegistered(false);
                        course.setRegisteredUsersNumber(course.getRegisteredUsersNumber() - 1);
                        UpdateCourseObservable.newInstance(course).subscribe();
                        getView().displayOperationSuccessful(DayCoursesView.OperationType.REMOVE_COURSE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getView().displayError(DayCoursesView.OperationType.REMOVE_COURSE);
                    }
                });
    }
}
