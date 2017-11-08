package com.gym.app.parts.shop;

import com.gym.app.data.SystemUtils;
import com.gym.app.data.model.Product;
import com.gym.app.data.observables.SaveProductsObservable;
import com.gym.app.data.room.AppDatabase;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.ApiService;
import com.gym.app.utils.MvpObserver;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Paul
 * @since 2017.10.29
 */

public class ShopPresenter extends Presenter<ShopView> {

    @Inject
    ApiService mApiService;

    @Inject
    AppDatabase mAppDatabase;

    @Inject
    SystemUtils mSystemUtils;

    ShopPresenter(ShopView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    private void loadProductsOffline() {
        mAppDatabase.getDao()
                .getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Product>>() {
                    @Override
                    public void accept(@NonNull List<Product> products) throws Exception {
                        if (products.isEmpty()) {
                            getView().showError(new Exception("No products found in database!"));
                        } else {
                            getView().showProducts(products);
                        }
                    }
                });
    }

    void loadProducts() {
        if (mSystemUtils.isNetworkUnavailable()) {
            loadProductsOffline();
            return;
        }
        getView().showLoading();
        mApiService.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MvpObserver<List<Product>>(this) {
                    @Override
                    public void onNext(List<Product> value) {
                        getView().showProducts(value);
                        SaveProductsObservable.newInstance(value).subscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getView().showError(e);
                    }
                });
    }
}
