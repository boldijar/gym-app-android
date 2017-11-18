package com.gym.app.parts.authentication.register;

import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.AuthenticationService;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catalinradoiu
 * @since 2017.11.15
 */

public class RegisterPresenter extends Presenter<RegisterView> {

    @Inject
    AuthenticationService mAuthenticationService;

    RegisterPresenter(RegisterView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    void register(String username, String email, String password) {
        addDisposable(mAuthenticationService.registerUser(email, password, username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().displayRegistrationSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getView().displayRegistrationError();
                    }
                }));
    }
}
