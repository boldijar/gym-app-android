package com.gym.app.server;

import com.gym.app.data.inovmodel.User;
import com.gym.app.data.model.AtTheGym;
import com.gym.app.data.model.AuthBody;
import com.gym.app.data.model.Availability;
import com.gym.app.data.model.BookParking;
import com.gym.app.data.model.Car;
import com.gym.app.data.model.CarBody;
import com.gym.app.data.model.ParkPlaceBody;
import com.gym.app.data.model.JWT;
import com.gym.app.data.model.ParkPlace;
import com.gym.app.data.model.ParkingHistory;
import com.gym.app.data.model.ParkingHistoryBody;
import com.gym.app.data.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {


    @GET("user")
    Observable<User> getUser();

    @GET("park_spots/available")
    Observable<List<ParkPlace>> getParkingPlaces(@Query("latitude") Double latitude,
                                                 @Query("longitude") Double longitude,
                                                 @Query("radius") Double radius,
                                                 @Query("start_datetime") String startDateTime,
                                                 @Query("end_datetime") String endDateTime);

    @GET("park_spots/{id}/availabilities")
    Observable<List<Availability>> getAvailabilities(@Path("id") int parkSpotId);


    @GET("park_spots")
    Observable<List<ParkPlace>> getParkingPlacesByCriterias(@Query("Latitude") String latitude,
                                                            @Query("Longitude") String longitude,
                                                            @Query("Radius") Integer radius,
                                                            @Query("start_datetime") String startDateTime,
                                                            @Query("end_datetime") String endDateTime);

    @POST("park_spots")
    Observable< ParkPlace > addParkingSpot(@Body ParkPlaceBody parkPlaceBody);

    @GET("user/park_spots")
    Observable<List<ParkPlace>> getOwnParkingPlaces();

    @POST("park_spots/{id}/availabilities")
    Completable addAvailability(@Body Map body, @Path("id") int parkSpotId);

    @PATCH("park_spots/{parkSpotId}/availabilities/{availabilityId}")
    Completable fixAvailability(@Body Map body, @Path("parkSpotId") int parkSpotId, @Path("availabilityId") int availabilityId);

    @POST("cars")
    Observable<Car> addCar(@Body CarBody carBody);

    @GET("user/cars")
    Observable< List<Car> > getCars();

    @POST("park_spots/{id}/book")
    Completable bookParking(@Body BookParking bookParking, @Path("id") int parkSpotId);

    @GET("/bookings")
    Observable<ArrayList<ParkingHistory>> getParkingHistories();


    @FormUrlEncoded
    @POST("login")
    Observable<JWT> login(@Body AuthBody authBody);


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
