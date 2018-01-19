package com.gym.app.parts.trainedcourses;

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

/**
 * @author catalinradoiu
 * @since 1/19/2018
 */

public class TrainedCoursesAdapter extends RecyclerView.Adapter<TrainedCoursesAdapter.TrainedCourseViewHolder> {

    private List<Course> mCoursesList;

    public TrainedCoursesAdapter() {
        mCoursesList = new ArrayList<>();
    }

    public void setCourses(List<Course> courseList) {
        this.mCoursesList = courseList;
        notifyDataSetChanged();
    }

    @Override
    public TrainedCourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrainedCourseViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trained_course, parent, false));
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

    static class TrainedCourseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.trained_course_image)
        ImageView mCourseImage;

        @BindView(R.id.trained_course_name)
        TextView mCourseName;

        @BindView(R.id.trained_course_time)
        TextView mCourseTime;

        public TrainedCourseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Course course) {
            mCourseName.setText(course.getName());
            Glide.with(mCourseImage.getContext()).load(Constants.COURSES_IMAGES_ENDPOINT + course.getImage())
                    .into(mCourseImage);
        }
    }
}
