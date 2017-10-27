package com.gym.app.parts.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.gym.app.R;
import com.gym.app.activities.BaseActivity;
import com.gym.app.activities.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AuthenticationActivity extends BaseActivity implements AuthenticationNavigation {

    private static final int REGISTER_FRAGMENT_POSITION = 1;

    @BindView(R.id.authentication_view_pager)
    ViewPager mAuthenticationViewPager;

    @BindView(R.id.application_logo)
    ImageView applicationLogo;

    private AuthenticationPagerAdapter mAuthenticationPagerAdapter;

    public static Intent createIntent(Context context) {
        return new Intent(context, AuthenticationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        mAuthenticationPagerAdapter = new AuthenticationPagerAdapter(getSupportFragmentManager());
        mAuthenticationViewPager.setAdapter(mAuthenticationPagerAdapter);

        mAuthenticationViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == REGISTER_FRAGMENT_POSITION && positionOffset == 0) {
                    positionOffset = 1;
                }
                if (positionOffset < 0.5) {
                    applicationLogo.setScaleX(1 - positionOffset);
                    applicationLogo.setScaleY(1 - positionOffset);
                } else {
                    applicationLogo.setScaleX(positionOffset);
                    applicationLogo.setScaleY(positionOffset);
                }

                applicationLogo.setRotation(positionOffset * 180);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void doLogin(View view) {
        startActivity(HomeActivity.createIntent(this));
        finish();
    }

    @Override
    public void goToRegister() {
        mAuthenticationViewPager.setCurrentItem(AuthenticationPagerAdapter.REGISTER_FRAGMENT);
    }

    @Override
    public void goBack() {
        mAuthenticationViewPager.setCurrentItem(AuthenticationPagerAdapter.LOGIN_FRAGMENT);
    }
}
