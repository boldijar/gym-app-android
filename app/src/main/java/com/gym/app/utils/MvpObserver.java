package com.gym.app.utils;


import com.gym.app.presenter.Presenter;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * @author Paul
 * @since 2017.08.30
 */

public class MvpObserver<T> implements Observer<T> {
    private final Presenter mPresenter;

    public MvpObserver(Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSubscribe(Disposable d) {
        mPresenter.addDisposable(d);
    }

    @Override
    public void onNext(T value) {

    }

    @Override
    public void onError(Throwable e) {
        Timber.e(e);
    }

    @Override
    public void onComplete() {

    }
}
