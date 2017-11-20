package com.gym.app.parts.findcourses.day_courses;

import android.annotation.SuppressLint;
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
import com.gym.app.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

class DayCoursesAdapter extends RecyclerView.Adapter<DayCoursesAdapter.DayCoursesViewHolder> {

    private List<Course> mTodayCourses;
    private DayCourseClickListener mDayCourseClickListener;

    interface DayCourseClickListener {
        void onClick(int position);
    }

    DayCoursesAdapter() {
        this.mTodayCourses = new ArrayList<>();
    }

    void setCourses(List<Course> courses) {
        this.mTodayCourses = courses;
        notifyDataSetChanged();
    }

    void setDayCourseClickListener(DayCourseClickListener dayCourseClickListener) {
        this.mDayCourseClickListener = dayCourseClickListener;
    }

    /**
     * Update the course from the given position
     *
     * @param position the position of the course that was changed
     */
    void updateCourse(int position) {
        notifyItemChanged(position);
    }

    Course getCourse(int position) {
        return mTodayCourses.get(position);
    }


    @Override
    public DayCoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DayCoursesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_course_view_holder, parent, false),
                mDayCourseClickListener);
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

        private static final long ONE_HOUR_TIME_STAMP = 3600 * 1000;

        private static final int CLICK_INTERVAL = 800;

        private long lastClickTime;

        @BindView(R.id.course_image)
        ImageView mCourseImage;

        @BindView(R.id.course_name)
        TextView mCourseName;

        @BindView(R.id.course_schedule)
        TextView mCourseSchedule;

        @BindView(R.id.course_remaining_places)
        TextView mCourseRemainingPlaces;

        @BindView(R.id.handle_course_button)
        Button mHandleCourseButton;

        private DayCourseClickListener mDayCourseClickListener;

        DayCoursesViewHolder(View itemView, DayCourseClickListener dayCourseClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mDayCourseClickListener = dayCourseClickListener;
        }

        public void bind(Course course) {
            int buttonColor;
            String buttonText;
            String remainingPlaces = (course.getCapacity() - course.getRegisteredUsersNumber() == 0) ?
                    mCourseRemainingPlaces.getContext().getString(R.string.course_no_places_left) :
                    mCourseRemainingPlaces.getContext()
                            .getString(R.string.course_places_left,
                                    course.getCapacity() - course.getRegisteredUsersNumber());
            mCourseRemainingPlaces.setText(remainingPlaces);
            Glide.with(mCourseImage.getContext()).load(Constants.COURSES_IMAGES_ENDPOINT +
                    course.getImage()).into(mCourseImage);
            mCourseName.setText(course.getName());
            mCourseSchedule.setText(getFormattedTime(course.getCourseDate() * 1000,
                    course.getCapacity() * 1000 + ONE_HOUR_TIME_STAMP));
            if (!course.isRegistered()) {
                buttonText = mHandleCourseButton.getContext().getString(R.string.reserve_course);
                if (course.getCapacity() - course.getRegisteredUsersNumber() == 0) {
                    mHandleCourseButton.setClickable(false);
                    buttonColor = ContextCompat.getColor(mHandleCourseButton.getContext(),
                            R.color.transparent_blue);
                } else {
                    buttonColor = ContextCompat.getColor(mHandleCourseButton.getContext(),
                            R.color.light_blue);
                }
            } else {
                buttonText = mHandleCourseButton.getContext().getString(R.string.remove_course);
                buttonColor = ContextCompat.getColor(mHandleCourseButton.getContext(),
                        R.color.light_red);
            }
            mHandleCourseButton.setTextColor(buttonColor);
            mHandleCourseButton.setText(buttonText);
        }

        @OnClick(R.id.handle_course_button)
        void handleRegisterClick() {
            if (System.currentTimeMillis() - lastClickTime > CLICK_INTERVAL) {
                int position = getAdapterPosition();
                if (position != NO_POSITION && mDayCourseClickListener != null) {
                    lastClickTime = System.currentTimeMillis();
                    mDayCourseClickListener.onClick(position);
                }
            }
        }

        private String getFormattedTime(long timestamp, long duration) {
            @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("HH:mm");
            Date startTime = new Date(timestamp);
            Date endTime = new Date(timestamp + duration);
            return format.format(startTime) + "-" +
                    format.format(endTime);

        }
    }
}
