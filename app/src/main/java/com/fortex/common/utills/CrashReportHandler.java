package com.fortex.common.utills;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.TreeSet;

import androidx.annotation.NonNull;

/**
 * Created by Junco.Xu on 2022/5/23 12:42
 */

public class CrashReportHandler implements Thread.UncaughtExceptionHandler {
	private static CrashReportHandler INSTANCE = new CrashReportHandler();
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private String CRASH_REPORTER_EXTENSION = ".txt";
	String crashDirName = null;
	public static final String CRASH_DIR = "fortexCrash";
	private CrashCallback mCrashCallback;

	private CrashReportHandler() {
	}

	public static CrashReportHandler getInstance() {
		return INSTANCE;
	}

	public void setCrashDirName(String crashDirName) {
		this.crashDirName = crashDirName;
	}

	public void setCrashCallback(CrashCallback mCrashCallback) {
		this.mCrashCallback = mCrashCallback;
	}

	/**
	 * initialization
	 *
	 * @param context context
	 */
	public void init(Context context) {
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// Get the default uncaughtexception processor of the system
		Thread.setDefaultUncaughtExceptionHandler(this);// Set the crashhandler as the default processor of the program
	}

	/**
	 * When uncaughtexception occurs, it will be transferred to the overridden method for processing
	 */
	@Override
	public void uncaughtException(@NonNull Thread thread, @NonNull Throwable ex) {
		if (mCrashCallback != null) {
			mCrashCallback.onCrash(ex);
		}
		if (!handleException(ex) && mDefaultHandler != null) {
			//If the customized exception is not handled, let the system default exception handler handle it
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
//            // Exit program
//            ActivityManager.getInstance().finishAllActivity();
//            //Kill the process and exit completely
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
		}
	}

	/**
	 * Customize error handling, collect error information, send error report and other operations are completed here.
	 *
	 * @param ex Throwable
	 */
	public boolean handleException(Throwable ex) {
		if (ex == null)
			return false;
		if (AppUtils.isDebuggable) {
			new Thread() {
				public void run() {
					Looper.prepare();
					Toast.makeText(AppUtils.getContext(), "The program has a serious exception and has been output to " + CRASH_DIR + " folder", Toast.LENGTH_LONG).show();
					Looper.loop();
				}
			}.start();
		}
		LogUtils.e(ex, true);
		//Upload log
		return false;
	}

	private void sendReports(Context context) {
		String[] crFiles = getCrashReportFiles(context);
		if (crFiles != null && crFiles.length > 0) {
			TreeSet<String> sortedFiles = new TreeSet<>(Arrays.asList(crFiles));

			for (String fileName : sortedFiles) {
				File cr = new File(context.getFilesDir(), fileName);
				postReport(context);
				cr.delete();// Delete sent reports
			}
		}

	}

	/**
	 * Get error report file name
	 *
	 * @param ctx context
	 * @return .txt file return ture
	 */
	private String[] getCrashReportFiles(Context ctx) {
		return ctx.getFilesDir().list((dir, name) -> name.endsWith(CRASH_REPORTER_EXTENSION));
	}

	public void sendReportsWhenStart() {
		sendReports(AppUtils.getContext());
	}

	/**
	 * upload report
	 *
	 * @param context context
	 */
	private void postReport(Context context) {

	}

	public interface CrashCallback {
		boolean onCrash(Throwable tr);
	}
}
