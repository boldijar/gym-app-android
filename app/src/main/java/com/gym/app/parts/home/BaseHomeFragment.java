package com.gym.app.parts.home;

import android.content.Context;
import android.support.annotation.StringRes;

import com.gym.app.fragments.BaseFragment;

/**
 * Fragment class that will be used everywhere in the home activity
 *
 * @author Paul
 * @since 2017.10.25
 */

public abstract class BaseHomeFragment extends BaseFragment {

    protected HomeNavigator mHomeNavigator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof HomeNavigator)) {
            throw new RuntimeException("You must implement the HomeNavigator in the fragment activity!");
        }
        mHomeNavigator = (HomeNavigator) context;
    }

    protected abstract
    @StringRes
    int getTitle();

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getTitle());
    }
}
