/*
 * Copyright (c) 2019 Pavel A. Sokolov
 */

package com.currencycalc.domain;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.util.concurrent.ExecutorService;

public final class AppState {
    private final static String FETCH_TIME = "fetch-time";
    private final static String FETCH_INTERVAL = "fetch-interval";
    private final static String FILE_NAME = "file-name";
    private final SharedPreferences preferences;
    private final onPreferencesChangedListener listener;
    private final ExecutorService executor;
    private volatile OnChangedListener onChangedListener;

    AppState(Context context) {
        this.preferences = context.getSharedPreferences(AppState.class.getSimpleName(), Context.MODE_PRIVATE);
        this.listener = new onPreferencesChangedListener();
        this.executor = Executor.getExecutor();
        this.preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void setOnChangedListener(OnChangedListener onChangedListener) {
        this.onChangedListener = onChangedListener;
    }

    public long getFetchTime() {
        return preferences.getLong(FETCH_TIME, System.currentTimeMillis() - getFetchInterval());
    }

    public void setFetchTime(long value) {
        preferences.edit().putLong(FETCH_TIME, value).commit();
    }

    public long getFetchInterval() {
        return preferences.getLong(FETCH_INTERVAL, AlarmManager.INTERVAL_DAY);
    }

    public void setFetchInterval(long value) {
        preferences.edit().putLong(FETCH_INTERVAL, value).commit();
    }

    public File getFileName() {
        return new File(preferences.getString(FILE_NAME, ""));
    }

    public void setFileName(File file) {
        preferences.edit().putString(FILE_NAME, file.getAbsolutePath()).commit();
    }

    public interface OnChangedListener {
        void onFetchTimeChanged(long value);

        void onFetchIntervalChanged(long value);

        void onFileNameChanged(File name);
    }

    private class onPreferencesChangedListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            if (onChangedListener != null) {
                if (s.equals(FETCH_TIME)) {
                    executor.execute(() -> onChangedListener.onFetchTimeChanged(getFetchTime()));
                } else if (s.equals(FETCH_INTERVAL)) {
                    executor.execute(() -> onChangedListener.onFetchIntervalChanged(getFetchInterval()));
                } else if (s.equals(FILE_NAME)) {
                    executor.execute(() -> onChangedListener.onFileNameChanged(getFileName()));
                }
            }
        }
    }
}
