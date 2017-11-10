package com.gym.app.parts.findcourses.day_courses;

import android.support.annotation.IntDef;

import com.gym.app.data.model.Course;

import java.util.List;

/**
 * @author catalinradoiu
 * @since 2017.11.05
 */

public interface DayCoursesView {

    @IntDef({RegisterError.UNAVAILABLE_NETWORK, RegisterError.REGISTRATION_FAILURE})
    @interface RegisterError{
        int UNAVAILABLE_NETWORK = 1;
        int REGISTRATION_FAILURE = 2;
    }

    void displayRegisterSuccessful();

    void displayError(@RegisterError int errorType);

}
