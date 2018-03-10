package com.gym.app.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gym.app.R;
import com.gym.app.data.Prefs;
import com.gym.app.fragments.DrawerFragment;
import com.patloew.rxlocation.RxLocation;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * @author Paul
 * @since 2017.08.29
 */

public class HomeActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.home_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.home_input)
    EditText mInput;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerFragment mDrawerFragment;

    SupportMapFragment mSupportMapFragment;
    private GoogleMap mMap;
    private static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    Place mLastPlace;

    private RxLocation mRxLocation;
    private Marker mLocationMarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mDrawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.home_drawer_fragment);
        initDrawer();
        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.home_map);
        mSupportMapFragment.getMapAsync(this);

        loadLocationStuff();
    }

    private void loadLocationStuff() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Enable location permission from settings", Toast.LENGTH_SHORT).show();
            return;
        }
        mRxLocation = new RxLocation(getApplication());
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000);

        mRxLocation.location().updates(locationRequest)
                .timeout(1000, TimeUnit.DAYS)
                .flatMap(location -> mRxLocation.geocoding().fromLocation(location).toObservable())
                .subscribe(new Observer<Address>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Address address) {
                        Timber.d("Got new address: "+address.toString());
                        Prefs.Latitude.put(address.getLatitude());
                        Prefs.Longitude.put(address.getLongitude());
                        haveNewLocation(address.getLatitude(), address.getLongitude());
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

    private void haveNewLocation(double latitude, double longitude) {
        if (mLocationMarker == null) {
            return;
        }
        Timber.d("Moved marker to " + latitude + ", " + longitude);
        mLocationMarker.setPosition(new LatLng(latitude, longitude));
    }

    @OnClick(R.id.home_input)
    void inputTouched() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            Toast.makeText(this, "Map issue1", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, "Map issue2", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        mMap = googleMap;
        moveCameraTo(Prefs.Latitude.getDouble(0), Prefs.Longitude.getDouble(0));
        MarkerOptions locationMarkerOptions = new MarkerOptions()
                .position(new LatLng(Prefs.Latitude.getDouble(0), Prefs.Longitude.getDouble(0)))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
        mLocationMarker = mMap.addMarker(locationMarkerOptions);
    }

    private void moveCameraTo(double lat, double longi) {
        LatLng coordinate = new LatLng(lat, longi);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 14.2f);
        mMap.animateCamera(yourLocation);
    }

    public void clickedMenu(View view) {
        mDrawerLayout.openDrawer(Gravity.START);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "connectionFailed for Google Api", Toast.LENGTH_SHORT).show();
        Timber.e(connectionResult.getErrorMessage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mLastPlace = PlaceAutocomplete.getPlace(this, data);
                foundNewPlace();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void foundNewPlace() {
        if (mLastPlace == null) {
            return;
        }
        mInput.setText(mLastPlace.getAddress());
        moveCameraTo(mLastPlace.getLatLng().latitude, mLastPlace.getLatLng().longitude);
    }
}
