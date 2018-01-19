package com.gym.app.parts.trainedcourses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gym.app.R;
import com.gym.app.data.model.Course;
import com.gym.app.parts.home.BaseHomeFragment;
import com.gym.app.parts.updatecourse.UpdateCourseActivity;
import com.gym.app.view.EmptyLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author catalinradoiu
 * @since 1/19/2018
 */

public class TrainedCoursesFragment extends BaseHomeFragment implements TrainedCoursesView {

    private static final int COURSE_EDIT_REQUEST_CODE = 1;

    @BindView(R.id.trained_courses_recycler)
    RecyclerView mTrainedCoursesRecycler;

    @BindView(R.id.trained_course_empty_layout)
    EmptyLayout mEmptyLayout;

    private TrainedCoursesAdapter mTrainedCoursesAdapter;
    private TrainedCoursesPresenter mTrainedCoursesPresenter;
    private Snackbar operationSnackBar;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initEmptyLayout();
        initPresenter();
        initAdapter();
        initListeners();
        mTrainedCoursesPresenter.loadTrainedCourses();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COURSE_EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK &&
                data.getBooleanExtra(UpdateCourseActivity.UPDATED, false)) {
            Course course = data.getParcelableExtra(UpdateCourseActivity.COURSE);
            mTrainedCoursesAdapter.updateCouse(data.getIntExtra(UpdateCourseActivity.POSITION, -1), course);
        }
    }

    @Override
    public void loadCourses(List<Course> courseList) {
        if (!courseList.isEmpty()) {
            mEmptyLayout.setState(EmptyLayout.State.CLEAR);
            mTrainedCoursesAdapter.setCourses(courseList);
        } else {
            mEmptyLayout.setState(EmptyLayout.State.EMPTY_NO_BUTTON, R.string.no_courses_created);
        }
    }

    @Override
    public void displayLoadError() {
        mEmptyLayout.setState(EmptyLayout.State.EMPTY, R.string.could_not_find_courses);
    }

    @Override
    public void displayDeleteSuccess(int position) {
        mTrainedCoursesAdapter.deleteCourse(position);
        if (mTrainedCoursesAdapter.getItemCount() == 0) {
            mEmptyLayout.setState(EmptyLayout.State.EMPTY_NO_BUTTON, R.string.no_courses_created);
        }
    }

    @Override
    public void displayDeleteError(final int id, final int position) {
        if (operationSnackBar != null && operationSnackBar.isShown()) {
            operationSnackBar.dismiss();
        }
        operationSnackBar = Snackbar.make(mTrainedCoursesRecycler, getString(R.string.delete_course_error),
                Snackbar.LENGTH_LONG);
        operationSnackBar.setAction(getString(R.string.retry), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTrainedCoursesPresenter.deleteCourse(id, position);
            }
        });
        operationSnackBar.show();
    }

    @Override
    protected int getTitle() {
        return R.string.trained_courses;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_trained_courses;
    }

    private void initPresenter() {
        mTrainedCoursesPresenter = new TrainedCoursesPresenter(this);
    }

    private void initAdapter() {
        mTrainedCoursesAdapter = new TrainedCoursesAdapter();
        mTrainedCoursesRecycler.setAdapter(mTrainedCoursesAdapter);
        mTrainedCoursesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mTrainedCoursesRecycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void initEmptyLayout() {
        mEmptyLayout.setState(EmptyLayout.State.LOADING);
        mEmptyLayout.setOnRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTrainedCoursesPresenter.loadTrainedCourses();
            }
        });
    }

    private void initListeners() {
        mTrainedCoursesAdapter.setOnDeleteClickListener(new TrainedCoursesAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                mTrainedCoursesPresenter.deleteCourse(mTrainedCoursesAdapter.getCourse(position).getId(), position);
            }
        });
        mTrainedCoursesAdapter.setOnEditClickListener(new TrainedCoursesAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(int position) {
                startActivityForResult(UpdateCourseActivity.getStartIntent(getContext(),
                        mTrainedCoursesAdapter.getCourse(position), position), COURSE_EDIT_REQUEST_CODE);
            }
        });
    }
}
