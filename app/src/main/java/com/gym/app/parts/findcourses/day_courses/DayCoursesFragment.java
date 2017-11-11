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

    private int mCurrentCoursePosition;

    private Snackbar mRetrySnackBar;

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
                mCurrentCoursePosition = position;
                mDayCoursesPresenter.registerToCourse(mTodayCoursesAdapter.getCourse(position));
            }
        });
        mTodayCoursesAdapter.setOnRemoveClickListener(new DayCoursesAdapter.OnRemoveClickListener() {
            @Override
            public void onClick(int position) {
                mCurrentCoursePosition = position;
                mDayCoursesPresenter.unregisterFromCourse(mTodayCoursesAdapter.getCourse(position));
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
    public void onDetach() {
        super.onDetach();
        if (mRetrySnackBar != null) {
            mRetrySnackBar.dismiss();
        }
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
        switch (operationType) {

            case OperationType.REGISTER_TO_COURSE:
                toastMessage = getString(R.string.registration_successful);
                break;
            case OperationType.REMOVE_COURSE:
                toastMessage = getString(R.string.unregister_successful);
                break;
        }
        if (mOperationStatus != null) {
            mOperationStatus.cancel();
        }
        mOperationStatus = Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT);
        mOperationStatus.show();
        mTodayCoursesAdapter.updateCourse(mCurrentCoursePosition);
    }

    @Override
    public void displayError(@OperationType final int operationType) {
        if (getView() != null) {
            mRetrySnackBar = Snackbar.make(getView(),
                    getString(R.string.network_error), Snackbar.LENGTH_LONG);
            mRetrySnackBar.setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (operationType) {
                        case OperationType.REGISTER_TO_COURSE:
                            mDayCoursesPresenter.unregisterFromCourse(mTodayCoursesAdapter
                                    .getCourse(mCurrentCoursePosition));
                            break;
                        case OperationType.REMOVE_COURSE:
                            mDayCoursesPresenter.registerToCourse(mTodayCoursesAdapter
                                    .getCourse(mCurrentCoursePosition));
                            break;
                    }
                }
            });
            mRetrySnackBar.show();
        }
    }
}
