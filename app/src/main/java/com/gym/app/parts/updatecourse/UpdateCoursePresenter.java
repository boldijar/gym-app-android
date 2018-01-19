package com.gym.app.parts.updatecourse;

import com.gym.app.data.model.Course;
import com.gym.app.data.room.AppDatabase;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.CoursesService;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author catalinradoiu
 * @since 1/19/2018
 */

public class UpdateCoursePresenter extends Presenter<UpdateCourseView> {

    @Inject
    CoursesService coursesService;

    @Inject
    AppDatabase appDatabase;

    UpdateCoursePresenter(UpdateCourseView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    void updateCourse(int courseId, String courseName, int capacity, long courseTime) {
        coursesService.updateCourse(courseId, courseName, courseTime, capacity, "PUT")
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().displaySuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().displayError();
                    }
                });
    }
}
