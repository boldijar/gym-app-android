package com.gym.app.data;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Paul
 * @since 2017.08.30
 */
public class SystemUtils {

    private final ConnectivityManager mConnectivityManager;

    public SystemUtils(ConnectivityManager connectivityManager) {
        mConnectivityManager = connectivityManager;
    }

    public boolean isNetworkUnavailable() {
        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        return activeNetwork == null || !activeNetwork.isConnectedOrConnecting();
    }
}