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
import com.gym.app.data.model.Course;
import com.gym.app.data.model.Day;
import com.gym.app.fragments.BaseFragment;
import com.gym.app.parts.findcourses.FindCoursesView;
import com.gym.app.utils.MvpObserver;
import com.gym.app.view.EmptyLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

    @BindView(R.id.day_courses_empty_layout)
    EmptyLayout mEmptyLayout;

    private DayCoursesPresenter mDayCoursesPresenter;
    private DayCoursesAdapter mTodayCoursesAdapter = new DayCoursesAdapter();
    private Snackbar mRetrySnackBar;
    private Toast mOperationStatus;
    private LinearLayoutManager mLinearLayoutManager;

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
        initPresenter();
        setListeners();
        initRecycler();
    }

    /*
        This method is needed when switching the fragment in the pager
        Because of the FragmentStatePagerAdapter, the state of the fragment is retained so no
        other life-cycle method works for hiding the snackbar
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (mRetrySnackBar != null) {
                mRetrySnackBar.dismiss();
            }
            if (mOperationStatus != null) {
                mOperationStatus.cancel();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDayCoursesPresenter.destroySubscriptions();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_today_courses;
    }

    @Override
    public void displayOperationSuccessful(final OperationType operationType, int coursePosition) {
        String toastMessage = "";
        switch (operationType) {
            case REGISTER_TO_COURSE:
                toastMessage = getString(R.string.registration_successful);
                break;
            case REMOVE_COURSE:
                toastMessage = getString(R.string.unregister_successful);
                break;
        }
        if (mOperationStatus != null) {
            mOperationStatus.cancel();
        }
        mOperationStatus = Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT);
        mOperationStatus.show();
        mTodayCoursesAdapter.updateCourse(coursePosition);
        enableCourseButton(coursePosition);
    }

    @Override
    public void displayError(final OperationType operationType, final int coursePosition) {
        if (getView() != null) {
            mRetrySnackBar = Snackbar.make(getView(),
                    getString(R.string.network_error), Snackbar.LENGTH_LONG);
            mRetrySnackBar.setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLinearLayoutManager.findViewByPosition(coursePosition)
                            .findViewById(R.id.handle_course_button)
                            .setClickable(false);
                    switch (operationType) {
                        case REGISTER_TO_COURSE:
                            mDayCoursesPresenter.handleCourseClick(mTodayCoursesAdapter
                                    .getCourse(coursePosition), coursePosition, false);
                            break;
                        case REMOVE_COURSE:
                            mDayCoursesPresenter.handleCourseClick(mTodayCoursesAdapter
                                    .getCourse(coursePosition), coursePosition, true);
                            break;
                    }
                }
            });
            mRetrySnackBar.show();
        }
        enableCourseButton(coursePosition);
    }

    private void setListeners() {
        mTodayCoursesAdapter.setDayCourseClickListener(new DayCoursesAdapter.DayCourseClickListener() {
            @Override
            public void onClick(int position) {
                mLinearLayoutManager.findViewByPosition(position)
                        .findViewById(R.id.handle_course_button)
                        .setClickable(false);
                Course course = mTodayCoursesAdapter.getCourse(position);
                mDayCoursesPresenter.handleCourseClick(course, position, course.isRegistered());
            }
        });
    }

    private void initRecycler() {
        List<Course> courseList = ((FindCoursesView) getParentFragment()).getCoursesForDay(
                getArguments().getLong(DAY_START),
                getArguments().getLong(DAY_END));
        if (!courseList.isEmpty()) {
            mTodayCoursesRecycler.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
            mTodayCoursesAdapter.setCourses(courseList);
            mLinearLayoutManager = new LinearLayoutManager(getContext());
            DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),
                    mLinearLayoutManager.getOrientation());
            mTodayCoursesRecycler.setAdapter(mTodayCoursesAdapter);
            mTodayCoursesRecycler.setLayoutManager(mLinearLayoutManager);
            mTodayCoursesRecycler.addItemDecoration(itemDecoration);
        } else {
            mEmptyLayout.setState(EmptyLayout.State.EMPTY_NO_BUTTON, R.string.no_courses_for_day);
        }
    }

    private void initPresenter() {
        mDayCoursesPresenter = new DayCoursesPresenter(this);
    }

    private void enableCourseButton(final int coursePosition) {
        Observable.timer(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MvpObserver<Long>(mDayCoursesPresenter) {

                    @Override
                    public void onNext(Long aLong) {
                        mLinearLayoutManager.findViewByPosition(coursePosition).findViewById(R.id.handle_course_button)
                                .setClickable(true);
                    }
                });
    }
}
