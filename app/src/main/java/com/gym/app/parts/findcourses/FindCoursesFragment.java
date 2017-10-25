package com.gym.app.parts.findcourses;

import com.gym.app.R;
import com.gym.app.parts.home.BaseHomeFragment;

/**
 * @author Paul
 * @since 2017.10.25
 */

public class FindCoursesFragment extends BaseHomeFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find_courses;
    }

    @Override
    protected int getTitle() {
        return R.string.find_courses;
    }
}
