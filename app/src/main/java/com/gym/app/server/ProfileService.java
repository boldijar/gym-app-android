package com.gym.app.server;

import io.reactivex.Completable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.17
 */

public interface ProfileService {

    @FormUrlEncoded
    @PUT("api/user")
    Completable updateUser(@Field("password") String password,
                             @Field("fullName") String username);
}
