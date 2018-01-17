package com.gym.app.parts.create_course;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gym.app.R;
import com.gym.app.parts.home.BaseHomeFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

/**
 * @author catalinradoiu
 * @since 2018.01.09
 */
public class CreateCourseFragment extends BaseHomeFragment implements CreateCourseView {

    @BindView(R.id.course_image)
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

    @BindView(R.id.image_upload_error)
    TextView mUploadImageError;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int READ_EXTERNAL_STORAGE_REQUEST = 2;

    private CreateCoursePresenter mCreateCoursePresenter;
    private long mCourseDateTimestamp;
    private Uri mUploadedImage;
    private Snackbar mOperationSnackBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initPresenter();
        initListeners();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_save_course, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_course_menu_item) {
            boolean isDataValid = true;
            if (mUploadedImage == null) {
                mUploadImageError.setVisibility(View.VISIBLE);
                isDataValid = false;
            } else {
                mUploadImageError.setVisibility(View.INVISIBLE);
            }
            if (mCourseDateTimestamp == 0) {
                mCourseDateLayout.setError(getString(R.string.no_date_selected));
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
                handlePermissionUpload();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri image = data.getData();
            if (image != null) {
                mCourseImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mCourseImage.setImageBitmap(rotatePhoto(getPath(image)));
                mUploadedImage = image;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createCourse();
        }
    }

    @Override
    public void displayError() {
        if (mOperationSnackBar != null && mOperationSnackBar.isShown()) {
            mOperationSnackBar.dismiss();
        }
        if (getView() != null) {
            mOperationSnackBar = Snackbar.make(getView(), getString(R.string.create_course_error), Snackbar.LENGTH_LONG);
            mOperationSnackBar.setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createCourse();
                }
            });
            mOperationSnackBar.show();
        }
    }

    @Override
    public void displaySuccess() {
        clearFields();
        if (mOperationSnackBar != null && mOperationSnackBar.isShown()) {
            mOperationSnackBar.dismiss();
        }
        if (getView() != null) {
            mOperationSnackBar = Snackbar.make(getView(), getString(R.string.course_create_success), Snackbar.LENGTH_LONG);
            mOperationSnackBar.show();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_create_course;
    }

    @Override
    protected int getTitle() {
        return R.string.create_course;
    }

    private void initPresenter() {
        mCreateCoursePresenter = new CreateCoursePresenter(this);
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
                DatePickerDialog datePicker = new DatePickerDialog(getContext(),
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

    private String getPath(Uri uri) {
        String result = "";
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri,
                projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(columnIndex);
            cursor.close();
        }
        return result;
    }

    private void displayCourseTimePicker(final Calendar calendar) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
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
                            if (getView() != null) {
                                mOperationSnackBar = Snackbar.make(getView(),
                                        getString(R.string.course_date_not_past), Snackbar.LENGTH_LONG);
                                mOperationSnackBar.show();
                            }
                        } else {
                            mCourseDateTimestamp = calendar.getTimeInMillis();
                            mCourseDate.setText(new SimpleDateFormat("DD/MM/YY HH:mm")
                                    .format(new Date(mCourseDateTimestamp)));
                        }
                    }
                }, 0, 0, true);
        timePickerDialog.show();
    }

    private void handlePermissionUpload() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST);
        } else {
            createCourse();
        }
    }

    private void createCourse() {
        File courseImage = null;
        try {
            courseImage = new Compressor(getContext()).compressToFile(new File(getPath(mUploadedImage)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCreateCoursePresenter.createCourse(mCourseName.getText().toString(),
                Integer.parseInt(mCourseCapacity.getText().toString()), mCourseDateTimestamp / 1000, courseImage);
    }

    private void clearFields() {
        mCourseImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mCourseName.setText("");
        mCourseCapacity.setText("");
        mCourseDate.setText(getString(R.string.tap_to_select_date));
        mCourseImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_a_photo));
    }

    private Bitmap rotatePhoto(String photoPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap rotatedBitmap;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;
            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }
}
