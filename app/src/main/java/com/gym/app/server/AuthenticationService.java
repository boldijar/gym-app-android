package com.gym.app.server;

import io.reactivex.Completable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author catlalinradoiu
 * @since 2017.11.15
 */

public interface AuthenticationService {

    @FormUrlEncoded
    @POST("api/register")
    Completable registerUser(@Field("email") String email,
                             @Field("password") String password,
                             @Field("fullName") String username);
}
