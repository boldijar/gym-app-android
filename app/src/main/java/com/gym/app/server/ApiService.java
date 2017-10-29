package com.gym.app.server;

import com.gym.app.data.model.LoginResponse;
import com.gym.app.data.model.Product;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/login")
    Observable<LoginResponse> login(@Query("email") String email, @Query("password") String password);

    @GET("api/products")
    Observable<List<Product>> getProducts();
}
