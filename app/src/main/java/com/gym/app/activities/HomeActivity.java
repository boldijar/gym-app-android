package com.gym.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.gym.app.R;
import com.gym.app.data.Prefs;
import com.gym.app.parts.findcourses.FindCoursesFragment;
import com.gym.app.parts.home.BaseHomeFragment;
import com.gym.app.parts.home.HomeNavigator;
import com.gym.app.parts.mycourses.MyCoursesFragment;
import com.gym.app.parts.profile.ProfileFragment;
import com.gym.app.parts.shop.ShopFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Paul
 * @since 2017.08.29
 */

public class HomeActivity extends BaseActivity implements HomeNavigator {

    @BindView(R.id.home_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    public static Intent createIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initDrawer();
        goToMyCourses();
    }

    private void initDrawer() {
        setSupportActionBar(mToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.appname, R.string.appname) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setFragment(BaseHomeFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_container, fragment)
                .commit();
        mDrawerLayout.closeDrawer(Gravity.START);
    }

    @Override
    public void goToMyCourses() {
        setFragment(new MyCoursesFragment());
    }

    @Override
    public void goToFindCourses() {
        setFragment(new FindCoursesFragment());
    }

    @Override
    public void goToShop() {
        setFragment(new ShopFragment());
    }

    @Override
    public void goToProfile() {
        setFragment(new ProfileFragment());
    }

    @Override
    public void logout() {
        Prefs.Token.put(null);
        Intent intent = SplashActivity.createIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
