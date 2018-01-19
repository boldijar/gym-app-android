package com.gym.app.parts.settings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gym.app.R;
import com.gym.app.data.Prefs;
import com.gym.app.data.model.AtTheGym;
import com.gym.app.parts.home.BaseHomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Shows all settings related stuff
 *
 * @author Paul
 * @since 2018.01.17
 */

public class SettingsFragment extends BaseHomeFragment implements SettingsView {
    private static final int REQUEST_CAMERA = 100;

    @BindView(R.id.settings_checkin)
    TextView mCheckinButton;
    @BindView(R.id.settings_notifications)
    SwitchCompat mNotificationsSwitch;
    @BindView(R.id.settings_newsletter)
    SwitchCompat mNewsletterSwitch;

    @BindView(R.id.no_users_at_gym)
    TextView mNumberOfUsers;

    private SettingsPresenter mSettingsPresenter = new SettingsPresenter(this);

    @Override
    protected int getTitle() {
        return R.string.settings;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        updateCheckInText();
        updateSwitches();
    }

    @OnCheckedChanged(R.id.settings_notifications)
    public void radioButtonCheckChanged(CompoundButton compoundButton, boolean checked) {
        Prefs.NotificationsEnabled.put(checked);
    }

    @OnCheckedChanged(R.id.settings_newsletter)
    public void newsletterChanged(CompoundButton compoundButton, boolean checked) {
        Prefs.SubscribedToNewsLetter.put(checked);
        mSettingsPresenter.subscribeToNewsLetter(checked);
    }

    private void updateSwitches() {
        mNotificationsSwitch.setChecked(Prefs.NotificationsEnabled.getBoolean(true));
        mNewsletterSwitch.setChecked(Prefs.SubscribedToNewsLetter.getBoolean(true));
        mSettingsPresenter.getNumberOfUsers();
    }

    private void updateCheckInText() {
        mCheckinButton.setText(Prefs.IsAtGym.getBoolean(false) ? R.string.check_out : R.string.check_in);
    }

    @OnClick(R.id.settings_checkin)
    void checkinClick() {
        if (Prefs.IsAtGym.getBoolean(false)) {
            wantToCheckOut();
            return;
        }
        wantToCheckIn();
    }

    private void wantToCheckIn() {
        if (cameraPermissionGranted()) {
            mHomeNavigator.goToScan();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }
    }

    private boolean cameraPermissionGranted() {
        return ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Prefs.ScannedSuccessfully.getBoolean(false)) {
            Prefs.ScannedSuccessfully.put(false);
            if (mCheckinButton != null) {
                Timber.d("Doing the checkin because of successfull scan");
                mSettingsPresenter.checkInUser(true);
            }
        }
    }

    private void wantToCheckOut() {
        mSettingsPresenter.checkInUser(false);
    }

    @Override
    public void checkInSuccess(boolean checkedIn) {
        Prefs.IsAtGym.put(checkedIn);
        updateCheckInText();
    }

    @Override
    public void numberOfUsers(AtTheGym noUsers) {
        String text = "People at gym: " + noUsers.mNumberOfUsers;
        mNumberOfUsers.setText(text);
    }
}
