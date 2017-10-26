package com.gym.app.parts.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.gym.app.R;
import com.gym.app.parts.home.BaseHomeFragment;
import com.gym.app.utils.SpaceItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Paul
 * @since 2017.10.25
 */

public class ShopFragment extends BaseHomeFragment {

    @BindView(R.id.shop_recycler)
    RecyclerView mRecyclerView;
    private ShopAdapter mShopAdapter = new ShopAdapter();


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        loadRecycler();
    }

    private void loadRecycler() {
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mShopAdapter);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shop;
    }

    @Override
    protected int getTitle() {
        return R.string.shop;
    }
}
