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
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.gym.app.data.model.BookParking;
import com.gym.app.data.model.ParkPlace;
import com.gym.app.data.model.ParkingHistory;
import com.gym.app.di.InjectionHelper;
import com.gym.app.fragments.DrawerFragment;
import com.gym.app.server.ApiService;
import com.patloew.rxlocation.RxLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Paul
 * @since 2017.08.29
 */

public class HomeActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener {

    @Inject
    ApiService mApiService;

    @BindView(R.id.home_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.home_input)
    EditText mInput;

    @BindView(R.id.home_bottom_card)
    View mCardRoot;

    @BindView(R.id.card_address)
    TextView mCardAdress;

    @BindView(R.id.card_title)
    TextView mCardTitle;

    @BindView(R.id.card_image)
    ImageView mCardImage;

//    @BindView(R.id.manageParkingSpacesText)
//    TextView manageParkingSpaceText;

//    @BindView(R.id.cancelOwnParkingSpots)
//    FloatingActionButton cancelOwnParkingSpotsButton;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerFragment mDrawerFragment;

    SupportMapFragment mSupportMapFragment;
    private GoogleMap mMap;
    private static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    Place mLastPlace;

    private RxLocation mRxLocation;
    private Marker mLocationMarker;

    private List<ParkPlace> mParkPlaces;
    private List<Marker> mParkPlacesMarkers = new ArrayList<>();

    private TimeFilterDialogFragment timeFilterDialogFragment;

    private Boolean isShowingOwnParkingPlaces = false;

    ParkPlace mLastParkPlace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InjectionHelper.getApplicationComponent().inject(this);
        ButterKnife.bind(this);
        mDrawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.home_drawer_fragment);
        initDrawer();
        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.home_map);
        mSupportMapFragment.getMapAsync(this);
        timeFilterDialogFragment=new TimeFilterDialogFragment();
//        Intent history = new Intent(this, ParkingHistory.class);
//        startActivity(history);

        loadLocationStuff();
        showCard(false);

//        Intent goToAuth = new Intent(this, AuthenticationActivity.class);
//        startActivity(goToAuth);
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
                        Timber.d("Got new address: " + address.toString());
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
        loadParkingPlaces();
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    private void loadParkingPlaces() {
        mApiService.getParkingPlaces(
                Prefs.Latitude.getDouble(0),
                Prefs.Longitude.getDouble(0 ),
                1000.0,
                null,
                null
        )
                .subscribeOn(Schedulers.io())
                .doOnError(Throwable::printStackTrace)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::gotParkPlaces, Throwable::printStackTrace);
    }

    private void gotParkPlaces(List<ParkPlace> parkPlaces) {
        for (Marker marker : mParkPlacesMarkers) {
            marker.remove();
        }
        mParkPlacesMarkers.clear();
        mParkPlaces = parkPlaces;
        for (ParkPlace parkPlace : parkPlaces) {
            MarkerOptions options = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource( R.drawable.ic_park_spot_green ))
                    .position(new LatLng(parkPlace.mLatitude, parkPlace.mLongitude));
            Marker marker = mMap.addMarker(options);
            marker.setTag(parkPlace);
            mParkPlacesMarkers.add(marker);
        }

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

    @OnClick(R.id.card_image)
    void cardImageClick() {
        showCard(false);
    }

    private boolean mShowing = false;

    private void showCard(boolean show) {
        mShowing = show;
        mCardRoot.post(() -> {
            if (show) {
                mCardRoot.setVisibility(View.VISIBLE);
                TranslateAnimation animate = new TranslateAnimation(
                        0,                 // fromXDelta
                        0,                 // toXDelta
                        mCardRoot.getHeight(),  // fromYDelta
                        0);                // toYDelta
                animate.setDuration(500);
                animate.setFillAfter(true);
                mCardRoot.startAnimation(animate);
            } else {
                TranslateAnimation animate = new TranslateAnimation(
                        0,                 // fromXDelta
                        0,                 // toXDelta
                        0,                 // fromYDelta
                        mCardRoot.getHeight()); // toYDelta
                animate.setDuration(500);
                animate.setFillAfter(true);
                mCardRoot.startAnimation(animate);
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTag() == null || !(marker.getTag() instanceof ParkPlace)) {
            return true;
        }
        this.mLastParkPlace = (ParkPlace) marker.getTag();
        mLastParkPlace = mLastParkPlace;
        Glide.with(this).load(mLastParkPlace.mUser.mAvatar).into(mCardImage);
        mCardAdress.setText(mLastParkPlace.mAddress);
        mCardTitle.setText(mLastParkPlace.mUser.mFirstName + " " + mLastParkPlace.mUser.mLastName + " spot #" + mLastParkPlace.mId);
        showCard(true);
        return true;
    }

    public void clickedFilter(View view) {
        this.timeFilterDialogFragment.show(getSupportFragmentManager(), "tag");
    }

    public void timeDialogDone(View view) {
        // Close the time dialog
        this.timeFilterDialogFragment.dismiss();

        int startingHour = this.timeFilterDialogFragment.mTimePicker1.getCurrentHour();
        int startingDay = this.timeFilterDialogFragment.mDatePicker1.getDayOfMonth();
        int startingMonth = this.timeFilterDialogFragment.mDatePicker1.getMonth();
        int startingYear = this.timeFilterDialogFragment.mDatePicker1.getYear();



        int endingHour = this.timeFilterDialogFragment.mTimePicker2.getCurrentHour();
        int endingDay = this.timeFilterDialogFragment.mDatePicker2.getDayOfMonth();
        int endingMonth = this.timeFilterDialogFragment.mDatePicker2.getMonth();
        int endingYear =this.timeFilterDialogFragment. mDatePicker2.getYear();


        // Goal: 2018-05-19 11:11:06 +0300
        StringBuilder start = new StringBuilder();
        start.append(startingYear); start.append("-");
        start.append(startingMonth); start.append("-");
        start.append(startingDay); start.append(" ");
        start.append(startingHour); start.append(":00:00 +0300");

        StringBuilder end = new StringBuilder();
        end.append(endingYear); end.append("-");
        end.append(endingMonth); end.append("-");
        end.append(endingDay); end.append(" ");
        end.append(endingHour); end.append(":00:00 +0300");



        mApiService.getParkingPlaces(
                Prefs.Latitude.getDouble(0),
                Prefs.Longitude.getDouble(0),
                1000.0,
                start.toString(),
                end.toString()

        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace)
                .subscribe(this::gotParkPlaces);

    }

    public void ownPlaceClicked(View view) {
        Intent goToManageActivity = new Intent(this, ManageActivity.class);
        startActivity(goToManageActivity);
//
//        mApiService.getOwnParkingPlaces().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::gotParkPlaces);
//
//        mDrawerLayout.closeDrawers();
//        cancelOwnParkingSpotsButton.setVisibility(View.VISIBLE);
//        isShowingOwnParkingPlaces = true;
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        if(isShowingOwnParkingPlaces) {
            Intent goToAddParkingPlaces = new Intent(this, AddParkingPlaceActivity.class);
            goToAddParkingPlaces.putExtra("Lat", String.valueOf(latLng.latitude));
            goToAddParkingPlaces.putExtra("Lng", String.valueOf(latLng.longitude));
            startActivity(goToAddParkingPlaces);
        }

    }

    public void cardCancel(View view) {
        showCard(false);
    }

    public void findParkingPlaces(View view) {

        // Load the default parking spaces
        loadParkingPlaces();

        isShowingOwnParkingPlaces = false;
    }

    public void bookParking(View view) {
       
    }

    public void goToParkingHistory(View view) {
        Intent goToPArkingHistoy = new Intent(this, ParkingHistoryActivity.class);
        startActivity(goToPArkingHistoy);
    }
}
