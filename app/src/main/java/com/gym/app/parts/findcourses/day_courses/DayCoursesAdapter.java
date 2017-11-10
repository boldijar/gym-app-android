package com.gym.app.parts.findcourses.day_courses;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gym.app.R;
import com.gym.app.data.model.Course;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * Adapter for the courses from a certain day
 *
 * @author catalinradoiu
 * @since 2017.01.01
 */

public class DayCoursesAdapter extends RecyclerView.Adapter<DayCoursesAdapter.DayCoursesViewHolder> {

    private List<Course> mTodayCourses;

    private OnRegisterClickListener mOnRegisterClickListener;

    interface OnRegisterClickListener {
        void onClick(int position);
    }

    public DayCoursesAdapter() {
        this.mTodayCourses = new ArrayList<>();
    }

    public void setCourses(List<Course> courses) {
        this.mTodayCourses = courses;
        notifyDataSetChanged();
    }

    public void decreaseCourseCapacity(int courseId){
        int position = 0;
        for (Course course : mTodayCourses){
            if (course.getmId() == courseId){
                course.setmCapacity(course.getCapacity() - 1);
                notifyItemChanged(position);
                break;
            }
            position++;
        }
    }

    public Course getCourse(int position){
        return mTodayCourses.get(position);
    }

    public void setOnRegisterClickListener(OnRegisterClickListener mOnRegisterClickListener) {
        this.mOnRegisterClickListener = mOnRegisterClickListener;
    }

    @Override
    public DayCoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DayCoursesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_course_view_holder, parent, false), mOnRegisterClickListener);
    }

    @Override
    public void onBindViewHolder(DayCoursesViewHolder holder, int position) {
        holder.bind(mTodayCourses.get(position));
    }

    @Override
    public int getItemCount() {
        return mTodayCourses.size();
    }

    static class DayCoursesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.course_image)
        ImageView courseImage;

        @BindView(R.id.course_name)
        TextView courseName;

        @BindView(R.id.course_schedule)
        TextView courseSchedule;

        @BindView(R.id.course_remaining_places)
        TextView courseRemainingPlaces;

        @BindView(R.id.reserve_course_button)
        Button reserveCourseButton;

        private OnRegisterClickListener onRegisterClickListener;

        public DayCoursesViewHolder(View itemView, OnRegisterClickListener onRegisterClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onRegisterClickListener = onRegisterClickListener;
        }

        public void bind(Course course) {
            Glide.with(courseImage.getContext()).load(course.getImage()).into(courseImage);
            courseName.setText(course.getName());
            courseSchedule.setText("12:00-13:00");
            courseRemainingPlaces.setText(
                    String.valueOf(course.getCapacity() - course.getRegisteredUsersNumber()));
            if (course.getCapacity() - course.getRegisteredUsersNumber() == 0){
                reserveCourseButton.setClickable(false);
                reserveCourseButton.setTextColor(ContextCompat.getColor(
                        reserveCourseButton.getContext(),
                        R.color.transparent_blue
                ));
            }
        }

        @OnClick(R.id.reserve_course_button)
        public void handleRegisterClick() {
            int position = getAdapterPosition();
            if (position != NO_POSITION && onRegisterClickListener != null) {
                onRegisterClickListener.onClick(position);
            }
        }
    }
}
