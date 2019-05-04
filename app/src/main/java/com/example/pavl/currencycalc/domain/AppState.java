package com.example.pavl.currencycalc.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.example.pavl.currencycalc.R;

import java.io.File;
import java.util.Date;

public final class AppState {
    private final static String FETCH_TIME = "fetch-time";
    private final static String FETCH_INTERVAL = "fetch-interval";
    private final static String FILE_NAME = "file-name";
    private volatile static AppState instance;
    private final Context context;
    private final SharedPreferences preferences;
    private final onPreferencesChangedListener listener;
    private volatile OnChangedListener onChangedListener;

    AppState(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(AppState.class.getSimpleName(), Context.MODE_PRIVATE);
        this.listener = new onPreferencesChangedListener();
        this.preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public static AppState getInstance(Context context) {
        if (instance == null) {
            synchronized (AppState.class) {
                if (instance == null) {
                    instance = new AppState(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public void setOnChangedListener(OnChangedListener onChangedListener) {
        this.onChangedListener = onChangedListener;
    }

    public Date getFetchTime() {
        return new Date(preferences.getLong(FETCH_TIME, SystemClock.elapsedRealtime()));
    }

    public void setFetchTime(Date date) {
        preferences.edit().putLong(FETCH_TIME, date.getTime()).apply();
    }

    public long getFetchInterval() {
        return preferences.getLong(FETCH_INTERVAL, context.getResources().getInteger(R.integer.fetch_interval));
    }

    public void setFetchInterval(long value) {
        preferences.edit().putLong(FETCH_INTERVAL, value).apply();
    }

    public File getFileName() {
        return new File(preferences.getString(FILE_NAME, ""));
    }

    public void setFileName(File file) {
        preferences.edit().putString(FILE_NAME, file.getAbsolutePath()).apply();
    }

    public interface OnChangedListener {
        void onFetchTimeChanged(Date time);

        void onFetchIntervalChanged(long value);

        void onFileNameChanged(File name);
    }

    private class onPreferencesChangedListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            if (onChangedListener != null) {
                if (s.equals(FETCH_TIME)) {
                    onChangedListener.onFetchTimeChanged(getFetchTime());
                } else if (s.equals(FETCH_INTERVAL)) {
                    onChangedListener.onFetchIntervalChanged(getFetchInterval());
                } else if (s.equals(FILE_NAME)) {
                    onChangedListener.onFileNameChanged(getFileName());
                }
            }
        }
    }
}
