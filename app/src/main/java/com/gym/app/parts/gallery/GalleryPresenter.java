package com.gym.app.parts.gallery;

import com.gym.app.data.SystemUtils;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.GalleryService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catalinradoiu
 * @since 2018.01.18
 */

public class GalleryPresenter extends Presenter<GalleryView> {

    @Inject
    GalleryService galleryService;

    @Inject
    SystemUtils systemUtils;

    public GalleryPresenter(GalleryView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    public void loadPhotos() {
        galleryService.getPhotos().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> photos) throws Exception {
                        getView().setPhotos(photos);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().displayError();
                    }
                });
    }
}
