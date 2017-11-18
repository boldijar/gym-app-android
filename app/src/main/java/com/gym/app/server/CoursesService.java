package com.gym.app.server;

import com.gym.app.data.model.Course;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Service for api request for the courses
 *
 * @author catalinradoiu
 * @since 2017.01.01
 */

public interface CoursesService {

    @GET("api/courses")
    Observable<List<Course>> getCoursesForPeriod(@Query("intervalStart") long intervalStart,
                                                 @Query("intervalStop") long intervalStop);

    @GET("api/courses")
    Observable<List<Course>> getCoursesForCurrentUser(@Query("usersCourses") boolean ownedCourses);

    @POST("api/course/{id}/subscription")
    Completable registerToCourse(@Path("id") int courseId);

    @DELETE("api/course/{id}/subscription")
    Completable unregisterFromCourse(@Path("id") int courseId);
}
