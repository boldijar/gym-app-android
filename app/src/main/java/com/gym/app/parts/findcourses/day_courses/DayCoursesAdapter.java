package com.gym.app.parts.findcourses.day_courses;

import android.content.Context;
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

/**
 * Adapter for the courses from a certain day
 *
 * @author catalinradoiu
 * @since 2017.01.01
 */

public class DayCoursesAdapter extends RecyclerView.Adapter<DayCoursesAdapter.DayCoursesViewHolder> {

    private List<Course> mTodayCourses;

    public DayCoursesAdapter() {
        this.mTodayCourses = new ArrayList<>();
    }

    public void setCourses(List<Course> courses) {
        this.mTodayCourses = courses;
        notifyDataSetChanged();
    }

    @Override
    public DayCoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DayCoursesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_course_view_holder, parent, false));
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

        public DayCoursesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Course course) {
            Glide.with(courseImage.getContext()).load(course.getTrainer().getPictureUrl()).into(courseImage);
            courseName.setText(course.getTrainer().getFullName());
            courseSchedule.setText("12:00-13:00");
            courseRemainingPlaces.setText(
                    String.valueOf(course.getCapacity() - course.getRegisteredUsersNumber()));
        }
    }
}
