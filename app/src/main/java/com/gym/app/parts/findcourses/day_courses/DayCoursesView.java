package com.gym.app.parts.findcourses.day_courses;

/**
 * @author catalinradoiu
 * @since 2017.11.05
 */
interface DayCoursesView {

    enum OperationType {
        REMOVE_COURSE,
        REGISTER_TO_COURSE
    }

    void displayOperationSuccessful(OperationType operationType, int coursePosition);

    void displayError(OperationType operationType, int coursePosition);
}
