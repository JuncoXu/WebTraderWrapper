package com.fortex.common.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fortex.common.constant.NetConstants;
import com.fortex.common.utills.AppUtils;

/**
 * Created by Junco.Xu on 2022/5/23 12:09
 */

public class Session {
	private static final Session CUR;

	// DefaultSharedPreferences
	private static final SharedPreferences GLOBALS_SHARED_PREFERENCE;

	// sessionSharedPreference name prefix: ${packagename}_preferences_
	private static final String SESSION_SHAREDPREFERENCE_PREFIX;

	static {
		Context appContext = AppUtils.getContext();
		GLOBALS_SHARED_PREFERENCE = PreferenceManager.getDefaultSharedPreferences(appContext);
		SESSION_SHAREDPREFERENCE_PREFIX = appContext.getPackageName() + "_preferences_";

		int did = getGlobalInt(NetConstants.DID, 0);
		CUR = new Session(did);
	}

	private Session(int userId) {
		initializeHolder(userId);
	}

	private void initializeHolder(int userId) {
		sessionHolder = new Holder(userId);
	}

	public static Session cur() {
		return CUR;
	}

	public static int getGlobalInt(String key, int defValue) {
		return GLOBALS_SHARED_PREFERENCE.getInt(key, defValue);
	}

	public static void putGlobalInt(String key, int value) {
		SharedPreferences.Editor editor = GLOBALS_SHARED_PREFERENCE.edit();
		editor.putInt(key, value).apply();
	}

	public static String getSessionString(String key, String defValue) {
		return cur().sessionHolder.getString(key, defValue);
	}

	public static boolean putSessionString(String key, String value) {
		return cur().sessionHolder.putString(key, value);
	}

	private Holder sessionHolder;
	public static class Holder {
		private SharedPreferences sessionSharedPreference;

		private Holder(int userId) {
			initSessionSharedPreference(userId);
		}

		private void initSessionSharedPreference(int userId) {
			Context appContext = AppUtils.getContext();
			String spName = SESSION_SHAREDPREFERENCE_PREFIX + userId;
			sessionSharedPreference = appContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
		}

		public String getString(String key, String defValue) {
			if (sessionSharedPreference != null) {
				return sessionSharedPreference.getString(key, defValue);
			}

			return defValue;
		}

		public boolean putString(String key, String value) {
			if (sessionSharedPreference != null) {
				SharedPreferences.Editor editor = sessionSharedPreference.edit();
				editor.putString(key, value);
				editor.apply();
				return true;
			}
			return false;
		}
	}
}
