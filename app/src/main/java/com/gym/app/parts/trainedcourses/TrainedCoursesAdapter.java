package com.gym.app.parts.trainedcourses;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gym.app.R;
import com.gym.app.data.model.Course;
import com.gym.app.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author catalinradoiu
 * @since 1/19/2018
 */

public class TrainedCoursesAdapter extends RecyclerView.Adapter<TrainedCoursesAdapter.TrainedCourseViewHolder> {

    private List<Course> mCoursesList;
    private OnEditClickListener mOnEditClickListener;
    private OnDeleteClickListener mOnDeleteClickListener;

    interface OnEditClickListener {
        void onEditClick(int position);
    }

    interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public TrainedCoursesAdapter() {
        mCoursesList = new ArrayList<>();
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.mOnDeleteClickListener = onDeleteClickListener;
    }

    public void setOnEditClickListener(OnEditClickListener onEditClickListener) {
        this.mOnEditClickListener = onEditClickListener;
    }

    public void setCourses(List<Course> courseList) {
        this.mCoursesList = courseList;
        notifyDataSetChanged();
    }

    @Override
    public TrainedCourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrainedCourseViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trained_course, parent, false),
                mOnDeleteClickListener, mOnEditClickListener);
    }

    @Override
    public void onBindViewHolder(TrainedCourseViewHolder holder, int position) {
        holder.bind(mCoursesList.get(position));
    }

    @Override
    public int getItemCount() {
        return mCoursesList.size();
    }

    public void deleteCourse(int position) {
        mCoursesList.remove(position);
        notifyItemRemoved(position);
    }

    public void updateCouse(int position, Course course) {
        mCoursesList.remove(position);
        mCoursesList.add(position, course);
        notifyItemChanged(position);
    }

    public Course getCourse(int position) {
        return mCoursesList.get(position);
    }

    static class TrainedCourseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.trained_course_image)
        ImageView mCourseImage;

        @BindView(R.id.trained_course_name)
        TextView mCourseName;

        @BindView(R.id.trained_course_time)
        TextView mCourseTime;

        private OnDeleteClickListener mOnDeteleClickListener;
        private OnEditClickListener mOnEditClickListener;

        public TrainedCourseViewHolder(View itemView, OnDeleteClickListener onDeleteClickListener,
                                       OnEditClickListener onEditClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mOnDeteleClickListener = onDeleteClickListener;
            this.mOnEditClickListener = onEditClickListener;
        }

        @SuppressLint("SimpleDateFormat")
        void bind(Course course) {
            mCourseName.setText(course.getName());
            Glide.with(mCourseImage.getContext()).load(Constants.COURSES_IMAGES_ENDPOINT + course.getImage())
                    .into(mCourseImage);
            mCourseTime.setText(new SimpleDateFormat("dd/MM/yy  hh:MM").format(new Date(course.getCourseDate() * 1000)));
        }

        @OnClick(R.id.trained_course_delete_button)
        void onDeleteClick() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && mOnDeteleClickListener != null) {
                mOnDeteleClickListener.onDeleteClick(position);
            }
        }

        @OnClick(R.id.trained_course_update_button)
        void onEditClick() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && mOnEditClickListener != null) {
                mOnEditClickListener.onEditClick(position);
            }
        }
    }
}
