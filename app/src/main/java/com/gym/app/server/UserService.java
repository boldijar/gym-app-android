package com.gym.app.server;

import com.gym.app.data.model.User;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @author flaviuoprea
 * @since 2017.11.12
 */

public interface UserService {

    @GET("api/user")
    Observable<User> getUser();

    @Multipart
    @POST("api/user")
    Completable updateUser(@Part("_method") RequestBody method,
                            @Part("fullName") RequestBody fullName,
                           @Part("password") RequestBody password,
                           @Part MultipartBody.Part picture);

/*
    @PUT("api/user/{id}")
    Completable updateUser(@Path("id") int id,*/
}


