package com.gym.app.parts.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gym.app.R;
import com.gym.app.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author catalinradoiu
 * @since 1/18/2018
 */

public class GalleryPhotosAdapter extends RecyclerView.Adapter<GalleryPhotosAdapter.PhotoViewHolder> {

    private List<String> mPhotosList;

    public GalleryPhotosAdapter() {
        mPhotosList = new ArrayList<>();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        holder.bind(mPhotosList.get(position));
    }

    @Override
    public int getItemCount() {
        return mPhotosList.size();
    }

    public void setPhotos(List<String> photosList) {
        this.mPhotosList = photosList;
        notifyDataSetChanged();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.gallery_image)
        ImageView mGalleryImage;

        PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(String imageSource) {
            Glide.with(mGalleryImage.getContext())
                    .load(Constants.GYM_PHOTOS_ENDPOINT + imageSource)
                    .into(mGalleryImage);
        }
    }
}
