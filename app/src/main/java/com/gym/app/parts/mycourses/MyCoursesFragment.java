package com.gym.app.parts.mycourses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gym.app.R;
import com.gym.app.data.model.Course;
import com.gym.app.parts.home.BaseHomeFragment;
import com.gym.app.view.EmptyLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment will user own courses
 *
 * @author Paul
 * @author catalinradoiu
 * @since 2017.10.25
 */

public class MyCoursesFragment extends BaseHomeFragment implements MyCoursesView {

    @BindView(R.id.my_courses_recycler)
    RecyclerView mCoursesRecycler;

    @BindView(R.id.my_courses_empty_layout)
    EmptyLayout mEmptyLayout;

    private MyCoursesPresenter mCoursesPresenter;
    private CoursesAdapter mCoursesAdapter;
    private Snackbar mRetrySnackBar;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initStateLayout();
        initRecycler();
        initPresenter();
        mCoursesPresenter.getCourses();
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
        if (!courseList.isEmpty()) {
            mEmptyLayout.setState(EmptyLayout.State.CLEAR);
            mCoursesRecycler.setVisibility(View.VISIBLE);
            mCoursesAdapter.setCourses(courseList);
        } else {
            mEmptyLayout.setState(EmptyLayout.State.EMPTY_NO_BUTTON, R.string.no_course_registrations);
        }
    }

    @Override
    public void showRemoveSuccessful(int coursePosition) {
        mCoursesAdapter.removeCourse(coursePosition);
        if (mCoursesAdapter.getItemCount() == 0) {
            mEmptyLayout.setState(EmptyLayout.State.EMPTY_NO_BUTTON, R.string.no_course_registrations);
            mCoursesRecycler.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRemoveError(final int coursePosition) {
        if (mRetrySnackBar != null) {
            mRetrySnackBar.dismiss();
        }
        if (getView() != null) {
            mRetrySnackBar = Snackbar.make(getView(), getString(R.string.network_error), Snackbar.LENGTH_LONG);
            mRetrySnackBar.setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCoursesPresenter.removeCourse(mCoursesAdapter.getCourse(coursePosition), coursePosition);
                }
            });
            mRetrySnackBar.show();
        }
    }

    @Override
    public void showNoCourses() {
        mEmptyLayout.setState(EmptyLayout.State.EMPTY, R.string.could_not_find_courses);
    }

    private void initPresenter() {
        mCoursesPresenter = new MyCoursesPresenter(this);
    }

    private void initRecycler() {
        mCoursesAdapter = new CoursesAdapter();
        mCoursesAdapter.setRemoveCourseListener(new CoursesAdapter.OnRemoveCourseListener() {
            @Override
            public void onClick(int position) {
                mCoursesPresenter.removeCourse(mCoursesAdapter.getCourse(position), position);
            }
        });
        mCoursesRecycler.setAdapter(mCoursesAdapter);
        mCoursesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initStateLayout() {
        mEmptyLayout.setState(EmptyLayout.State.LOADING);
        mEmptyLayout.setOnRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmptyLayout.setState(EmptyLayout.State.LOADING);
                mCoursesPresenter.getCourses();
            }
        });
    }
}
