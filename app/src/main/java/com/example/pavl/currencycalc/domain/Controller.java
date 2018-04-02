package com.example.pavl.currencycalc.domain;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.example.pavl.currencycalc.R;
import com.example.pavl.currencycalc.model.Currency;
import com.example.pavl.currencycalc.model.CurrencyList;
import com.example.pavl.currencycalc.model.CustomMatcher;
import com.example.pavl.currencycalc.network.NetworkHandler;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class Controller extends HandlerThread {
    private final static int REQUEST_CODE = 0;
    private final static String TAG = Controller.class.getSimpleName();
    private static volatile Controller controller;
    private final Context context;
    private final AlarmManager alarmManager;
    private final ConnectivityManager connectivityManager;
    private final NetworkHandler networkHandler;
    private final Resources resources;
    private final Map<String, Drawable> flags;
    private final AppState state;
    private CurrencyList list;
    private Handler handler;
    private CurrencyListListener listener;

    private Controller(Context context) {
        super(Controller.class.getSimpleName());
        this.context = context;
        this.flags = Collections.synchronizedMap(new HashMap<String, Drawable>());
        this.resources = context.getResources();
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.networkHandler = new NetworkHandler(context);
        this.state = AppState.getInstance(context);
        this.list = new CurrencyList();
    }

    public static Controller getInstance(Context context) {
        if (controller == null) {
            synchronized (Controller.class) {
                if (controller == null) {
                    controller = new Controller(context);
                    controller.start();
                }
            }
        }
        return controller;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        handler = new Handler(getLooper());
    }

    public void setListener(CurrencyListListener listener) {
        this.listener = listener;
    }

    public void fetch() {
        if (connectivityManager.isDefaultNetworkActive()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onFetch();
                }
            });
        }
    }

    public void init() {
        Intent intent = SystemBroadcastReceiver.getFetchIntent();
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                state.getFetchTime().getTime() + state.getFetchInterval(),
                state.getFetchInterval(),
                PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        Log.d(TAG, intent.getComponent() + " is scheduled");
        if (state.getFileName().exists()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onParse();
                }
            });
        } else if (connectivityManager.isDefaultNetworkActive()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onFetch();
                }
            });
        }
    }

    public CurrencyList getCurrencies() {
        return list;
    }

    private void onFetch() {
        try {
            Log.d(TAG, "fetching data");
            state.setFileName(networkHandler.fetch());
            state.setFetchTime(new Date());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onParse();
                }
            });
        } catch (Throwable e) {
            Log.e(TAG, "error: " + e.getMessage());
            if (listener != null) {
                listener.onError(e);
            }
        }
    }

    private void onParse() {
        try {
            File file = state.getFileName();
            if (!file.exists()) {
                Log.w(TAG, "data has not been fetched yet");
                return;
            }
            Log.d(TAG, "parsing data");
            Serializer serializer = new Persister(new CustomMatcher());
            list = serializer.read(CurrencyList.class, file);
            Log.d(TAG, "caching flags");
            for (Currency currency : list.getCurrencies()) {
                getFlagDrawable(currency.getCharCode());
            }
            if (listener != null) {
                listener.onCurrencyListUpdated(list);
            }
        } catch (Throwable e) {
            Log.e(TAG, "error: " + e.getMessage());
            if (listener != null) {
                listener.onError(e);
            }
        }
    }

    public Drawable getFlagDrawable(String charCode) {
        String name = "ic_" + charCode.toLowerCase();
        Drawable flag = flags.get(name);
        if (flag == null) {
            int id = resources.getIdentifier(name, "drawable", context.getPackageName());
            if (id == 0) {
                flag = resources.getDrawable(R.drawable.flag_unknown);
            } else {
                flag = resources.getDrawable(id);
            }
            flags.put(name, flag);
        }
        return flag;
    }

    public interface CurrencyListListener {
        void onCurrencyListUpdated(CurrencyList list);

        void onError(Throwable e);
    }
}
