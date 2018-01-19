package com.gym.app.parts.settings;

import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.ApiService;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Paul
 * @since 2018.01.17
 */

public class SettingsPresenter extends Presenter<SettingsView> {

    @Inject
    ApiService mApiService;

    public SettingsPresenter(SettingsView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    public void checkInUser(final boolean checkIn) {
        mApiService.checkInUser(checkIn,"PUT")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().checkInSuccess(checkIn);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable);
                    }
                });
    }
}
