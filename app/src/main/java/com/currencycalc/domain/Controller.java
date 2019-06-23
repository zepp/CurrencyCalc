/*
 * Copyright (c) 2019 Pavel A. Sokolov
 */

package com.currencycalc.domain;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.currencycalc.common.AsyncConsumer;
import com.currencycalc.common.AsyncOperation;
import com.currencycalc.model.CurrencyList;
import com.currencycalc.model.CustomMatcher;
import com.currencycalc.network.NetworkHandler;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.concurrent.ExecutorService;

public final class Controller {
    private final static int REQUEST_CODE = 0;
    private final static String TAG = Controller.class.getSimpleName();
    private static volatile Controller instance;
    private final ExecutorService executor;
    private final Context context;
    private final AlarmManager alarmManager;
    private final ConnectivityManager connectivityManager;
    private final NetworkHandler networkHandler;
    private final AppState state;
    private File fileName;

    private Controller(Context context) {
        this.context = context;
        this.executor = Executor.getExecutor();
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.networkHandler = new NetworkHandler(context);
        this.state = new AppState(context);
    }

    public static Controller getInstance(Context context) {
        if (instance == null) {
            synchronized (Controller.class) {
                if (instance == null) {
                    instance = new Controller(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public void asyncFetch(AsyncConsumer<CurrencyList> consumer) {
        File file = state.getFileName();
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null) {
            networkHandler.asyncFetch((newFile, e) ->
                    executor.execute(new AsyncOperation<>(() -> {
                        state.setFileName(newFile);
                        state.setFetchTime(System.currentTimeMillis());
                        return parse(newFile);
                    }, consumer)));
        } else if (file.exists()) {
            executor.execute(new AsyncOperation<>(() -> parse(file), consumer));
        } else {
            consumer.accept(null, new RuntimeException("cached data is not available"));
        }
    }

    public CurrencyList load() throws Exception {
        File file = state.getFileName();
        if (file.exists()) {
            return parse(file);
        } else {
            throw new Exception("cached data is not available");
        }
    }

    public void initialize() {
        schedule();
        fileName = state.getFileName();
        state.setOnChangedListener(new AppState.OnChangedListener() {
            @Override
            public void onFetchTimeChanged(long value) {
            }

            @Override
            public void onFetchIntervalChanged(long value) {
                schedule();
            }

            @Override
            public void onFileNameChanged(File name) {
                if (fileName.exists()) {
                    fileName.delete();
                }
                fileName = name;
            }
        });
    }

    private void schedule() {
        Intent intent = SystemBroadcastReceiver.getFetchIntent();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC,
                state.getFetchTime() + state.getFetchInterval(),
                state.getFetchInterval(), pendingIntent);
        Log.d(TAG, intent + " is scheduled");
    }

    private CurrencyList parse(File file) throws Exception {
        Log.d(TAG, "parsing data");
        Serializer serializer = new Persister(new CustomMatcher());
        return serializer.read(CurrencyList.class, file);
    }
}
