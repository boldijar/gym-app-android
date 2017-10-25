package com.gym.app.parts.profile;

import com.gym.app.R;
import com.gym.app.parts.home.BaseHomeFragment;

/**
 * @author Paul
 * @since 2017.10.25
 */

public class ProfileFragment extends BaseHomeFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected int getTitle() {
        return R.string.profile;
    }
}
