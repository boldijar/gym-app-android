package com.gym.app.parts.scan;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.gym.app.activities.BaseActivity;
import com.gym.app.data.Prefs;

/**
 * @author Paul
 * @since 2018.01.17
 */

public class ScanActivity extends BaseActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private static final String TEXT_TO_SCAN = "ketamina";

    private QRCodeReaderView mQrCodeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQrCodeView = new QRCodeReaderView(this);
        setContentView(mQrCodeView);

        mQrCodeView.setOnQRCodeReadListener(this);
        mQrCodeView.setQRDecodingEnabled(true);
        mQrCodeView.setAutofocusInterval(1000);
        mQrCodeView.setBackCamera();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQrCodeView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mQrCodeView.stopCamera();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, ScanActivity.class);
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        if (!text.equals(TEXT_TO_SCAN)) {
            return;
        }
        Prefs.ScannedSuccessfully.put(true);
        finish();
    }
}
