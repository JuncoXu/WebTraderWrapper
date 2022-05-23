package com.fortex.common.utills;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 *  Created by Junco.Xu on 2022/5/23 12:34
 */

public class LogUtils {

    private static final String TAG = "FORTEX";
    private static final String DIVIDER = "================== Gorgeous dividing line ================";
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(TimeDateUtils.DATE_PATTERN_YYYY_MM_DD_HHMMSS, Locale.CHINA);

    private static String sDeviceInfo;

    static {
        init();
    }

    private static void init() {
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static void d(Object object) {
        Logger.d(object);
    }

    public static void d(String message, Object... args) {
        Logger.d(message, args);
    }

    public static void e(Throwable throwable) {
        if (throwable == null) {
            return;
        }
        e(throwable.getMessage(), throwable);
    }

    public static void e(String message, Object... args) {
        Logger.e(message, args);
    }

    public static void e(String message, Throwable throwable, Object... args) {
        Logger.e(throwable, message, args);
    }

    public static void w(Object object) {
        Logger.w(obj2Str(object));
    }

    public static void w(String message, Object... args) {
        Logger.w(message, args);
    }

    public static void i(Object object) {
        Logger.i(obj2Str(object));
    }

    public static void i(String message, Object... args) {
        Logger.i(message, args);
    }

    public static void v(Object object) {
        Logger.v(obj2Str(object));
    }

    public static void v(String message, Object... args) {
        Logger.v(message, args);
    }

    public static void wtf(String message, Object... args) {
        Logger.wtf(message, args);
    }

    public static void json(String json) {
        Logger.json(json);
    }

    public static void xml(String xml) {
        Logger.xml(xml);
    }

    public static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            sb.append("\nkey:").append(key).append(", value:").append(bundle.getString(key));
        }
        return sb.toString();
    }

    private static String obj2Str(Object object) {
        if (object == null) {
            return null;
        }

        String message;
        if (object.getClass().isArray()) {
            message = Arrays.deepToString((Object[]) object);
        } else {
            message = object.toString();
        }
        return message;
    }

    public static void e(Throwable throwable, boolean toFile) {
        if (throwable == null) {
            return;
        }

        e(throwable);

        if (toFile) {
            printSendError(throwable);
        }
    }

    private static void printSendError(Throwable throwable) {
        if (throwable == null) {
            return;
        }

        i("========================The program crashed！========================");

        try {
            PrintStream stream = getFilePrintStream();
            if (stream == null) {
                return;
            }

            stream.println(getDeviceInfo());

            stream.println(DIVIDER);

            stream.println("TIME: " + FORMAT.format(new Date()));

            throwable.printStackTrace(stream);

            stream.close();
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    private static PrintStream getFilePrintStream() throws FileNotFoundException {
        String filename = "error" + FORMAT.format(new Date()) + ".log";

        File dir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + File.separator
                + CrashReportHandler.CRASH_DIR
                + File.separator);

        String crashDirName = CrashReportHandler.getInstance().crashDirName;
        crashDirName = crashDirName == null ? AppUtils.getPackageNameTail() : crashDirName;

        File crashDir = new File(dir, crashDirName);
        if (!crashDir.exists()) {
            crashDir.mkdirs();
        }
        if (crashDir.exists()) {
            return new PrintStream(new File(crashDir, filename));
        } else {
            return null;
        }
    }

    private static String getDeviceInfo() {
        if (sDeviceInfo == null) {
            Context context = AppUtils.getContext();
            StringBuilder builder = new StringBuilder();
            try {
                PackageManager pm = context.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
                // 得到该应用的信息，即主Activity
                if (pi != null) {
                    String versionName = pi.versionName == null ? "null" : pi.versionName;
                    String versionCode = pi.versionCode + "";
                    builder.append("versionName: ").append(versionName).append("\n");
                    builder.append("versionCode: ").append(versionCode).append("\n");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e(e);
            }

            try {
                Class<?> clazz = Class.forName("android.os.Build");
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    builder.append(field.getName()).append(": ").append(field.get(clazz)).append("\n");
                }
            } catch (ClassNotFoundException | IllegalAccessException e) {
                e(e);
            }

            sDeviceInfo = builder.toString();
        }

        return sDeviceInfo;
    }

    public static void setDebugMode(boolean debuggable) {
        Logger.addLogAdapter(new AndroidLogAdapter() {
            // If you want to disable/hide logs for output, override isLoggable method. true will print the log message, false will ignore it.
            @Override public boolean isLoggable(int priority, String tag) {
                // Log adapter checks whether the log should be printed or not by checking this.
                return debuggable;
            }
        });
    }

    public static void SaveLogs(boolean debuggable) {
        //Save logs to the file
//        FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder().tag("custom").build();
//        Logger.addLogAdapter(new DiskLogAdapter(formatStrategy));
    }
}
