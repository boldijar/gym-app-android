package com.gym.app.parts.findcourses.day_courses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.gym.app.R;
import com.gym.app.data.model.Day;
import com.gym.app.fragments.BaseFragment;
import com.gym.app.parts.findcourses.FindCoursesView;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private DayCoursesPresenter mDayCoursesPresenter;

    private DayCoursesAdapter mTodayCoursesAdapter = new DayCoursesAdapter();

    private int mCurrentCourseId;

    private int mOperationType;

    private Snackbar mRetrySnackbar;

    private Toast mOperationStatus;

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
        mDayCoursesPresenter = new DayCoursesPresenter(this);
        mTodayCoursesAdapter.setCourses(((FindCoursesView) getParentFragment()).getCoursesForDay(
                getArguments().getLong(DAY_START),
                getArguments().getLong(DAY_END)
        ));
        mTodayCoursesAdapter.setOnRegisterClickListener(new DayCoursesAdapter.OnRegisterClickListener() {
            @Override
            public void onClick(int position) {
                mCurrentCourseId = mTodayCoursesAdapter.getCourse(position).getId();
                mDayCoursesPresenter.registerToCourse(mCurrentCourseId);
            }
        });
        mTodayCoursesAdapter.setOnRemoveClickListener(new DayCoursesAdapter.OnRemoveClickListener() {
            @Override
            public void onClick(int position) {
                mCurrentCourseId = mTodayCoursesAdapter.getCourse(position).getId();
                mDayCoursesPresenter.unregisterFromCourse(mCurrentCourseId);
            }
        });
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

    @Override
    public void displayOperationSuccessful(@OperationType int operationType) {
        String toastMessage = "";
        int courseStatus = 0;
        switch (operationType) {

            case OperationType.REGISTER_TO_COURSE:
                toastMessage = getString(R.string.registration_successful);
                courseStatus = 1;
                break;
            case OperationType.REMOVE_COURSE:
                toastMessage = getString(R.string.unregister_successful);
                courseStatus = -1;
                break;
        }
        if (mOperationStatus != null) {
            mOperationStatus.cancel();
        }
        mOperationStatus = Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT);
        mOperationStatus.show();
        mTodayCoursesAdapter.changeCourseStatus(mCurrentCourseId, courseStatus);
    }

    @Override
    public void displayError(@OperationType final int operationType) {
        if (getView() != null) {
            mRetrySnackbar = Snackbar.make(getView(),
                    getString(R.string.network_error), Snackbar.LENGTH_LONG);
            mRetrySnackbar.setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (operationType) {
                        case OperationType.REGISTER_TO_COURSE:
                            mDayCoursesPresenter.unregisterFromCourse(mCurrentCourseId);
                            break;
                        case OperationType.REMOVE_COURSE:
                            mDayCoursesPresenter.registerToCourse(mCurrentCourseId);
                            break;
                    }
                }
            });
            mRetrySnackbar.show();
        }
    }

}
