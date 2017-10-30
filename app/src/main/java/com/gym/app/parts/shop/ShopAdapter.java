package com.gym.app.parts.shop;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gym.app.R;
import com.gym.app.data.model.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Paul
 * @since 2017.10.25
 */

class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    private List<Product> mItems = new ArrayList<>();

    public void setItems(List<Product> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopViewHolder holder, int position) {
        Product product = mItems.get(position);
        Glide.with(holder.mImage.getContext()).load(product.mImage).into(holder.mImage);
        holder.mPrice.setText(product.mPrice);
        holder.mTitle.setText(product.mName);
        holder.mDescription.setText(product.mDescription);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ShopViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.shop_description)
        TextView mDescription;
        @BindView(R.id.shop_image)
        ImageView mImage;
        @BindView(R.id.shop_price)
        TextView mPrice;
        @BindView(R.id.shop_title)
        TextView mTitle;

        ShopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
