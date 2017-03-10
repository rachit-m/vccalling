package com.labs.poziom.whereabouts;

/**
 * Created by Rachit on 3/1/2017.
 */

import android.app.Application;
import android.content.Context;

import org.acra.*;
import org.acra.annotation.*;

import static org.acra.ReportField.ANDROID_VERSION;
import static org.acra.ReportField.CUSTOM_DATA;
import static org.acra.ReportField.LOGCAT;
import static org.acra.ReportField.PHONE_MODEL;
import static org.acra.ReportField.STACK_TRACE;

@ReportsCrashes(
        formUri = "http://52.27.54.85/blog/rad_app/testapi.php",
        customReportContent = { ANDROID_VERSION, PHONE_MODEL, CUSTOM_DATA, STACK_TRACE},
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text
)
/*
@ReportsCrashes(mailTo = "rachit.iitkgp@gmail.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.app_name)
        */

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
    }
}