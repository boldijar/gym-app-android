package com.gym.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.gym.app.R;
import com.gym.app.data.Prefs;
import com.gym.app.parts.authentication.AuthenticationActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;
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

    @BindView(R.id.imageViewSplash)
    ImageView mImageViewSplash;
    @BindView(R.id.textViewSplash)
    TextView mTextViewSplash;
    @BindAnim(R.anim.down_to_up)
    Animation mLogoAnimation;
    @BindAnim(R.anim.up_to_down)
    Animation mTextAnimation;
    private boolean mTextAnimationIsFinished = false;
    private boolean mLogoAnimationIsFinished = false;

    public static Intent createIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setUIElements();
        setAnimation();
        isFinishedDownToUp();
        isFinishedUpToDown();
    }

    private void timer() {
        Observable.timer(1, TimeUnit.SECONDS)
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

    private void isFinishedDownToUp() {
        mLogoAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLogoAnimationIsFinished = true;
                if (mTextAnimationIsFinished) {
                    timer();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void isFinishedUpToDown() {
        mTextAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTextAnimationIsFinished = true;
                if (mLogoAnimationIsFinished) {
                    timer();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void doneWaiting() {
        if (Prefs.Token.get() != null) {
            startActivity(HomeActivity.createIntent(this));
        } else {
            startActivity(AuthenticationActivity.createIntent(this));
        }
        finish();
    }

    private void setUIElements() {
        ButterKnife.bind(this);
    }

    private void setAnimation() {
        mImageViewSplash.setAnimation(mLogoAnimation);
        mTextViewSplash.setAnimation(mTextAnimation);
    }
}
