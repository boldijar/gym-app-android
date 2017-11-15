package com.gym.app.parts.findcourses.day_courses;

import com.gym.app.data.SystemUtils;
import com.gym.app.data.model.Course;
import com.gym.app.data.observables.UpdateCourseObservable;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.CoursesService;

import javax.inject.Inject;

import io.reactivex.Completable;
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

    void handleCourseClick(final Course course, final int position, final boolean isRegistered) {
        Completable operation;
        final DayCoursesView.OperationType type;
        if (isRegistered) {
            operation = mCoursesService.unregisterFromCourse(course.getId());
            type = DayCoursesView.OperationType.REMOVE_COURSE;
        } else {
            operation = mCoursesService.registerToCourse(course.getId());
            type = DayCoursesView.OperationType.REGISTER_TO_COURSE;
        }
        addDisposable(operation.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        course.setIsRegistered(!isRegistered);
                        course.setRegisteredUsersNumber((!isRegistered) ?
                                course.getRegisteredUsersNumber() + 1 :
                                course.getRegisteredUsersNumber() - 1);
                        UpdateCourseObservable.newInstance(course).subscribe();
                        getView().displayOperationSuccessful(type, position);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getView().displayError(DayCoursesView.OperationType.REGISTER_TO_COURSE, position);
                    }
                }));
    }
}
