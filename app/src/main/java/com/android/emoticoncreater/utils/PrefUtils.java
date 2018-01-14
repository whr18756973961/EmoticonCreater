package com.android.emoticoncreater.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.android.emoticoncreater.app.APP;
import com.android.emoticoncreater.config.Constants;

import java.util.Set;


/**
 * sharedPreferences工具类
 */

public class PrefUtils {

    public static SharedPreferences getDefaultSp() {
        return PreferenceManager.getDefaultSharedPreferences(APP.get());
    }

    public static void set(@NonNull String key, @NonNull Object value) {
        SharedPreferences.Editor edit = getDefaultSp().edit();

        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else if (value instanceof Set) {
            edit.putStringSet(key, (Set<String>) value);
        } else {
            throw new IllegalArgumentException(String.format("Type of value unsupported key=%s, value=%s", key, value));
        }
        edit.apply();
    }

    public static void clearKey(@NonNull String key) {
        getDefaultSp().edit().remove(key).apply();
    }

    public static String getTheme(){
        return getDefaultSp().getString(Constants.THEME, Constants.LIGHT_TEAL);
    }

    public static String getLanguage(){
        return getDefaultSp().getString(Constants.LANGUAGE, "zh-rCN");
    }
}
