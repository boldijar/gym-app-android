package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.05.19
 */
public class AuthBody {
    @SerializedName("auth")
    public final Auth mAuth;

    public AuthBody(Auth auth) {
        mAuth = auth;
    }
}
