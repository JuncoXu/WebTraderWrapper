package com.fortex.common.utills;

import android.app.Application;
import android.content.Context;

/**
 * Created by Junco.Xu on 2022/5/23 12:30
 */

public class AppUtils {
    static Application application; //ApplicationContext
    static boolean isDebuggable;

    public static void init(Application appCxt, boolean debuggable) {
        application = appCxt;
        isDebuggable = debuggable;
        LogUtils.setDebugMode(debuggable);
        LogUtils.SaveLogs(debuggable);
    }

    /**
     * get Applicaton context
     *
     * @return mContext
     */
    public static Context getContext() {
        return application.getApplicationContext();
    }

    public static boolean getIsDebuggable() { return isDebuggable; }

    public static CharSequence getPackageName() {
        return getContext().getPackageName();
    }

    public static String getPackageNameTail() {
        String packageName = getPackageName().toString();
        return packageName.substring(packageName.lastIndexOf(".") + 1);
    }
}
