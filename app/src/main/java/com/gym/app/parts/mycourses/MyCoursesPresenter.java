package com.gym.app.parts.mycourses;

import com.gym.app.data.SystemUtils;
import com.gym.app.data.model.Course;
import com.gym.app.data.room.AppDatabase;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.CoursesService;
import com.gym.app.utils.MvpObserver;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
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

    public MyCoursesPresenter(MyCoursesView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    public void getCourses() {
        mCoursesService.getCoursesForCurrentUser(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MvpObserver<List<Course>>(this) {
                    @Override
                    public void onNext(List<Course> value) {
                        super.onNext(value);
                    }
                });
    }

    private void loadCoursesOffline() {

    }
}
