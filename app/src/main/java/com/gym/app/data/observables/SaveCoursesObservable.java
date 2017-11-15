package com.gym.app.data.observables;

import com.gym.app.data.model.Course;
import com.gym.app.data.room.AppDatabase;
import com.gym.app.di.InjectionHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Observable for saving the courses in the database
 *
 * @author catalinradoiu
 * @since 2017.11.11
 */

public class SaveCoursesObservable implements ObservableOnSubscribe<Long> {

    private List<Course> mCoursesList;

    @Inject
    AppDatabase mAppDatabase;

    private SaveCoursesObservable(List<Course> courseList) {
        InjectionHelper.getApplicationComponent().inject(this);
        mCoursesList = courseList;
    }

    public static Observable<Long> newInstance(List<Course> courses) {
        return Observable.create(new SaveCoursesObservable(courses)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void subscribe(ObservableEmitter<Long> e) throws Exception {
        mAppDatabase.getCoursesDao().deleteAllCourses();
        mAppDatabase.getCoursesDao().insertCourses(mCoursesList);
        e.onComplete();
    }
}
