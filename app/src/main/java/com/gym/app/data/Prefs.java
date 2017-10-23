package com.gym.app.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * @author Paul
 * @since 2017.08.29
 */

public enum Prefs {

    Token;

    Prefs(String defaultValue) {
        mDefaultValue = defaultValue;
    }

    Prefs() {

    }

    private String mDefaultValue;

    private static SharedPreferences sSharedPreferences;
    private static Gson GSON = new Gson();

    public static void init(Application application) {
        sSharedPreferences = application.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public void put(Object value) {
        if (value != null) {
            sSharedPreferences.edit().putString(name(), value + "").commit();
        } else {
            sSharedPreferences.edit().putString(name(), null).commit();
        }
    }

    public void putAsJson(Object object) {
        put(GSON.toJson(object));
    }

    public <T> T getFromJson(Class<T> type) {
        String json = get();
        if (json == null) {
            return null;
        }
        return GSON.fromJson(json, type);
    }

    public String get() {
        return sSharedPreferences.getString(name(), mDefaultValue);
    }


    public boolean getBoolean(boolean defaultValue) {
        String value = get();
        if (value == null) {
            return defaultValue;
        }
        return Boolean.valueOf(value);
    }

    public int getInteger(int defaultValue) {
        String value = get();
        if (value == null) {
            return defaultValue;
        }
        return Integer.valueOf(value);
    }
}
