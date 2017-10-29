package com.gym.app.server;

import com.gym.app.data.model.Event;
import com.gym.app.data.model.LoginResponse;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("api/events")
    Observable<ArrayList<Event>> getEvents();

    @POST("api/login")
    Observable<LoginResponse> login(@Query("email") String email, @Query("password") String password);
}
