package com.gym.app.parts.findcourses.day_courses;

import android.support.annotation.IntDef;

/**
 * @author catalinradoiu
 * @since 2017.11.05
 */

public interface DayCoursesView {

    @IntDef({OperationType.REGISTER_TO_COURSE, OperationType.REMOVE_COURSE})
    @interface OperationType {
        int REMOVE_COURSE = 1;
        int REGISTER_TO_COURSE = 2;
    }

    void displayOperationSuccessful(@OperationType int operationType);

    void displayError(@OperationType int operationType);
}
