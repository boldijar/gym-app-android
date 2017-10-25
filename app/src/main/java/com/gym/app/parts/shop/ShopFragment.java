package com.gym.app.parts.shop;

import com.gym.app.R;
import com.gym.app.parts.home.BaseHomeFragment;

/**
 * @author Paul
 * @since 2017.10.25
 */

public class ShopFragment extends BaseHomeFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shop;
    }

    @Override
    protected int getTitle() {
        return R.string.shop;
    }
}
