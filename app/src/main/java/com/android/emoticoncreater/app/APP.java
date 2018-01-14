package com.android.emoticoncreater.app;

import android.app.Application;

/**
 * application
 */

public class APP extends Application {

    private static APP mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    public static APP get(){
        return mApp;
    }
}
