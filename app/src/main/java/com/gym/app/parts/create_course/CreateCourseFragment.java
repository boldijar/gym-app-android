package com.gym.app.parts.create_course;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.gym.app.R;
import com.gym.app.parts.home.BaseHomeFragment;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

/**
 * @author catalinradoiu
 * @since 2018.01.09
 */
public class CreateCourseFragment extends BaseHomeFragment implements CreateCourseView {

    @BindView(R.id.course_image)
    ImageView courseImage;

    @BindView(R.id.course_name_layout)
    TextInputLayout courseNameLayout;

    @BindView(R.id.course_name_input)
    TextInputEditText courseName;

    @BindView(R.id.course_capacity_layout)
    TextInputLayout courseCapacityLayout;

    @BindView(R.id.course_capacity_input)
    TextInputEditText courseCapacity;

    @BindView(R.id.course_date_layout)
    TextInputLayout courseDateLayout;

    @BindView(R.id.course_date_input)
    TextInputEditText courseDate;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int READ_EXTERNAL_STORAGE_REQUEST = 2;

    private CreateCoursePresenter createCoursePresenter;
    private long courseDateTimestamp;
    private Uri uploadedImage;

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
            // todo : validations
            handlePermissionUpload();
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
                courseImage.setImageURI(image);
                uploadedImage = image;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                File courseImage = new Compressor(getContext()).compressToFile(new File(getPath(uploadedImage)));
                createCoursePresenter.createCourse(courseName.getText().toString(),
                        courseDateTimestamp, 1515758400, courseImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        createCoursePresenter = new CreateCoursePresenter(this);
    }


    private void initListeners() {
        courseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(Intent.createChooser(new Intent().setType("image/*")
                                .setAction(Intent.ACTION_PICK), getString(R.string.select_image)),
                        PICK_IMAGE_REQUEST);
            }
        });
    }

    private String getPath(Uri uri) {
        String result;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void handlePermissionUpload() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST);
        } else {
            try {
                File courseImage = new Compressor(getContext()).compressToFile(new File(getPath(uploadedImage)));
                createCoursePresenter.createCourse(courseName.getText().toString(),
                        courseDateTimestamp, 1515758400, courseImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
