package com.gym.app.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.gym.app.R;
import com.gym.app.fragments.DrawerFragment;
import com.gym.app.parts.home.BaseHomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Paul
 * @since 2017.08.29
 */

public class HomeActivity extends BaseActivity {

    @BindView(R.id.home_drawer_layout)
    DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerFragment mDrawerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mDrawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.home_drawer_fragment);
        initDrawer();
    }

    private void initDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.appname, R.string.appname) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                mDrawerFragment.loadImage();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
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
}
