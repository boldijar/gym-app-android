package com.gym.app.parts.create_course;

import com.gym.app.data.SystemUtils;
import com.gym.app.data.model.Course;
import com.gym.app.data.room.AppDatabase;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.CoursesService;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author catalinradoiu
 * @since 2018.01.09
 */

public class CreateCoursePresenter extends Presenter<CreateCourseView> {

    @Inject
    CoursesService mCoursesService;

    @Inject
    AppDatabase mAppDatabase;

    CreateCoursePresenter(CreateCourseView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    void createCourse(String courseName, int capacity, long courseDate, File courseImage) {
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), courseName);
        RequestBody dateBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(courseDate));
        RequestBody capacityBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(capacity));
        MultipartBody.Part imageBody = MultipartBody.Part.createFormData("image", courseImage.getName(),
                RequestBody.create(MediaType.parse("image/*"), courseImage));
        addDisposable(mCoursesService.createCourse(nameBody, dateBody, capacityBody, imageBody)
                .doOnSuccess(new Consumer<Course>() {
                    @Override
                    public void accept(Course course) throws Exception {
                        mAppDatabase.getCoursesDao().insertCourse(course);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Course>() {
                    @Override
                    public void accept(Course course) throws Exception {
                        getView().displaySuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().displayError();
                    }
                }));
    }
}
