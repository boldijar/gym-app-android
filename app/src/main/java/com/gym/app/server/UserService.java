package com.gym.app.server;

import com.gym.app.data.model.User;

import java.io.File;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * @author flaviuoprea
 * @since 2017.11.12
 */

public interface UserService {

    @GET("api/user")
    Observable<User> getUser();

    @PUT("api/user")
    Completable updateUser(@Query("fullName") String fullName, @Query("password") String password, @Query("picture") File picture);

}


