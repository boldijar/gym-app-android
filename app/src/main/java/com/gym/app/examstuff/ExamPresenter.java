package com.gym.app.examstuff;

import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.ApiService;
import com.gym.app.utils.MvpObserver;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.01.23
 */

public class ExamPresenter extends Presenter<ExamView> {

    @Inject
    ApiService mApiService;

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
                        getView().showTasks(value.mTasks);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                        getView().showConnected(false);

                    }
                });
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
                        getView().deleteError(id,e);
                    }
                });
    }
}
