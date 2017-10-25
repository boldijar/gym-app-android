package com.gym.app.parts.mycourses;

import com.gym.app.R;
import com.gym.app.parts.home.BaseHomeFragment;

/**
 * Fragment will user own courses
 *
 * @author Paul
 * @since 2017.10.25
 */

public class MyCoursesFragment extends BaseHomeFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_courses;
    }

    @Override
    protected int getTitle() {
        return R.string.my_courses;
    }
}
