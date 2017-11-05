package com.gym.app.parts.findcourses.day_courses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gym.app.R;
import com.gym.app.data.model.Course;
import com.gym.app.data.model.Day;
import com.gym.app.fragments.BaseFragment;
import com.gym.app.parts.findcourses.FindCoursesView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment for the courses from a certain day
 *
 * @author catalinradoiu
 * @since 2017.01.01
 */

public class DayCoursesFragment extends BaseFragment implements DayCoursesView {

    private static final String DAY_START = "dayStart";
    private static final String DAY_END = "dayEnd";

    @BindView(R.id.today_courses_recycler)
    RecyclerView mTodayCoursesRecycler;

    private DayCoursesAdapter mTodayCoursesAdapter = new DayCoursesAdapter();

    private DayCoursesPresenter mTodayCoursesPresenter;

    public static Fragment newFragment(Day day) {
        Bundle bundle = new Bundle();
        bundle.putLong(DAY_START, day.getStartTime());
        bundle.putLong(DAY_END, day.getEndTime());
        Fragment dayCoursesFragment = new DayCoursesFragment();
        dayCoursesFragment.setArguments(bundle);
        return dayCoursesFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mTodayCoursesPresenter = new DayCoursesPresenter(this);
        mTodayCoursesAdapter.setCourses(((FindCoursesView) getParentFragment()).getCoursesForDay(
                getArguments().getLong(DAY_START),
                getArguments().getLong(DAY_END)
        ));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mTodayCoursesRecycler.getContext(),
                linearLayoutManager.getOrientation());
        mTodayCoursesRecycler.setAdapter(mTodayCoursesAdapter);
        mTodayCoursesRecycler.setLayoutManager(linearLayoutManager);
        mTodayCoursesRecycler.addItemDecoration(itemDecoration);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_today_courses;
    }
}
