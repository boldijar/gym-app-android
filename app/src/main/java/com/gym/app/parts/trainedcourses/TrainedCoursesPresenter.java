package com.gym.app.parts.trainedcourses;

import com.gym.app.data.SystemUtils;
import com.gym.app.data.model.Course;
import com.gym.app.data.room.AppDatabase;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.CoursesService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.Observable;
import rx.Subscriber;

/**
 * @author catalinradoiu
 * @since 1/19/2018
 */

public class TrainedCoursesPresenter extends Presenter<TrainedCoursesView> {

    @Inject
    CoursesService mCoursesService;

    @Inject
    AppDatabase mAppDatabase;

    @Inject
    SystemUtils mSystemUtils;

    TrainedCoursesPresenter(TrainedCoursesView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    void loadTrainedCourses() {
            addDisposable(mCoursesService.getTrainedCourses(true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Course>>() {
                        @Override
                        public void accept(List<Course> courses) throws Exception {
                            getView().loadCourses(courses);
                        }
                    }, throwable -> {
                        getView().displayLoadError();
                    }));
    }

    void deleteCourse(final int id, final int position) {
        mCoursesService.deleteCourse(id)
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mAppDatabase.getCoursesDao().deleteCourse(id);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().displayDeleteSuccess(position);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().displayDeleteError(id, position);
                    }
                });
    }

    void update(final Course course) {
        Observable.create((Observable.OnSubscribe<Void>) subscriber -> mAppDatabase.getCoursesDao().insertCourse(course));

    }

}
