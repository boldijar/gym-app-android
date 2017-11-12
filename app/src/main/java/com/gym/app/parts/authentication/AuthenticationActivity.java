package com.gym.app.parts.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.Toast;

import com.gym.app.R;
import com.gym.app.activities.BaseActivity;
import com.gym.app.activities.HomeActivity;
import com.gym.app.data.Prefs;
import com.gym.app.data.model.LoginResponse;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AuthenticationActivity extends BaseActivity implements AuthenticationNavigation, AuthenticationView {

    private static final int REGISTER_FRAGMENT_POSITION = 1;
    private static final String ARG_INVALID_TOKEN = "arginvalidtoken";

    @BindView(R.id.authentication_view_pager)
    ViewPager mAuthenticationViewPager;

    @BindView(R.id.application_logo)
    ImageView applicationLogo;

    private AuthenticationPagerAdapter mAuthenticationPagerAdapter;
    private AuthenticationPresenter mAuthenticationPresenter;

    public static Intent createIntent(Context context) {
        return new Intent(context, AuthenticationActivity.class);
    }

    public static Intent createExpiredTokenIntent(Context context) {
        Intent intent = new Intent(context, AuthenticationActivity.class);
        intent.putExtra(ARG_INVALID_TOKEN, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        mAuthenticationPresenter = new AuthenticationPresenter(this);
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
        if (getIntent().getBooleanExtra(ARG_INVALID_TOKEN, false)) {
            Toast.makeText(this, R.string.token_expired, Toast.LENGTH_SHORT).show();
        }
    }

    public void doLogin(String email, String password) {
        mAuthenticationPresenter.login(email, password);
    }

    @Override
    public void goToRegister() {
        mAuthenticationViewPager.setCurrentItem(AuthenticationPagerAdapter.REGISTER_FRAGMENT);
    }

    @Override
    public void goBack() {
        mAuthenticationViewPager.setCurrentItem(AuthenticationPagerAdapter.LOGIN_FRAGMENT);
    }

    @Override
    public void showLoginResponse(LoginResponse loginResponse) {
        Prefs.Token.put(loginResponse.mToken);
        startActivity(HomeActivity.createIntent(this));
        finish();
    }

    @Override
    public void showError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuthenticationPresenter.destroySubscriptions();
    }
}
