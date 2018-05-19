package com.gym.app.server;

import com.gym.app.data.inovmodel.User;
import com.gym.app.data.model.AtTheGym;
import com.gym.app.data.model.Availability;
import com.gym.app.data.model.Car;
import com.gym.app.data.model.CarBody;
import com.gym.app.data.model.LoginResponse;
import com.gym.app.data.model.ParkPlace;
import com.gym.app.data.model.Product;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {


    @GET("user")
    Observable<User> getUser();

    @GET("admin/park_spots")
    Observable<List<ParkPlace>> getParkingPlaces();

    @GET("park_spots/{id}/availabilities")
    Observable<List<Availability>> getAvailabilities(@Path("id") int parkSpotId);


    @GET("park_spots")
    Observable<List<ParkPlace>> getParkingPlacesByCriterias(@Query("Latitude") String latitude,
                                                            @Query("Longitude") String longitude,
                                                            @Query("Radius") Integer radius,
                                                            @Query("start_datetime") String startDateTime,
                                                            @Query("end_datetime") String endDateTime);

    @GET("user/park_spots")
    Observable<List<ParkPlace>> getOwnParkingPlaces();

    @POST("cars")
    Observable<Car> addCar(@Body CarBody carBody);

    @GET("user/cars")
    Observable< List<Car> > getCars();




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

    @DELETE("api/newsletter/subscription")
    Completable unSubscribeToNewsLetter();


}
