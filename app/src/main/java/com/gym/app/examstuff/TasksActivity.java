package com.gym.app.examstuff;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gym.app.R;
import com.gym.app.activities.BaseActivity;
import com.gym.app.view.EmptyLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Paul
 * @since 2018.01.23
 */

public class TasksActivity extends BaseActivity implements ExamView, TaskAdapter.Listener {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty)
    EmptyLayout mEmptyLayout;
    @BindView(R.id.status)
    TextView mStatus;

    private AlertDialog mProgressDialog;

    private ExamPresenter mExamPresenter = new ExamPresenter(this);
    private int mPage = 1;
    private TaskAdapter mTaskAdapter = new TaskAdapter(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        ButterKnife.bind(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deleting task....");
        builder.setMessage("Wait just a sec");
        builder.setCancelable(false);
        mProgressDialog = builder.create();
        loadRecycler();
        loadTasks();
        mEmptyLayout.setOnRetryListener(view -> loadTasks());
    }

    private void loadRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mTaskAdapter);
    }

    private void loadTasks() {
        mExamPresenter.loadTasks(mPage);
    }

    @Override
    public void showTasks(List<Task> tasks) {
        mPage++;
        mTaskAdapter.addTasks(tasks);
        mEmptyLayout.setState(EmptyLayout.State.CLEAR);
    }

    @Override
    public void showProgress() {
        mEmptyLayout.setState(EmptyLayout.State.LOADING);
    }

    @Override
    public void showError(Throwable error) {
        mEmptyLayout.setState(EmptyLayout.State.ERROR, error.getMessage());
    }

    @Override
    public void showConnected(boolean conencted) {
        mStatus.setVisibility(conencted ? View.GONE : View.VISIBLE);
    }

    @Override
    public void deleteSuccess(int id) {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mTaskAdapter.deleteTask(id);
        Toast.makeText(this, "Task id " + id + " was deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteError(int id, Throwable e) {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        Toast.makeText(this, "Can't delete task = ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        mExamPresenter.destroySubscriptions();
        mProgressDialog = null;
        super.onDestroy();
    }

    @Override
    public void deleteTask(int id) {
        mProgressDialog.show();
        mExamPresenter.deleteTask(id);
    }
}
