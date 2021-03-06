package com.example.viktoria.reminderexample.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.example.viktoria.reminderexample.R;

import java.util.Locale;

/**
 * Is extension of Application class. Handles locale changes.
 */
public class MyApplication extends Application {
    private String[] lang_array;
    private Locale locale = null;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //get value of lang saved in PrefFragment
        String newLocaleValue = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.langPrefKey), lang_array[0]);
        locale = new Locale(newLocaleValue);
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources()
                .getConfiguration();
        if (!config.locale.equals(locale)) {
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, null);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lang_array = getResources().getStringArray(R.array.lang_to_load_array);
        //get value of lang saved in PrefFragment
        String newLocaleValue = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.langPrefKey), lang_array[0]);
        locale = new Locale(newLocaleValue);
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources()
                .getConfiguration();
        if (!config.locale.equals(locale)) {
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, null);
        }
    }

    public static boolean isConnected(Context mContext) {
        ConnectivityManager connMgr = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

}
