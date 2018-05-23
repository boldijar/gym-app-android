package com.gym.app.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.gym.app.R;
import com.gym.app.adapters.PHAdapter;
import com.gym.app.data.model.ParkingHistory;
import com.gym.app.data.model.ParkingHistoryBody;
import com.gym.app.di.InjectionHelper;
import com.gym.app.server.ApiService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ParkingHistoryActivity extends AppCompatActivity {

    @Inject
    ApiService mApiService;

    @BindView(R.id.parking_histories_recycler_view)
    RecyclerView mParkingHistories;

    PHAdapter mAdapter;

    private ArrayList<ParkingHistory> mParkingHistoriesList=new ArrayList<>();


    private void setupRecycler(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mParkingHistories.setLayoutManager(layoutManager);


        mAdapter = new PHAdapter(mParkingHistoriesList);
        mParkingHistories.setAdapter(mAdapter);

        mParkingHistories.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void loadParkingHistoriesist(){
        mParkingHistoriesList = new ArrayList<>();

        mApiService.getParkingHistories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listOfParkingHistories -> {

                    this.mAdapter.setmPHs(listOfParkingHistories);
                    this.mAdapter.notifyDataSetChanged();
                });
        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_history);

        View rootView = getWindow().getDecorView().getRootView();
        ButterKnife.bind(this);


        // inject service
        InjectionHelper.getApplicationComponent().inject(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }


        });
        setupRecycler();
        getParkingHistories();
    }

/*
    public void addParkingHistory(View view){
        ParkingHistory toBeParkingHistory = new ParkingHistory();

        toBeParkingHistory.setSpot(mName.getText().toString());
        toBeParkingHistory.setAddress(mAddress.getText().toString());
        toBeParkingHistory.setPrice(mPrice.getText().toString());
        toBeParkingHistory.setStart_date(mStartDate.getText().toString());
        toBeParkingHistory.setEnd_date(mEndDate.getText().toString());

        ParkingHistoryBody parkingHistoryBody = new ParkingHistoryBody(toBeParkingHistory);

        mApiService.addParkingHistory(parkingHistoryBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        parkingHistory -> {
                            getParkingHistories();
                        }

                );

    }
*/


    public void getParkingHistories(){
        mApiService.getParkingHistories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listOfParkingHistories -> {

                    this.mAdapter.setmPHs(listOfParkingHistories);
                    this.mAdapter.notifyDataSetChanged();
                });
    }

}
