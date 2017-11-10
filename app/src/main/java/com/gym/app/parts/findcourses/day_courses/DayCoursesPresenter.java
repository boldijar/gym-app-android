package com.gym.app.parts.findcourses.day_courses;

import com.gym.app.data.SystemUtils;
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
    SystemUtils systemUtils;

    public DayCoursesPresenter(DayCoursesView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    public void registerToCourse(int courseId) {
        if (!systemUtils.isNetworkUnavailable()) {
            mCoursesService.registerToCourse(courseId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action() {
                        @Override
                        public void run() throws Exception {
                            getView().displayRegisterSuccessful();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            getView().displayError(DayCoursesView.RegisterError.REGISTRATION_FAILURE);
                        }
                    });
        } else {
            getView().displayError(DayCoursesView.RegisterError.UNAVAILABLE_NETWORK);
        }
    }
}
