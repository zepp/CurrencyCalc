package com.example.pavl.currencycalc.domain;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.Log;

import com.example.pavl.currencycalc.R;
import com.example.pavl.currencycalc.model.CurrencyList;
import com.example.pavl.currencycalc.model.CustomMatcher;
import com.example.pavl.currencycalc.network.NetworkHandler;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class Controller {
    private final static int REQUEST_CODE = 0;
    private final static String TAG = Controller.class.getSimpleName();
    private static volatile Controller controller;
    private final Context context;
    private final AlarmManager alarmManager;
    private final NetworkHandler handler;
    private final Resources resources;
    private final Map<String, Drawable> flags;

    private Controller(Context context) {
        this.context = context;
        this.flags = new HashMap<>();
        this.resources = context.getResources();
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.handler = NetworkHandler.getInstance(context);
    }

    public static Controller getInstance(Context context) {
        if (controller == null) {
            synchronized (Controller.class) {
                if (controller == null) {
                    controller = new Controller(context);
                }
            }
        }
        return controller;
    }

    public void fetch() {
        Log.d(TAG, "fetching data");
        handler.fetch();
    }

    public void schedule(Intent intent, int hours) {
        long mSec = hours * 60 * 60 * 1000;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + mSec, mSec, pendingIntent);
        Log.d(TAG, intent.getComponent() + " is scheduled");
    }

    public CurrencyList getCurrencies() {
        CurrencyList list = new CurrencyList();
        try {
            File file = NetworkHandler.getFile(context);
            if (file.exists()) {
                Serializer serializer = new Persister(new CustomMatcher());
                list = serializer.read(CurrencyList.class, file);
                Log.d(TAG, "data is loaded");
            } else {
                Log.w(TAG, "data has not been fetched yet");
            }
        } catch (Exception e) {
            Log.e(TAG, "error: " + e.getMessage());
        }
        return list;
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
}
