package com.gym.app.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gym.app.R;
import com.gym.app.data.model.Car;
import com.gym.app.data.model.CarBody;
import com.gym.app.di.InjectionHelper;
import com.gym.app.server.ApiService;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.05.19
 */
public class AddCarActivity extends BaseActivity {

    @Inject
    ApiService mApiService;

    @BindView(R.id.modelText)
    EditText mModel;

    @BindView(R.id.plateText)
    EditText mPlate;

    @BindView(R.id.sizeText)
    Spinner mSize;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        InjectionHelper.getApplicationComponent().inject(this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.add_car_button)
    public void addCar(View view) {
        Car toBeAddedCar = new Car();
        toBeAddedCar.setPlate(mPlate.getText().toString());
        toBeAddedCar.setSize(mSize.getSelectedItem().toString());
        toBeAddedCar.setModel(mModel.getText().toString());
        CarBody carBody = new CarBody(toBeAddedCar);


        mApiService.addCar(carBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Car>() {
                               @Override
                               public void onSubscribe(Disposable d) {

                               }

                               @Override
                               public void onNext(Car car) {
                                   setResult(RESULT_OK);
                                   finish();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   Toast.makeText(AddCarActivity.this, "Server error. Please try later.", Toast.LENGTH_SHORT).show();
                                   e.printStackTrace();
                               }

                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
