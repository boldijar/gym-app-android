package com.gym.app.parts.findcourses;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gym.app.data.model.Day;
import com.gym.app.parts.findcourses.day_courses.DayCoursesFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Pager adapter for the days of a week
 *
 * @author catalinradoiu
 * @since 02.01.2017
 */

class DayPagerAdapter extends FragmentStatePagerAdapter {

    private List<Day> mDaysList;

    DayPagerAdapter(FragmentManager fm) {
        super(fm);
        mDaysList = new ArrayList<>();
    }

    void setDaysList(List<Day> mDaysList) {
        this.mDaysList = mDaysList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return DayCoursesFragment.newFragment(mDaysList.get(position));
    }

    @Override
    public int getCount() {
        return mDaysList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDaysList.get(position).getDayName();
    }
}
