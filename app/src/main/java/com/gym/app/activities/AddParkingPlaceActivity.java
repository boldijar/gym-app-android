package com.gym.app.activities;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.gym.app.R;
import com.gym.app.data.Prefs;
import com.gym.app.data.model.Car;
import com.gym.app.data.model.ParkPlace;
import com.gym.app.data.model.ParkPlaceBody;
import com.gym.app.di.InjectionHelper;
import com.gym.app.server.ApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddParkingPlaceActivity extends AppCompatActivity {
    @Inject
    ApiService mApiService;

    @BindView(R.id.parkNameText)
    EditText parkNameText;

    @BindView(R.id.parkSizeSpinner)
    Spinner parkSizeSpinner;

    @BindView(R.id.priceText)
    TextView priceText;

    @BindView(R.id.priceSeekBar)
    SeekBar priceSeekBar;

    private Double lat;
    private Double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InjectionHelper.getApplicationComponent().inject(this);
        ButterKnife.bind(this);

        this.lat = getIntent().getDoubleExtra("Lat", 0.0);
        this.lng = getIntent().getDoubleExtra("Lng", 0.0);

        populateSpinner();
        updatePriceRealTime();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updatePriceRealTime() {
        priceSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        priceText.setText( String.valueOf( seekBar.getProgress()) );
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
    }

    private void populateSpinner() {
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("small");
        spinnerArray.add("medium");
        spinnerArray.add("compact");
        spinnerArray.add("large truck");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.parkSizeSpinner);
        sItems.setAdapter(adapter);
    }

    public void addParkingPlace(View view) {
        ParkPlace toBeAddedParkPlace = new ParkPlace();
        toBeAddedParkPlace.mName = parkNameText.getText().toString();
        toBeAddedParkPlace.mSize = (String) parkSizeSpinner.getSelectedItem();
        toBeAddedParkPlace.mPricePerHour = priceSeekBar.getProgress();
        toBeAddedParkPlace.mDescription = "The description of this parking place";
        toBeAddedParkPlace.mLatitude = this.lat;
        toBeAddedParkPlace.mLongitude = this.lng;
        toBeAddedParkPlace.mAddress = "";


        mApiService.getUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( user -> {
                    toBeAddedParkPlace.mUser = user;

                    // Get the Address
                    Geocoder geocoder;
                    List<Address> addresses = new ArrayList<>();
                    geocoder = new Geocoder(this, Locale.getDefault());

                    try {
                        List<Address> x = geocoder.getFromLocation(this.lat, this.lng, 1);
                        Address myNeededAddress = x.get(0);
                        toBeAddedParkPlace.mAddress = myNeededAddress.getAddressLine(0);
                        int a = 3;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    ParkPlaceBody toBeAddedParkPlaceBody = new ParkPlaceBody(toBeAddedParkPlace);
                    mApiService.addParkingSpot(toBeAddedParkPlaceBody)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<ParkPlace>() {
                                           @Override
                                           public void onSubscribe(Disposable d) {

                                           }

                                           @Override
                                           public void onNext(ParkPlace pplace) {
                                               setResult(RESULT_OK);
                                               finish();
                                           }

                                           @Override
                                           public void onError(Throwable e) {
                                               Toast.makeText(getApplicationContext(), "Server error. Please try later.", Toast.LENGTH_SHORT).show();
                                               e.printStackTrace();
                                           }

                                           @Override
                                           public void onComplete() {

                                           }
                                       }

                                       );

                });


    }


}


