package com.gym.app.parts.profile;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.gym.app.R;
import com.gym.app.parts.home.BaseHomeFragment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.app.Activity.RESULT_OK;

/**
 * @author Paul
 * @since 2017.10.25
 */

public class ProfileFragment extends BaseHomeFragment {

    @BindView(R.id.profile_image)
    CircleImageView mProfileImage;
    private File mPhotoFile = null;
    private PopupMenu mUploadPhotoMenu;
    private static final int MY_REQUEST_CAMERA = 10;
    private static final int MY_REQUEST_WRITE_CAMERA = 11;
    private static final int CAPTURE_CAMERA = 12;
    private static final int MY_REQUEST_READ_GALLERY = 13;
    private static final int MY_REQUEST_WRITE_GALLERY = 14;
    private static final int MY_REQUEST_GALLERY = 15;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setUploadPhotoMenu();
    }

    private void setUploadPhotoMenu() {
        mUploadPhotoMenu = new PopupMenu(getContext(), mProfileImage);
        mUploadPhotoMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_take_photo:
                        checkPermissionCameraWrite();
                        return true;
                    case R.id.item_open_gallery:
                        checkPermissionReadExternalStorage();
                        return true;
                }
                return false;
            }
        });
        mUploadPhotoMenu.inflate(R.menu.menu_upload_photo);
    }

    @OnClick(R.id.profile_image)
    void clickedImage(){
        mUploadPhotoMenu.show();
    }

    private void checkPermissionReadExternalStorage(){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_READ_GALLERY);
        } else {
            checkPermissionWriteExternalStorage();
        }
    }
    private void checkPermissionWriteExternalStorage(){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_WRITE_GALLERY);
        } else {
            getPhotos();
        }
    }

    private void checkPermissionCamera(){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_REQUEST_CAMERA);
        } else {
            catchPhoto();
        }
    }
    private void checkPermissionCameraWrite(){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_WRITE_CAMERA);
        } else {
            checkPermissionCamera();
        }
    }

    public void onRequestPermissionsResult (int requestCode, String[] permissions,  int[] grantResults)
    {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case MY_REQUEST_CAMERA:
                    catchPhoto();
                    break;
                case MY_REQUEST_WRITE_CAMERA:
                    checkPermissionCamera();
                    break;
                case MY_REQUEST_READ_GALLERY:
                    checkPermissionWriteExternalStorage();
                    break;
                case MY_REQUEST_WRITE_GALLERY:
                    getPhotos();
                    break;
            }
        }
    }

    private void catchPhoto() {
        mPhotoFile = getFile();
        if(mPhotoFile !=null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                Uri photoURI = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".com.gym.app.provider", mPhotoFile);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, CAPTURE_CAMERA);
            } catch (ActivityNotFoundException e) {
            }
        } else {
            Toast.makeText(getActivity(), R.string.check_sdcard_status, Toast.LENGTH_SHORT).show();
        }
    }

    private void getPhotos() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, MY_REQUEST_GALLERY);
    }

    public File getFile(){
        File fileDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getContext().getPackageName()
                + "/Files");
        if (!fileDir.exists()){
            if (!fileDir.mkdirs()){
                return null;
            }
        }
        File mediaFile = new File(fileDir.getPath() + File.separator + System.currentTimeMillis()+"temp.jpg");
        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK){
            return;
        }

        switch (requestCode) {
            case CAPTURE_CAMERA:
                mProfileImage.setImageURI(Uri.parse(getString(R.string.file_path) + mPhotoFile));
                mPhotoFile = null;
                break;
            case MY_REQUEST_GALLERY:
                try {
                    InputStream inputStream = getActivity().getApplicationContext().getContentResolver().openInputStream(data.getData());
                    mPhotoFile = getFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(mPhotoFile);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                    fileOutputStream.close();
                    inputStream.close();
                    mProfileImage.setImageURI(Uri.parse(getString(R.string.file_path) + mPhotoFile));

                } catch (Exception e) {
                }
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected int getTitle() {
        return R.string.profile;
    }
}
