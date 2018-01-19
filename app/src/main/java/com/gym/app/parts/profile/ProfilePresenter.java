package com.gym.app.parts.profile;

import android.text.TextUtils;

import com.gym.app.data.model.User;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.UserService;
import com.gym.app.utils.MvpObserver;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by flaviuoprea on 11/12/17.
 */

public class ProfilePresenter extends Presenter<ProfileView> {

    @Inject
    UserService userService;

    public ProfilePresenter(ProfileView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    public void getUser(){
        userService.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new MvpObserver<User>(this){
                    @Override
                    public void onNext(User value) {
                        getView().showUser(value);
                    }
                });
    }

    public void updateUser(String name, String password, File file){
        RequestBody methodBody = RequestBody.create(MediaType.parse("text/plain"), "PUT");
        if (file != null) {
            updateImageHelper(file, methodBody);
        }
        if (!TextUtils.isEmpty(name)) {
            updateNameHelper(name, methodBody);
        }
        if (!TextUtils.isEmpty(password)) {
            updatePasswordHelper(password, methodBody);
        }
    }

    private void updatePasswordHelper(String password, RequestBody methodBody) {
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);
        userService.updatePassword(methodBody, passwordBody)
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().updateMessage();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().showError();
                    }
                });
    }

    private void updateNameHelper(String name, RequestBody methodBody) {
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        userService.updateUserName(methodBody, nameBody)
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().updateMessage();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().showError();
                    }
                });
    }

    private void updateImageHelper(File file, RequestBody methodBody) {
        RequestBody pictureBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), pictureBody);
        userService.updateImage(methodBody, imagePart)
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().updateMessage();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().showError();
                    }
                });
    }
}
