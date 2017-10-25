package com.gym.app.parts.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gym.app.R;
import com.gym.app.activities.BaseActivity;
import com.gym.app.activities.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AuthenticationActivity extends BaseActivity implements AuthenticationNavigation {

    @BindView(R.id.authentication_view_pager)
    ViewPager mAuthenticationViewPager;

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
