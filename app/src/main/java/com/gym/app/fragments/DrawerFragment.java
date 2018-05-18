package com.gym.app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gym.app.R;
import com.gym.app.di.InjectionHelper;
import com.gym.app.server.ApiService;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @BindView(R.id.drawer_first_name)
    TextView mFirstName;
    @BindView(R.id.drawer_second_name)
    TextView mSecondName;
    @BindView(R.id.drawer_credits)
    TextView mCredits;

    @Inject
    ApiService mApiService;

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
        mApiService.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    Glide.with(getContext()).load(user.mAvatar).into(mImageView);
                    mFirstName.setText(user.mFirstName);
                    mSecondName.setText(user.mLastName);
                    mCredits.setText(user.mPoints + " credits");

                }, throwable -> Timber.e(throwable));
    }


    @OnClick(R.id.drawer_menu_your_cars)
    void clickedYourCars(){
        Toast.makeText(getContext(), "Clicked car", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_drawer;
    }
}
