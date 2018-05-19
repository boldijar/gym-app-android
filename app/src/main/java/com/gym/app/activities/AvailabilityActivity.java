package com.gym.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.gym.app.R;
import com.gym.app.data.model.Availability;
import com.gym.app.di.InjectionHelper;
import com.gym.app.parts.adapters.AvailabilityAdapter;
import com.gym.app.server.ApiService;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Paul
 * @since 2018.05.19
 */
public class AvailabilityActivity extends BaseActivity implements AvailabilityAdapter.AvailabilityListener {

    private static final String ARG_SPOT = "spot";
    private static final int REQUEST_ADD = 122;
    @BindView(R.id.availability_recycler)
    RecyclerView mRecyclerView;

    @Inject
    ApiService mApiService;

    private int mSpotId;
    private AvailabilityAdapter mAvailabilityAdapter;

    public static Intent createIntent(Context context, int spotId) {
        Intent intent = new Intent(context, AvailabilityActivity.class);
        intent.putExtra(ARG_SPOT, spotId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);
        ButterKnife.bind(this);
        InjectionHelper.getApplicationComponent().inject(this);
        mSpotId = getIntent().getIntExtra(ARG_SPOT, -1);
        mAvailabilityAdapter = new AvailabilityAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAvailabilityAdapter);
        loadAvailabilities();
    }

    private void loadAvailabilities() {
        mApiService.getAvailabilities(mSpotId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Availability>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Availability> availabilities) {
                        gotAvailabilities(availabilities);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_availability, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            Intent intent = AddAvailabilityActivity.createIntent(this, mSpotId);
            startActivityForResult(intent, REQUEST_ADD);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK) {
            loadAvailabilities();
        }
    }

    private void gotAvailabilities(List<Availability> availabilities) {
        mAvailabilityAdapter.setAvailabilities(availabilities);
    }

    @Override
    public void onDelete(int id) {

    }

    @Override
    public void onEdit(Availability availability) {
        Intent intent = AddAvailabilityActivity.createIntent(this, availability.mParkSpotId, availability.mSchedule, availability.mId);
        startActivityForResult(intent, REQUEST_ADD);
    }
}
