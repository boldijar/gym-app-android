package com.gym.app.parts.mycourses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gym.app.R;
import com.gym.app.data.model.Course;
import com.gym.app.parts.home.BaseHomeFragment;

import java.util.List;

/**
 * Fragment will user own courses
 *
 * @author Paul
 * @since 2017.10.25
 */

public class MyCoursesFragment extends BaseHomeFragment implements MyCoursesView {

    private MyCoursesPresenter mCoursesPresenter;
    private CoursesAdapter mCoursesAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCoursesPresenter.destroySubscriptions();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_courses;
    }

    @Override
    protected int getTitle() {
        return R.string.my_courses;
    }

    @Override
    public void loadCourses(List<Course> courseList) {

    }
}
