package com.gym.app.data.observables;

import com.gym.app.data.model.Product;
import com.gym.app.data.room.AppDatabase;
import com.gym.app.di.InjectionHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Observable that saves products
 *
 * @author Paul
 * @since 2017.10.29
 */

public class SaveProductsObservable implements ObservableOnSubscribe<Void> {

    private final List<Product> mProducts;

    @Inject
    AppDatabase mAppDatabase;

    private SaveProductsObservable(List<Product> products) {
        InjectionHelper.getApplicationComponent().inject(this);
        mProducts = products;
    }

    @Override
    public void subscribe(ObservableEmitter<Void> e) throws Exception {
        mAppDatabase.getDao().insertProducts(mProducts);
        Timber.d("Inserted " + mProducts.size() + " products in the database");
        e.onComplete();
    }

    public static Observable<Void> newInstance(List<Product> products) {
        return Observable.create(new SaveProductsObservable(products)).subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
    }
}
