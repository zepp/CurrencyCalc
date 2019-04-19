package com.example.pavl.currencycalc.domain;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.pavl.currencycalc.model.CurrencyList;
import com.example.pavl.currencycalc.model.CustomMatcher;
import com.example.pavl.currencycalc.network.NetworkHandler;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Controller {
    private final static int REQUEST_CODE = 0;
    private final static String TAG = Controller.class.getSimpleName();
    private static volatile Controller controller;
    private final ExecutorService executor;
    private final Context context;
    private final AlarmManager alarmManager;
    private final ConnectivityManager connectivityManager;
    private final NetworkHandler networkHandler;

    private final AppState state;

    private Controller(Context context) {
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.networkHandler = new NetworkHandler(context);
        this.state = AppState.getInstance(context);
    }

    public static Controller getInstance(Context context) {
        if (controller == null) {
            synchronized (Controller.class) {
                if (controller == null) {
                    controller = new Controller(context.getApplicationContext());
                }
            }
        }
        return controller;
    }

    public void fetch(Consumer<CurrencyList> onDataReady, Consumer<Throwable> onError) {
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null) {
            executor.submit(() -> {
                try {
                    onFetch();
                    onDataReady.accept(onParse());
                } catch (Throwable e) {
                    onError.accept(e);
                }
            });
        } else if (state.getFileName().exists()) {
            executor.submit(() -> {
                try {
                    onDataReady.accept(onParse());
                } catch (Throwable e) {
                    onError.accept(e);
                }
            });
        } else {
            onError.accept(new RuntimeException("cached data is not available"));
        }
    }

    public void init() {
        Intent intent = SystemBroadcastReceiver.getFetchIntent();
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                state.getFetchTime().getTime() + state.getFetchInterval(),
                state.getFetchInterval(),
                PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        Log.d(TAG, intent.getComponent() + " is scheduled");
    }

    private void onFetch() throws IOException {
        Log.d(TAG, "fetching data");
        state.setFileName(networkHandler.fetch());
        state.setFetchTime(new Date());
    }

    private CurrencyList onParse() throws Exception {
        CurrencyList list;
        Log.d(TAG, "parsing data");
        File file = state.getFileName();
        Serializer serializer = new Persister(new CustomMatcher());
        list = serializer.read(CurrencyList.class, file);
        return list;
    }

    public interface Consumer<T> {
        void accept(T t);
    }
}
