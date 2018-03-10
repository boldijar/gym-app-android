package com.gym.app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.gym.app.R;
import com.gym.app.data.Prefs;
import com.gym.app.di.InjectionHelper;
import com.gym.app.server.UserService;
import com.gym.app.utils.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Paul
 * @since 2017.10.23
 */

public class DrawerFragment extends BaseFragment {


    @BindView(R.id.drawer_image)
    ImageView mImageView;

    @BindView(R.id.drawer_radio_group)
    RadioGroup mRadioGroup;

    @Inject
    UserService mUserService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectionHelper.getApplicationComponent().inject(this);
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
                .subscribe(user -> {
                    if (mImageView == null) {
                        return;
                    }
                    Glide.with(getContext()).load(Constants.USER_ENDPOINT + user.mImage).into(mImageView);
                }, throwable -> Timber.e(throwable));
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
