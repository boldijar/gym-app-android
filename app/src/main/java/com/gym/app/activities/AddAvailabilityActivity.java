package com.gym.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gym.app.R;
import com.gym.app.di.InjectionHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.blackbox_vision.datetimepickeredittext.view.DatePickerInputEditText;
import io.blackbox_vision.datetimepickeredittext.view.TimePickerInputEditText;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.05.19
 */
public class AddAvailabilityActivity extends BaseActivity {

    private final static String ARG_SPOT = "spot";
    private int mSpotId;

    @BindView(R.id.from_date_picker)
    DatePickerInputEditText mFromDate;
    @BindView(R.id.from_time_picker)
    TimePickerInputEditText mFromTime;
    @BindView(R.id.to_date_picker)
    DatePickerInputEditText mToDate;
    @BindView(R.id.to_time_picker)
    TimePickerInputEditText mToTime;

    public static Intent createIntent(Context context, int spotId) {
        Intent intent = new Intent(context, AddAvailabilityActivity.class);
        intent.putExtra(ARG_SPOT, spotId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_availability);
        ButterKnife.bind(this);
        InjectionHelper.getApplicationComponent().inject(this);
        mFromDate.setManager(getSupportFragmentManager());
        mFromTime.setManager(getSupportFragmentManager());
        mToDate.setManager(getSupportFragmentManager());
        mToTime.setManager(getSupportFragmentManager());
    }
}
