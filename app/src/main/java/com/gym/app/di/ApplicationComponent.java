package com.gym.app.di;

import com.gym.app.Shaorma;
import com.gym.app.data.observables.SaveProductsObservable;
import com.gym.app.parts.authentication.AuthenticationPresenter;
import com.gym.app.parts.shop.ShopPresenter;

import dagger.Component;

@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {

    void inject(Shaorma shaorma);

    void inject(AuthenticationPresenter authenticationPresenter);

    void inject(ShopPresenter shopPresenter);

    void inject(SaveProductsObservable saveProductsObservable);
}