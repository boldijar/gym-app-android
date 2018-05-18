package com.gym.app.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gym.app.R;
import com.gym.app.data.model.Car;
import com.gym.app.data.model.CarBody;
import com.gym.app.di.InjectionHelper;
import com.gym.app.server.ApiService;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCars extends AppCompatActivity {

    @Inject
    ApiService mApiService;

    @BindView(R.id.modelText)
    EditText mModel;

    @BindView(R.id.plateText)
    EditText mPlate;

    @BindView(R.id.sizeText)
    EditText mSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cars);

        // Bind View
        View rootView = getWindow().getDecorView().getRootView();
        ButterKnife.bind(this, rootView);

        // Inject service
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addCar(View view) {
        Car toBeAddedCar = new Car();
        toBeAddedCar.setPlate(mPlate.getText().toString());
        toBeAddedCar.setSize(mSize.getText().toString());
        toBeAddedCar.setModel(mModel.getText().toString());
        CarBody carBody = new CarBody(toBeAddedCar);


        mApiService.addCar(carBody).subscribe( car -> {
            Toast.makeText(getBaseContext(),(String)car.getPlate(), Toast.LENGTH_SHORT).show();
        });

    }
}
