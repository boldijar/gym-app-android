package com.gym.app.parts.findcourses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gym.app.R;
import com.gym.app.data.model.Course;
import com.gym.app.data.model.Day;
import com.gym.app.parts.findcourses.day_courses.DayCoursesPresenter;
import com.gym.app.parts.findcourses.day_courses.DayCoursesView;
import com.gym.app.parts.home.BaseHomeFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Paul
 * @since 2017.10.25
 */

public class FindCoursesFragment extends BaseHomeFragment implements FindCoursesView {

    @BindView(R.id.find_courses_view_pager)
    ViewPager mFindCoursesViewPager;

    @BindView(R.id.find_courses_tab_layout)
    TabLayout mFindCoursesTabLayout;

    private FindCoursesPresenter mFindCoursesPresenter;

    private DayPagerAdapter mDayPagerAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mFindCoursesPresenter = new FindCoursesPresenter(this);
        mDayPagerAdapter = new DayPagerAdapter(getChildFragmentManager());
        mFindCoursesViewPager.setAdapter(mDayPagerAdapter);
        mFindCoursesTabLayout.setupWithViewPager(mFindCoursesViewPager);
        mFindCoursesTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        ViewCompat.setElevation(mFindCoursesTabLayout, getResources().getDimension(R.dimen.standard_elevation));
        mFindCoursesPresenter.initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mFindCoursesPresenter.destroySubscriptions();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find_courses;
    }

    @Override
    protected int getTitle() {
        return R.string.find_courses;
    }


    @Override
    public void initDays(List<Day> days) {
        mDayPagerAdapter.setmDaysList(days);
    }

    @Override
    public List<Course> getCoursesForDay(long dayStartTime, long dayEndTime) {
        return mFindCoursesPresenter.getCoursesForDay(dayStartTime, dayEndTime);
    }
}
