package com.gym.app.parts.authentication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gym.app.parts.authentication.register.RegisterFragment;


/**
 * Pager adapter for navigating between the fragment from AuthenticationActivity
 *
 * @author catalinradoiu
 * @since 2017.10.25
 */
class AuthenticationPagerAdapter extends FragmentPagerAdapter {

    static final int LOGIN_FRAGMENT = 0;
    static final int REGISTER_FRAGMENT = 1;
    private static final int FRAGMENT_COUNT = 2;

    AuthenticationPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case LOGIN_FRAGMENT:
                return new LoginFragment();
            case REGISTER_FRAGMENT:
                return new RegisterFragment();
        }
        return new RegisterFragment();
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }
}
