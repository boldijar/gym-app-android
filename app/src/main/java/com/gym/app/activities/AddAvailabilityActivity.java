package com.gym.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gym.app.R;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.05.19
 */
public class AddAvailabilityActivity extends BaseActivity {

    private final static String ARG_SPOT = "spot";
    private int mSpotId;

    public static Intent createIntent(Context context, int spotId) {
        Intent intent = new Intent(context, AddAvailabilityActivity.class);
        intent.putExtra(ARG_SPOT, spotId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_availability);
    }
}
