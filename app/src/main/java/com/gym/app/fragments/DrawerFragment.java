package com.gym.app.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gym.app.R;
import com.gym.app.data.Prefs;
import com.gym.app.data.model.User;
import com.gym.app.di.InjectionHelper;
import com.gym.app.parts.home.HomeNavigator;
import com.gym.app.server.UserService;
import com.gym.app.utils.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Paul
 * @since 2017.10.23
 */

public class DrawerFragment extends BaseFragment {

    private HomeNavigator mHomeNavigator;
    private SharedPreferences mSharedPreferences;

    @BindView(R.id.drawer_image)
    ImageView mImageView;

    @Inject
    UserService mUserService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectionHelper.getApplicationComponent().inject(this);
    }

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
        loadImage();
    }

    public void loadImage() {
        mUserService.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        if (mImageView == null) {
                            return;
                        }
                        Glide.with(getContext()).load(Constants.USER_ENDPOINT + user.mImage).into(mImageView);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable);
                    }
                });
    }

    @Optional
    @OnClick({
            R.id.drawer_find_courses,
            R.id.drawer_my_courses,
            R.id.drawer_profile,
            R.id.drawer_shop,
            R.id.drawer_create_course,
            R.id.drawer_logout,
            R.id.drawer_terms,
            R.id.drawer_notes,
            R.id.drawer_settings,
            R.id.drawer_gallery,
            R.id.drawer_trained_courses
    })
    void onOptionsClicked(View view) {
        switch (view.getId()) {
            case R.id.drawer_find_courses:
                mHomeNavigator.goToFindCourses();
                return;
            case R.id.drawer_trained_courses:
                mHomeNavigator.goToTrainedCourses();
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
            case R.id.drawer_notes:
                mHomeNavigator.goToNotes();
                return;
            case R.id.drawer_logout:
                mHomeNavigator.logout();
                return;
            case R.id.drawer_terms:
                mHomeNavigator.goToTerms();
                return;
            case R.id.drawer_settings:
                mHomeNavigator.goToSettings();
                return;
            case R.id.drawer_gallery:
                mHomeNavigator.goToGallery();
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
