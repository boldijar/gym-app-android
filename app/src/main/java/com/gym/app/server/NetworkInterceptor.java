package com.gym.app.server;


import android.app.Application;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Paul
 * @since 2017.08.30
 */

public class NetworkInterceptor implements Interceptor {

    private final Application mApplication;

    public NetworkInterceptor(Application application) {
        mApplication = application;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request original = chain.request();

        Request.Builder builder = original.newBuilder();
        builder = builder.header("Authorization",
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1MjY4Mjg2NTAsInN1YiI6MX0.C9qfry7Ke6I8elT-dNLV8waZ3F5MQIQKm8o0fDCxrrw");
        Response response = chain.proceed(builder.build());
        return response;
    }
}
