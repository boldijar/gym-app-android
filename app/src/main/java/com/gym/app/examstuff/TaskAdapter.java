package com.gym.app.examstuff;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gym.app.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.01.23
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holder> {

    private final List<Task> mTaskList = new ArrayList<>();
    private final Listener mListener;

    public TaskAdapter(Listener listener) {
        mListener = listener;
    }

    public void addTasks(List<Task> tasks) {
        mTaskList.addAll(tasks);
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Task task = mTaskList.get(position);
        holder.mText1.setText(task.mId + "");
        holder.mText2.setText(task.mText);
        holder.mText3.setText(task.mDate);
        holder.itemView.setOnClickListener(view -> mListener.deleteTask(task.mId));
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    public void deleteTask(int id) {
        for (int i = 0; i < mTaskList.size(); i++) {
            Task task = mTaskList.get(i);
            if (task.mId == id) {
                mTaskList.remove(i);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void clear() {
        mTaskList.clear();
        notifyDataSetChanged();
    }

    public interface Listener {
        void deleteTask(int id);
    }

    public static class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.text1)
        TextView mText1;
        @BindView(R.id.text2)
        TextView mText2;
        @BindView(R.id.text3)
        TextView mText3;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
