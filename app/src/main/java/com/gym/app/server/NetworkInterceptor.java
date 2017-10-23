package com.gym.app.server;


import com.gym.app.data.Prefs;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Paul
 * @since 2017.08.30
 */

public class NetworkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request original = chain.request();

        final Request.Builder builder = original.newBuilder()
                .header("Authorization", "Bearer " + Prefs.Token.get());

        Response response = chain.proceed(builder.build());
        if (!response.isSuccessful()) {
            throw new ServerException(response.message(), response.code());
        }
        return response;
    }
}
