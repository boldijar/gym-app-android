package com.gym.app.parts.mycourses;

import com.gym.app.data.model.Course;

import java.util.List;

/**
 * @author catalinradoiu
 * @since 2017.11.18
 */

interface MyCoursesView {

    void loadCourses(List<Course> courseList);

    void showRemoveSuccessful(int coursePosition);

    void showRemoveError(int coursePosition);

    void showNoCourses();
}
