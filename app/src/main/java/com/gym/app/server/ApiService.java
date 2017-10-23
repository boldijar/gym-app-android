package com.gym.app.server;

import com.gym.app.data.model.Event;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiService {

    @GET("api/events")
    Observable<ArrayList<Event>> getEvents();
}
