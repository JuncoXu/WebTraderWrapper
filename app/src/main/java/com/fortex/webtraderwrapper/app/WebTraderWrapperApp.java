package com.fortex.webtraderwrapper.app;

import android.app.Application;

import com.fortex.common.utills.AppUtils;
import com.fortex.common.utills.CrashReportHandler;
import com.fortex.webtraderwrapper.BuildConfig;

/**
 * Created by Junco.Xu on 2022/5/23 13:46
 */

public class WebTraderWrapperApp extends Application {

    public WebTraderWrapperApp() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AppUtils.init(this, BuildConfig.DEBUG);
        CrashReportHandler crashReportHandler = CrashReportHandler.getInstance();
        crashReportHandler.init(this);
        crashReportHandler.setCrashCallback(tr -> false);

        com.fortex.common.utills.LogUtils.i("The current timestamp is: " + System.currentTimeMillis());
    }
}
