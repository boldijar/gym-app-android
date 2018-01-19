package com.gym.app.server;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * @author catalinradoiu
 * @since 1/18/2018
 */

public interface GalleryService {

    @GET("api/media/photos")
    Single<List<String>> getPhotos();
}
