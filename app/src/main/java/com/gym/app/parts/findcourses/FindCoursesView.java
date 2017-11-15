package com.gym.app.parts.findcourses;

import com.gym.app.data.model.Course;
import com.gym.app.data.model.Day;

import java.util.List;

/**
 * Contains UI related method for the FindCoursesFragment
 *
 * @author catalinradoiu
 * @since 2017.10.31
 */

public interface FindCoursesView {

    void initDays(List<Day> days);

    void setLoaded();

    void setError();

    List<Course> getCoursesForDay(long dayStartTime, long dayEndTime);
}
