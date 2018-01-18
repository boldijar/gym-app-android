package com.gym.app.parts.gallery;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gym.app.R;
import com.gym.app.parts.home.BaseHomeFragment;
import com.gym.app.view.EmptyLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author catalinradoiu
 * @since 2018.01.18
 */

public class GalleryFragment extends BaseHomeFragment implements GalleryView {

    @BindView(R.id.gallery_recycler)
    RecyclerView mGalleryRecycler;

    @BindView(R.id.gallery_empty_layout)
    EmptyLayout mEmptyLayout;

    private GalleryPresenter mGalleryPresenter;
    private GalleryPhotosAdapter mGalleryPhotosAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mEmptyLayout.setState(EmptyLayout.State.LOADING);
        initPresenter();
        initAdapter();
        initStateLayout();
        mGalleryPresenter.loadPhotos();
    }

    @Override
    public void setPhotos(List<String> photos) {
        if (photos.size() > 0) {
            mEmptyLayout.setState(EmptyLayout.State.CLEAR);
            mGalleryPhotosAdapter.setPhotos(photos);
        } else {
            mEmptyLayout.setState(EmptyLayout.State.EMPTY_NO_BUTTON, R.string.no_photos_in_gallery);
        }
    }

    @Override
    public void displayError() {
        mEmptyLayout.setState(EmptyLayout.State.EMPTY, R.string.could_not_load_gallery);
    }

    @Override
    protected int getTitle() {
        return R.string.gallery;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gallery;
    }

    private void initPresenter() {
        mGalleryPresenter = new GalleryPresenter(this);
    }

    private void initAdapter() {
        mGalleryPhotosAdapter = new GalleryPhotosAdapter();
        mGalleryRecycler.setAdapter(mGalleryPhotosAdapter);
        mGalleryRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mGalleryRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.small_margin);
            }
        });
    }

    private void initStateLayout() {
        mEmptyLayout.setOnRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGalleryPresenter.loadPhotos();
            }
        });
    }
}
