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
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1MjY4MjI3OTMsInN1YiI6NDh9.QlBSWoE91_neq1AjZXr-h_yrPAdRg7x5rLzBGpQW7Fc");
        Response response = chain.proceed(builder.build());
        return response;
    }
}
