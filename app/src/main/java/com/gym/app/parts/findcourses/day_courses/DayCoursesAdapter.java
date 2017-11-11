package com.gym.app.parts.findcourses.day_courses;

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

class DayCoursesAdapter extends RecyclerView.Adapter<DayCoursesAdapter.DayCoursesViewHolder> {

    private static final int RESERVE_COURSE_VIEW = 1;
    private static final int REMOVE_COURSE_VIEW = 2;

    private List<Course> mTodayCourses;

    private OnRegisterClickListener mOnRegisterClickListener;

    private OnRemoveClickListener mOnRemoveClickListener;

    interface OnRegisterClickListener {
        void onClick(int position);
    }

    interface OnRemoveClickListener {
        void onClick(int position);
    }

    DayCoursesAdapter() {
        this.mTodayCourses = new ArrayList<>();
    }

    void setCourses(List<Course> courses) {
        this.mTodayCourses = courses;
        notifyDataSetChanged();
    }

    /**
     * Changes the course status for the current user
     *
     * @param courseId the id of the course to be changed
     * @param status   the new status of the course ( has to be 1 if the user subscribed to the course,
     *                 or -1 if the user removed the course)
     */
    void changeCourseStatus(int courseId, int status) {
        int position = 0;
        for (Course course : mTodayCourses) {
            if (course.getId() == courseId) {
                if (status < 0) {
                    course.setIsRegistered(false);
                } else {
                    course.setIsRegistered(true);
                }
                course.setRegisteredUsersNumber(course.getRegisteredUsersNumber() + status);
                notifyItemChanged(position);
                break;
            }
            position++;
        }
    }

    Course getCourse(int position) {
        return mTodayCourses.get(position);
    }

    void setOnRegisterClickListener(OnRegisterClickListener mOnRegisterClickListener) {
        this.mOnRegisterClickListener = mOnRegisterClickListener;
    }

    public void setOnRemoveClickListener(OnRemoveClickListener mOnRemoveClickListener) {
        this.mOnRemoveClickListener = mOnRemoveClickListener;
    }

    @Override
    public DayCoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OnRegisterClickListener onRegisterClickListener = null;
        OnRemoveClickListener onRemoveClickListener = null;
        if (viewType == REMOVE_COURSE_VIEW) {
            onRemoveClickListener = mOnRemoveClickListener;
        } else if (viewType == RESERVE_COURSE_VIEW) {
            onRegisterClickListener = mOnRegisterClickListener;
        }
        return new DayCoursesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_course_view_holder, parent, false),
                onRegisterClickListener,
                onRemoveClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        return (mTodayCourses.get(position).isRegistered()) ? REMOVE_COURSE_VIEW : RESERVE_COURSE_VIEW;
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
        ImageView mCourseImage;

        @BindView(R.id.course_name)
        TextView mCourseName;

        @BindView(R.id.course_schedule)
        TextView mCourseSchedule;

        @BindView(R.id.course_remaining_places)
        TextView mCourseRemainingPlaces;

        @BindView(R.id.handle_course_button)
        Button mHandleCourseButton;

        private OnRegisterClickListener mOnRegisterClickListener;

        private OnRemoveClickListener mOnRemoveClickListener;

        DayCoursesViewHolder(View itemView, OnRegisterClickListener onRegisterClickListener,
                             OnRemoveClickListener onRemoveClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mOnRegisterClickListener = onRegisterClickListener;
            this.mOnRemoveClickListener = onRemoveClickListener;
        }

        public void bind(Course course) {
            Glide.with(mCourseImage.getContext()).load(course.getImage()).into(mCourseImage);
            mCourseName.setText(course.getName());
            mCourseSchedule.setText("12:00-13:00");
            mCourseRemainingPlaces.setText(
                    String.valueOf(course.getCapacity() - course.getRegisteredUsersNumber()));
            if (!course.isRegistered()) {
                mHandleCourseButton.setText(mHandleCourseButton.getContext()
                        .getString(R.string.reserve_course));
                if (course.getCapacity() - course.getRegisteredUsersNumber() == 0) {
                    mHandleCourseButton.setClickable(false);
                    mHandleCourseButton.setTextColor(ContextCompat.getColor(
                            mHandleCourseButton.getContext(),
                            R.color.transparent_blue
                    ));
                }
            } else {
                mHandleCourseButton.setText(mHandleCourseButton.getContext()
                        .getString(R.string.remove_course));
            }
        }

        @OnClick(R.id.handle_course_button)
        void handleRegisterClick() {
            int position = getAdapterPosition();
            if (position != NO_POSITION) {
                if (mOnRemoveClickListener != null) {
                    mOnRemoveClickListener.onClick(position);
                } else if (mOnRegisterClickListener != null) {
                    mOnRegisterClickListener.onClick(position);
                }
            }
        }
    }
}
