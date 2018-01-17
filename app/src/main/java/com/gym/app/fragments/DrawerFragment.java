package com.gym.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gym.app.R;
import com.gym.app.data.Prefs;
import com.gym.app.parts.home.HomeNavigator;
import com.gym.app.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * @author Paul
 * @since 2017.10.23
 */

public class DrawerFragment extends BaseFragment {

    private HomeNavigator mHomeNavigator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof HomeNavigator)) {
            throw new RuntimeException("HomeNavigator must be implemented in activity!");
        }
        mHomeNavigator = (HomeNavigator) context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Optional
    @OnClick({
            R.id.drawer_find_courses,
            R.id.drawer_my_courses,
            R.id.drawer_profile,
            R.id.drawer_shop,
            R.id.drawer_create_course,
            R.id.drawer_logout,
            R.id.drawer_terms
    })
    void onOptionsClicked(View view) {
        switch (view.getId()) {
            case R.id.drawer_find_courses:
                mHomeNavigator.goToFindCourses();
                return;
            case R.id.drawer_create_course:
                mHomeNavigator.goToCreateCourse();
                return;
            case R.id.drawer_my_courses:
                mHomeNavigator.goToMyCourses();
                return;
            case R.id.drawer_profile:
                mHomeNavigator.goToProfile();
                return;
            case R.id.drawer_shop:
                mHomeNavigator.goToShop();
                return;
            case R.id.drawer_logout:
                mHomeNavigator.logout();
                return;
            case R.id.drawer_terms:
                mHomeNavigator.goToTerms();
                return;
        }
    }

    @Override
    protected int getLayoutId() {
        if (Prefs.Role.get() == null) {
            return R.layout.fragment_drawer;
        }
        if (Prefs.Role.get().equals(Constants.USER)) {
            return R.layout.fragment_drawer;
        } else {
            return R.layout.fragment_drawer_trainer;
        }
    }
}
