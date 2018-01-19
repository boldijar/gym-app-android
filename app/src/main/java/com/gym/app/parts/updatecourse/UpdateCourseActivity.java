package com.gym.app.parts.updatecourse;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.gym.app.R;
import com.gym.app.data.model.Course;
import com.gym.app.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author catalinradoiu
 * @since 1/19/2018
 */

public class UpdateCourseActivity extends AppCompatActivity implements UpdateCourseView {

    public static final String COURSE = "course";
    public static final String POSITION = "position";
    public static final String UPDATED = "updated";

    private static final int PICK_IMAGE_REQUEST = 1;

    @BindView(R.id.update_course_image)
    ImageView mCourseImage;

    @BindView(R.id.course_name_layout)
    TextInputLayout mCourseNameLayout;

    @BindView(R.id.course_name_input)
    TextInputEditText mCourseName;

    @BindView(R.id.course_capacity_layout)
    TextInputLayout mCourseCapacityLayout;

    @BindView(R.id.course_capacity_input)
    TextInputEditText mCourseCapacity;

    @BindView(R.id.course_date_layout)
    TextInputLayout mCourseDateLayout;

    @BindView(R.id.course_date_input)
    TextInputEditText mCourseDate;

    private long mCourseDateTimestamp;
    private Snackbar mOperationSnackBar;
    private UpdateCoursePresenter updateCoursePresenter;

    public static Intent getStartIntent(Context context, Course course, int position) {
        return new Intent(context, UpdateCourseActivity.class).putExtra(COURSE, course)
                .putExtra(POSITION, position);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_course);
        ButterKnife.bind(this);
        initPresenter();
        initListeners();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_course, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_course_menu_item) {
            boolean isDataValid = true;
            if (mCourseDateTimestamp * 1000 < System.currentTimeMillis()) {
                mCourseDateLayout.setError(getString(R.string.course_date_not_past));
                isDataValid = false;
            } else {
                mCourseDateLayout.setError("");
            }
            if (mCourseCapacity.getText().toString().isEmpty()) {
                mCourseCapacityLayout.setError(getString(R.string.course_capaciy_invalid));
                isDataValid = false;
            } else {
                try {
                    int capacity = Integer.parseInt(mCourseCapacity.getText().toString());
                    if (capacity < 1 || capacity > 50) {
                        mCourseCapacityLayout.setError(getString(R.string.course_capaciy_invalid));
                        isDataValid = false;
                    } else {
                        mCourseCapacityLayout.setError("");
                    }
                } catch (NumberFormatException e) {
                    mCourseCapacityLayout.setError(getString(R.string.course_capaciy_invalid));
                }
            }
            if (mCourseName.getText().toString().isEmpty() || mCourseName.getText().toString().length() < 3) {
                mCourseNameLayout.setError(getString(R.string.course_name_invalid));
                isDataValid = false;
            } else {
                mCourseNameLayout.setError("");
            }
            if (isDataValid) {
                updateCourse();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        sendResult(false);
        super.onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendResult(false);
    }

    @Override
    public void displayError() {
        if (mOperationSnackBar != null && mOperationSnackBar.isShown()) {
            mOperationSnackBar.dismiss();
        }
        mOperationSnackBar = Snackbar.make(mCourseCapacityLayout, getString(R.string.update_course_error), Snackbar.LENGTH_LONG);
        mOperationSnackBar.setAction(getString(R.string.retry), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCourse();
            }
        });
        mOperationSnackBar.show();
    }

    @Override
    public void displaySuccess() {
        sendResult(true);
        finish();
    }

    private void initPresenter() {
        updateCoursePresenter = new UpdateCoursePresenter(this);
    }

    private void initListeners() {
        mCourseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(Intent.createChooser(new Intent().setType("image/*")
                                .setAction(Intent.ACTION_PICK), getString(R.string.select_image)),
                        PICK_IMAGE_REQUEST);
            }
        });

        mCourseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCourseDate.getText().toString().equals(getString(R.string.tap_to_select_date))) {
                    mCourseDate.setText("");
                }
                final Calendar calendar = Calendar.getInstance();
                int pickerYear = Calendar.getInstance().get(Calendar.YEAR);
                int pickerMonth = Calendar.getInstance().get(Calendar.MONTH);
                int pickerDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(UpdateCourseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                displayCourseTimePicker(calendar);
                            }
                        }, pickerYear, pickerMonth, pickerDay);
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                datePicker.show();
            }
        });
    }

    private void displayCourseTimePicker(final Calendar calendar) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                            if (mOperationSnackBar != null && mOperationSnackBar.isShown()) {
                                mOperationSnackBar.dismiss();
                            }
                            mOperationSnackBar = Snackbar.make(mCourseCapacityLayout,
                                    getString(R.string.course_date_not_past), Snackbar.LENGTH_LONG);
                            mOperationSnackBar.show();
                        } else {
                            mCourseDateTimestamp = calendar.getTimeInMillis() / 1000;
                            mCourseDate.setText(new SimpleDateFormat("dd/MM/yy HH:mm")
                                    .format(new Date(mCourseDateTimestamp * 1000)));
                        }
                    }
                }, 0, 0, true);
        timePickerDialog.show();
    }

    private void updateCourse() {
        updateCoursePresenter.updateCourse(((Course) getIntent().getParcelableExtra(COURSE)).getId(), mCourseName.getText().toString(),
                Integer.parseInt(mCourseCapacity.getText().toString()), mCourseDateTimestamp);
    }

    @SuppressLint("SimpleDateFormat")
    private void initData() {
        Course course = getIntent().getParcelableExtra(COURSE);
        mCourseDateTimestamp = course.getCourseDate();
        mCourseName.setText(course.getName());
        mCourseCapacity.setText(String.valueOf(course.getCapacity()));
        Glide.with(mCourseImage).load(Constants.COURSES_IMAGES_ENDPOINT + course.getImage()).into(mCourseImage);
        mCourseDate.setText(new SimpleDateFormat("dd/MM/yy HH:mm")
                .format(new Date(mCourseDateTimestamp * 1000)));
    }

    private void sendResult(boolean updated) {
        Course course = getIntent().getParcelableExtra(COURSE);
        course.setCourseDate(mCourseDateTimestamp);
        course.setName(mCourseName.getText().toString());
        course.setCapacity(Integer.parseInt(mCourseCapacity.getText().toString()));
        setResult(RESULT_OK, new Intent().putExtra(POSITION, getIntent().getIntExtra(POSITION, -1))
                .putExtra(COURSE, course).putExtra(UPDATED, updated));
    }
}
