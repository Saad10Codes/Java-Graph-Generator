package com.cs.uwindsor.group.assignment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingManager {
	private static String SETTING_FILE = "User Preferences";
	
	public static String SETTING_USER_PROVINCE = "province";
	
	public static String SETTING_USER_HEIGHT = "height";
	public static String SETTING_USER_WEIGHT = "weight";
	
	private SharedPreferences appSharedPrefs;
    private Editor prefsEditor;

    public SettingManager(Context context)
    {
        this.appSharedPrefs = context.getSharedPreferences(SETTING_FILE, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public String getStringPref(String pref) {
        return appSharedPrefs.getString(pref, "");
    }

    public void saveStringPref(String pref, String value) {
        prefsEditor.putString(pref, value);
        prefsEditor.commit();
    }
}
