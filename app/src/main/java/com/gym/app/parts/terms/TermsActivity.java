package com.gym.app.parts.terms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.gym.app.R;
import com.gym.app.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by stefanvacareanu on 11/13/17.
 */

public class TermsActivity extends BaseActivity {


    @BindView(R.id.webview_terms)
    WebView mWebViewTerms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        ButterKnife.bind(this);
        mWebViewTerms.loadUrl("file:///android_asset/TermsAndConditionsHTML");

    }

    public static Intent createIntent(Context context){
        Intent intent = new Intent(context,TermsActivity.class);
        return intent;
    }


}
