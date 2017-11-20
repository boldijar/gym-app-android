package com.gym.app.parts.mycourses;

import com.gym.app.data.SystemUtils;
import com.gym.app.data.model.Course;
import com.gym.app.data.observables.SaveCoursesObservable;
import com.gym.app.data.observables.UpdateCourseObservable;
import com.gym.app.data.room.AppDatabase;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.CoursesService;
import com.gym.app.utils.MvpObserver;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catalinradoiu
 * @since 2017.11.18
 */

public class MyCoursesPresenter extends Presenter<MyCoursesView> {

    @Inject
    CoursesService mCoursesService;

    @Inject
    AppDatabase mAppDatabase;

    @Inject
    SystemUtils mSystemUtils;

    MyCoursesPresenter(MyCoursesView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    void getCourses() {
        if (mSystemUtils.isNetworkUnavailable()) {
            loadCoursesOffline();
        } else {
            mCoursesService.getCoursesForCurrentUser(true)
                    .flatMap(new Function<List<Course>, ObservableSource<List<Course>>>() {
                        @Override
                        public ObservableSource<List<Course>> apply(@NonNull List<Course> courses) throws Exception {
                            List<Course> result = new ArrayList<>();
                            for (Course value : courses) {
                                if (value.getCourseDate() > System.currentTimeMillis() / 1000) {
                                    value.setIsRegistered(true);
                                    result.add(value);
                                }
                            }
                            return Observable.just(result);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MvpObserver<List<Course>>(this) {
                        @Override
                        public void onNext(List<Course> value) {
                            super.onNext(value);
                            SaveCoursesObservable.newInstance(value).subscribe();
                            getView().loadCourses(value);
                        }
                    });
        }
    }

    void removeCourse(final Course course, final int coursePosition) {
        addDisposable(mCoursesService.unregisterFromCourse(course.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        course.setIsRegistered(false);
                        UpdateCourseObservable.newInstance(course).subscribe();
                        getView().showRemoveSuccessful(coursePosition);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getView().showRemoveError(coursePosition);
                    }
                }));
    }

    private void loadCoursesOffline() {
        addDisposable(mAppDatabase.getCoursesDao().getRegisteredCourses()
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<List<Course>, SingleSource<List<Course>>>() {
                    @Override
                    public SingleSource<List<Course>> apply(@NonNull List<Course> courses) throws Exception {
                        List<Course> result = new ArrayList<>();
                        for (Course value : courses) {
                            if (value.getCourseDate() > System.currentTimeMillis() / 1000) {
                                result.add(value);
                            }
                        }
                        return Single.just(result);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Course>>() {
                    @Override
                    public void accept(@NonNull List<Course> courses) throws Exception {
                        if (courses.isEmpty()) {
                            getView().showNoCourses();
                        } else {
                            getView().loadCourses(courses);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getView().showNoCourses();
                    }
                }));
    }
}
