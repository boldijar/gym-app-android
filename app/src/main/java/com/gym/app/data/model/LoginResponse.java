package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Paul
 * @since 2017.10.29
 */

public class LoginResponse {

    @SerializedName("token")
    public String mToken;
    @SerializedName("role")
    public String mRole;
    @SerializedName("error")
    public String mError;

    public boolean hasError() {
        return mError != null;
    }
}
