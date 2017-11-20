package com.gym.app.parts.mycourses;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author catalinradoiu
 * @since 2017.11.18
 */

class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseViewHolder> {

    private List<Course> mCourseList;
    private OnRemoveCourseListener mOnRemoveCourseListener;

    interface OnRemoveCourseListener {
        void onClick(int position);
    }

    CoursesAdapter() {
        mCourseList = new ArrayList<>();
    }

    public void setCourses(List<Course> courses) {
        this.mCourseList = courses;
        notifyDataSetChanged();
    }

    Course getCourse(int position) {
        return mCourseList.get(position);
    }

    void removeCourse(int position) {
        mCourseList.remove(position);
        notifyItemRemoved(position);
    }

    void setRemoveCourseListener(OnRemoveCourseListener onRemoveCourseListener) {
        this.mOnRemoveCourseListener = onRemoveCourseListener;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CourseViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_my_course, parent, false), mOnRemoveCourseListener);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        holder.bind(mCourseList.get(position));
    }

    @Override
    public int getItemCount() {
        return mCourseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.my_course_image)
        ImageView mCourseImage;

        @BindView(R.id.course_name)
        TextView mCourseName;

        @BindView(R.id.course_capacity)
        TextView mCourseCapacity;

        @BindView(R.id.course_schedule)
        TextView mCourseSchedule;

        @BindView(R.id.course_trainer)
        TextView mCourseTrainer;

        private OnRemoveCourseListener mOnRemoveCourseListener;

        CourseViewHolder(View itemView, OnRemoveCourseListener onRemoveCourseListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mOnRemoveCourseListener = onRemoveCourseListener;
        }

        public void bind(Course course) {
            Glide.with(mCourseImage.getContext())
                    .load(Constants.COURSES_IMAGES_ENDPOINT + course.getImage())
                    .into(mCourseImage);
            mCourseName.setText(course.getName());
            mCourseTrainer.setText(mCourseTrainer.getContext().getString(R.string.course_held_by,
                    course.getTrainer().getFullName()));
            mCourseSchedule.setText(com.gym.app.utils.TimeUtils.formatToDate(course.getCourseDate()));
            mCourseCapacity.setText(mCourseCapacity.getContext().getString(R.string.people_per_session,
                    course.getCapacity()));
        }

        @OnClick(R.id.remove_course_button)
        void onRemoveCourseButtonClick() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && mOnRemoveCourseListener != null) {
                mOnRemoveCourseListener.onClick(position);
            }
        }
    }
}
