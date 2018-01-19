package com.gym.app.parts.trainedcourses;

import com.gym.app.data.model.Course;

import java.util.List;

/**
 * @author catalinradoiu
 * @since 1/19/2018
 */

interface TrainedCoursesView {

    void loadCourses(List<Course> courseList);

    void displayLoadError();

    void displayDeleteSuccess(int position);

    void displayDeleteError(int id, int position);
}
