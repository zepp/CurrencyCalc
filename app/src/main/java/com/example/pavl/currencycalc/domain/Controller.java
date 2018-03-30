package com.example.pavl.currencycalc.domain;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.example.pavl.currencycalc.network.NetworkHandler;

public final class Controller {
    private final static int REQUEST_CODE = 0;
    private final static String TAG = Controller.class.getSimpleName();
    private static volatile Controller controller;
    private final Context context;
    private final AlarmManager alarmManager;
    private final NetworkHandler handler;

    private Controller(Context context) {
        this.context = context;
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
}
