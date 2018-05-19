package com.gym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gym.app.R;
import com.gym.app.data.model.ParkingHistory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ParkingHistoryActivity extends AppCompatActivity {

    @BindView(R.id.parking_histories)
    RecyclerView mParkingHistories;


    private List<ParkingHistory> mParkingHistoriesList;


    private void loadParkingHistoriesist(){
        mParkingHistoriesList = new ArrayList<>();

        mParkingHistoriesList.add(new ParkingHistory("place1"));
        mParkingHistoriesList.add(new ParkingHistory("place2"));
        mParkingHistoriesList.add(new ParkingHistory("place3"));
        mParkingHistoriesList.add(new ParkingHistory("place4"));
        mParkingHistoriesList.add(new ParkingHistory("place5"));
    }

    void setList(){
        mParkingHistories.set
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_history);
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
    }

}
