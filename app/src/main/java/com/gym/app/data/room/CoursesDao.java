package com.gym.app.data.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.gym.app.data.model.Course;

import java.util.List;

import io.reactivex.Single;

/**
 * Dao for database courses manipulation
 *
 * @author catalinradoiu
 * @since 2017.11.11
 */

@Dao
public interface CoursesDao {

    @Query("DELETE FROM courses")
    void deleteAllCourses();

    @Query("SELECT * FROM courses")
    Single<List<Course>> getAllCourses();

    @Query("SELECT * FROM courses WHERE isUserRegistered = 1")
    Single<List<Course>> getRegisteredCourses();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourses(List<Course> courses);

    @Update
    void updateCourse(Course course);
}
