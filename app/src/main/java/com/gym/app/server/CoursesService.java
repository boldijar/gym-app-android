package com.gym.app.server;

import com.gym.app.data.model.Course;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Service for api request for the courses
 *
 * @author catalinradoiu
 * @since 2017.01.01
 */

public interface CoursesService {

    @GET("api/courses")
    Observable<List<Course>> getCoursesForPeriod(@Query("interval_start") long intervalStart,
                                                 @Query("interval_end") long intervalEnd);
}
