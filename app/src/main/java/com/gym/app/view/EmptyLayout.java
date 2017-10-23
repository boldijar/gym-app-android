package com.gym.app.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gym.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author Paul
 * @since 2017.08.30
 */

public class EmptyLayout extends LinearLayout {

    @BindView(R.id.empty_layout_button)
    TextView mButton;
    @BindView(R.id.empty_layout_text)
    TextView mText;
    @BindView(R.id.empty_layout_progress)
    View mProgress;

    public EmptyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmptyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EmptyLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.view_empty_layout, this, true);
        ButterKnife.bind(this, view);
    }

    public void setOnRetryListener(OnClickListener onRetryListener) {
        mButton.setOnClickListener(onRetryListener);
    }

    public void setState(State state) {
        setState(state, 0);
    }

    public void setState(State state, int textId) {
        switch (state) {
            case LOADING:
                mProgress.setVisibility(VISIBLE);
                mText.setVisibility(GONE);
                mButton.setVisibility(GONE);
                break;
            case ERROR:
            case EMPTY:
                mProgress.setVisibility(GONE);
                mText.setVisibility(VISIBLE);
                mButton.setVisibility(VISIBLE);
                if (textId == 0) {
                    mText.setText(null);
                } else {
                    mText.setText(textId);
                }
                break;
            case EMPTY_NO_BUTTON:
                mProgress.setVisibility(GONE);
                mText.setVisibility(VISIBLE);
                mButton.setVisibility(GONE);
                if (textId == 0) {
                    mText.setText(null);
                } else {
                    mText.setText(textId);
                }
                break;
            case CLEAR:
                mProgress.setVisibility(GONE);
                mText.setVisibility(GONE);
                mButton.setVisibility(GONE);
        }
    }

    public enum State {
        LOADING,
        ERROR,
        EMPTY,
        EMPTY_NO_BUTTON,
        CLEAR
    }
}
