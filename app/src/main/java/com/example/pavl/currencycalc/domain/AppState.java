package com.example.pavl.currencycalc.domain;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pavl.currencycalc.R;

import java.util.Date;

public final class AppState {
    private final static String FETCH_TIME = "fetch-time";
    private final static String FETCH_INTERVAL = "fetch-interval";
    private final static String FILE_NAME = "file-name";
    private volatile static AppState state;
    private final Context context;
    private final SharedPreferences preferences;
    private final onPreferencesChangedListener listener;
    private OnChangedListener onChangedListener;

    AppState(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(AppState.class.getSimpleName(), Context.MODE_PRIVATE);
        this.listener = new onPreferencesChangedListener();
        this.preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public static AppState getInstance(Context context) {
        if (state == null) {
            synchronized (AppState.class) {
                if (state == null) {
                    state = new AppState(context);
                }
            }
        }
        return state;
    }

    public void setOnChangedListener(OnChangedListener onChangedListener) {
        this.onChangedListener = onChangedListener;
    }

    public Date getFetchTime() {
        return new Date(preferences.getLong(FETCH_TIME, 0));
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

    public String getFileName() {
        return preferences.getString(FILE_NAME, null);
    }

    public void setFileName(String value) {
        preferences.edit().putString(FILE_NAME, value).apply();
    }

    public interface OnChangedListener {
        void onFetchTimeChanged(Date time);

        void onFetchIntervalChanged(long value);

        void onFileNameChanged(String value);
    }

    private class onPreferencesChangedListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            if (onChangedListener == null) {
                return;
            }
            switch (s) {
                case FETCH_TIME:
                    onChangedListener.onFetchTimeChanged(getFetchTime());
                    break;
                case FETCH_INTERVAL:
                    onChangedListener.onFetchIntervalChanged(getFetchInterval());
                    break;
                case FILE_NAME:
                    onChangedListener.onFileNameChanged(getFileName());
                    break;
            }
        }
    }
}
