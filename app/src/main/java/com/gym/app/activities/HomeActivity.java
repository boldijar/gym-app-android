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
import com.gym.app.fragments.DrawerFragment;
import com.gym.app.parts.create_course.CreateCourseFragment;
import com.gym.app.parts.findcourses.FindCoursesFragment;
import com.gym.app.parts.gallery.GalleryFragment;
import com.gym.app.parts.home.BaseHomeFragment;
import com.gym.app.parts.home.HomeNavigator;
import com.gym.app.parts.mycourses.MyCoursesFragment;
import com.gym.app.parts.notes.NotesFragment;
import com.gym.app.parts.profile.ProfileFragment;
import com.gym.app.parts.scan.ScanActivity;
import com.gym.app.parts.settings.SettingsFragment;
import com.gym.app.parts.shop.ShopFragment;
import com.gym.app.parts.terms.TermsActivity;
import com.gym.app.parts.trainedcourses.TrainedCoursesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Paul
 * @since 2017.08.29
 */

public class HomeActivity extends BaseActivity implements HomeNavigator {

    private static final String ARG_GO_TO_MY_COURSES = "mycourses";
    @BindView(R.id.home_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerFragment mDrawerFragment;

    public static Intent createIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    public static Intent createMyCoursesIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(ARG_GO_TO_MY_COURSES, true);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mDrawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.home_drawer_fragment);
        initDrawer();
        goToMyCourses();
    }


    @Override
    protected void onNewIntent(Intent intent) {
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
                mDrawerFragment.loadImage();
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
    public void goToCreateCourse() {
        setFragment(new CreateCourseFragment());
    }

    @Override
    public void goToSettings() {
        setFragment(new SettingsFragment());
    }

    @Override
    public void goToScan() {
        startActivity(ScanActivity.createIntent(this));
    }

    @Override
    public void goToGallery() {
        setFragment(new GalleryFragment());
    }

    @Override
    public void goToTrainedCourses() {
        setFragment(new TrainedCoursesFragment());
    }

    @Override
    public void goToTerms() {
        startActivity(new Intent(this, TermsActivity.class));
    }

    @Override
    public void goToNotes() {
        setFragment(new NotesFragment());
    }

    @Override
    public void logout() {
        Prefs.Token.put(null);
        Prefs.Role.put(null);
        Intent intent = SplashActivity.createIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
