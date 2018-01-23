package com.gym.app.examstuff;

import com.gym.app.data.room.AppDatabase;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.ApiService;
import com.gym.app.utils.MvpObserver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.01.23
 */

public class ExamPresenter extends Presenter<ExamView> {

    @Inject
    ApiService mApiService;

    @Inject
    AppDatabase mAppDatabase;

    public ExamPresenter(ExamView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    public void loadTasks(int page) {
        getView().showProgress();
        mApiService.loadTasks(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MvpObserver<ExamResponse>(this) {
                    @Override
                    public void onNext(ExamResponse value) {
                        getView().showConnected(true);
                        getView().showTasks(value.mTasks, page);
                        cache(value.mTasks);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showConnected(false);
                        if (page == 1) {
                            tryLoadingOffline(e);
                        } else {
                            getView().showError(e);
                        }
                    }
                });
    }

    private void tryLoadingOffline(Throwable e) {
        mAppDatabase.taskDao().getTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Task>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(List<Task> tasks) {
                        if (tasks.size() == 0) {
                            getView().showError(e);
                        } else {
                            getView().showLoadedOffline(tasks);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }
                });


    }

    private void cache(List<Task> tasks) {
        Observable observable = Observable.create((ObservableOnSubscribe<Void>) e -> {
            mAppDatabase.taskDao().insertAll(tasks);
            Timber.e("Done inserting tasks");
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteTask(int id) {
        mApiService.deleteTask(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onComplete() {
                        getView().deleteSuccess(id);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().deleteError(id, e);
                    }
                });
    }

    public void loadToCache() {
        Observable.interval(5, TimeUnit.SECONDS)
                .flatMap(n ->
                        mApiService.loadTasks(1)
                                .subscribeOn(Schedulers.io()))
                .observeOn(Schedulers.io())
                .subscribe(examResponse -> {
                    Timber.e("Caching each 5 s! ");
                    cache(examResponse.mTasks);
                }, Throwable::printStackTrace);

    }
}
