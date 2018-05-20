package com.gym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gym.app.R;
import com.gym.app.data.model.Car;
import com.gym.app.di.InjectionHelper;
import com.gym.app.parts.adapters.CarsAdapter;
import com.gym.app.server.ApiService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyCarsActivity extends AppCompatActivity {

    private static final int REQUEST_ADD = 100;
    @Inject
    ApiService mApiService;

    @BindView(R.id.carsRecyclerView)
    RecyclerView carsRecyclerView;

    CarsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cars);
        ButterKnife.bind(this);

        // Inject service
        InjectionHelper.getApplicationComponent().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.my_cars_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCarsActivity.this, AddCarActivity.class);
                startActivityForResult(intent, REQUEST_ADD);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        carsRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new CarsAdapter(new ArrayList<Car>());
        carsRecyclerView.setAdapter(adapter);

        getCars();
    }


    public void getCars() {
        mApiService.getCars()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Car>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Car> cars) {
                        adapter.setCarsList(cars);
                        adapter.notifyDataSetChanged();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_ADD) {
            getCars();
        }
    }
}
