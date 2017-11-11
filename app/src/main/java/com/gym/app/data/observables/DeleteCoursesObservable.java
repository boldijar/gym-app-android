package com.gym.app.data.observables;

import com.gym.app.data.room.AppDatabase;
import com.gym.app.di.InjectionHelper;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Observable for deleting all the courses from the database
 *
 * @author catalinradoiu
 * @since 2017.11.11
 */

public class DeleteCoursesObservable implements ObservableOnSubscribe<Void> {

    @Inject
    AppDatabase mDatabase;

    private DeleteCoursesObservable() {
        InjectionHelper.getApplicationComponent().inject(this);
    }

    public static Observable<Void> newInstance() {
        return Observable.create(new DeleteCoursesObservable()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void subscribe(ObservableEmitter<Void> e) throws Exception {
        mDatabase.getCoursesDao().deleteAllCourses();
        e.onComplete();
    }
}
