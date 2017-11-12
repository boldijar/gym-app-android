package com.gym.app.parts.shop;

import com.gym.app.data.model.Product;

import java.util.List;

/**
 * @author Paul
 * @since 2017.10.29
 */

public interface ShopView {

    void showProducts(List<Product> productList);

    void showError(Throwable throwable);

    void showLoading();
}
