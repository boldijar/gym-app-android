package com.gym.app.data.observables;

import com.gym.app.data.model.Course;
import com.gym.app.data.room.AppDatabase;
import com.gym.app.di.InjectionHelper;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Observable for course update
 *
 * @author catalinradoiu
 * @since 2017.11.11
 */

public class UpdateCourseObservable implements ObservableOnSubscribe<Void> {

    @Inject
    AppDatabase mDatabase;

    private Course mCourse;

    private UpdateCourseObservable(Course course) {
        InjectionHelper.getApplicationComponent().inject(this);
        this.mCourse = course;
    }

    public static Observable<Void> newInstance(Course course) {
        return Observable.create(new UpdateCourseObservable(course)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void subscribe(ObservableEmitter<Void> e) throws Exception {
        mDatabase.getCoursesDao().updateCourse(mCourse);
        e.onComplete();
    }
}
