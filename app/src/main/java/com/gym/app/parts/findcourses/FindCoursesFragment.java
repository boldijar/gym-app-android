package com.gym.app.parts.findcourses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gym.app.R;
import com.gym.app.parts.home.BaseHomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Paul
 * @since 2017.10.25
 */

public class FindCoursesFragment extends BaseHomeFragment implements FindCoursesView {

    @BindView(R.id.my_courses_toolbar)
    Toolbar mCoursesToolbar;

    @BindView(R.id.courses_tab_layout)
    TabLayout mCoursesTabLayout;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find_courses;
    }

    @Override
    protected int getTitle() {
        return R.string.find_courses;
    }
}
