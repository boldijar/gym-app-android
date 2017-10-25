package com.gym.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.gym.app.R;
import com.gym.app.data.Prefs;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Paul
 * @since 2017.08.29
 */

public class SplashActivity extends BaseActivity {

    private ImageView imageViewSplash;
    private TextView textViewSplash;
    private Animation upToDown, downToUp;

    public static Intent createIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setUIElements();
        setAnimation();

        Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long value) {
                        doneWaiting();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void doneWaiting() {
        if (Prefs.Token.get() != null) {
            startActivity(HomeActivity.createIntent(this));
        } else {
            startActivity(LoginActivity.createIntent(this));
        }
        finish();
    }

    private void setUIElements(){
        imageViewSplash = findViewById(R.id.imageViewSplash);
        textViewSplash = findViewById(R.id.textViewSplash);
    }

    private void setAnimation(){
        upToDown = AnimationUtils.loadAnimation(this, R.anim.up_to_down);
        downToUp = AnimationUtils.loadAnimation(this, R.anim.down_to_up);
        imageViewSplash.setAnimation(downToUp);
        textViewSplash.setAnimation(upToDown);
    }
}
