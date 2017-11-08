package com.gym.app.parts.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.gym.app.R;
import com.gym.app.data.model.Product;
import com.gym.app.parts.home.BaseHomeFragment;
import com.gym.app.utils.SpaceItemDecoration;
import com.gym.app.view.EmptyLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Paul
 * @since 2017.10.25
 */

public class ShopFragment extends BaseHomeFragment implements ShopView {

    @BindView(R.id.shop_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.shop_empty_layout)
    EmptyLayout mEmptyLayout;

    private ShopAdapter mShopAdapter = new ShopAdapter();
    private ShopPresenter mShopPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        loadRecycler();
        mShopPresenter = new ShopPresenter(this);
        mShopPresenter.loadProducts();
        mEmptyLayout.setOnRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShopPresenter.loadProducts();
            }
        });
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

    @Override
    public void showProducts(List<Product> productList) {
        mEmptyLayout.setState(EmptyLayout.State.CLEAR);
        mShopAdapter.setItems(productList);
    }

    @Override
    public void showError(Throwable throwable) {
        mEmptyLayout.setState(EmptyLayout.State.ERROR, R.string.products_error);
    }

    @Override
    public void showLoading() {
        mEmptyLayout.setState(EmptyLayout.State.LOADING);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mShopPresenter.destroySubscriptions();
    }
}
