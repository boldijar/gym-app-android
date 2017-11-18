package com.gym.app.server;

import io.reactivex.Completable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author catlalinradoiu
 * @since 2017.11.15
 */

public interface AuthenticationService {

    @POST("api/register")
    Completable registerUser(@Query("email") String email,
                             @Query("password") String password,
                             @Query("fullName") String username);
}
