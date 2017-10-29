package com.gym.app.presenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * @author Paul
 * @since 2017.08.30
 */

public class Presenter<T> {

    private T mView;
    private List<Disposable> mDisposables;

    public Presenter(T view) {
        mView = view;
    }

    public T getView() {
        return mView;
    }

    public void addDisposable(Disposable disposable) {
        if (mDisposables == null) {
            mDisposables = new ArrayList<>();
        }
        mDisposables.add(disposable);
    }

    public void destroySubscriptions() {
        if (mDisposables != null) {
            for (Disposable disposable : mDisposables) {
                disposable.dispose();
            }
            mDisposables = null;
        }
        mView = null;
    }
}
