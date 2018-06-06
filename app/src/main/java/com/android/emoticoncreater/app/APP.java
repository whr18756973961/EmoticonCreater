package com.android.emoticoncreater.app;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * application
 */

public class APP extends Application {

    private static APP INStANCE;

    public APP() {
        INStANCE = this;
    }

    public static APP getInstance() {
        if (INStANCE == null) {
            synchronized (APP.class) {
                if (INStANCE == null) {
                    INStANCE = new APP();
                }
            }
        }
        return INStANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        CrashReport.initCrashReport(getApplicationContext(), "06b4b6de0f", false);
    }
}
