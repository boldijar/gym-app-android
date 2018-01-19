package com.gym.app.parts.trainedcourses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
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
 * @author catalinradoiu
 * @since 1/19/2018
 */

public class TrainedCoursesFragment extends BaseHomeFragment implements TrainedCoursesView {

    @BindView(R.id.trained_courses_recycler)
    RecyclerView mTrainedCoursesRecycler;

    @BindView(R.id.trained_course_empty_layout)
    EmptyLayout mEmptyLayout;

    private TrainedCoursesAdapter mTrainedCoursesAdapter;
    private TrainedCoursesPresenter mTrainedCoursesPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initEmptyLayout();
        initPresenter();
        initAdapter();
        mTrainedCoursesPresenter.loadTrainedCourses();
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
}
