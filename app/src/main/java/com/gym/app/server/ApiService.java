package com.gym.app.server;

import com.gym.app.data.inovmodel.User;
import com.gym.app.data.model.AtTheGym;
import com.gym.app.data.model.LoginResponse;
import com.gym.app.data.model.ParkPlace;
import com.gym.app.data.model.ParkingHistory;
import com.gym.app.data.model.ParkingHistoryBody;
import com.gym.app.data.model.Product;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {


    @GET("user")
    Observable<User> getUser();


    @GET("admin/park_spots")
    Observable<List<ParkPlace>> getParkingPlaces();

    @GET("/bookings")
    Observable<ArrayList<ParkingHistory>> getParkingHistories();


    @FormUrlEncoded
    @POST("api/login")
    Observable<LoginResponse> login(@Field("email") String email, @Field("password") String password);

    @GET("api/products")
    Observable<List<Product>> getProducts();

    @POST("api/user")
    @FormUrlEncoded
    Completable checkInUser(@Field("isAtTheGym") boolean isAtTheGym,
                            @Field("_method") String method);

    @GET("api/users/at-the-gym")
    Observable<AtTheGym> getNumberOfUsers();

    @POST("api/newsletter/subscription")
    Completable subscribeToNewsLetter();

    @POST("user/bookings")
    Observable<ParkingHistory> addParkingHistory(@Body ParkingHistoryBody parkingHistoryBody);

    @DELETE("api/newsletter/subscription")
    Completable unSubscribeToNewsLetter();


}
