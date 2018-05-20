package com.gym.app.parts.authentication;

import com.gym.app.data.model.Auth;
import com.gym.app.data.model.AuthBody;
import com.gym.app.data.model.JWT;
import com.gym.app.data.model.LoginResponse;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.ApiService;
import com.gym.app.utils.MvpObserver;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Paul
 * @since 2017.10.29
 */

public class AuthenticationPresenter extends Presenter<AuthenticationView> {

    @Inject
    ApiService mApiService;

    public AuthenticationPresenter(AuthenticationView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    void login(String email, String password) {
        mApiService.login(new AuthBody(new Auth(email, password)))
                .subscribeOn(Schedulers.io()) // where the request should be done
                .observeOn(AndroidSchedulers.mainThread()) // where the response should be handled
                .subscribe(new MvpObserver<JWT>(this) { // this is a cool class to help disposing observables
                    @Override
                    public void onNext(JWT value) {
                        getView().showLoginResponse(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getView().showError(e);
                    }
                });
    }
}
