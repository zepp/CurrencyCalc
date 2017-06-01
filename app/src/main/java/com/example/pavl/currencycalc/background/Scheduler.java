package com.example.pavl.currencycalc.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class Scheduler {
    private final static String TAG = Scheduler.class.getSimpleName();
    private final static int requestCode = 0;
    private static volatile Scheduler scheduler;
    private final Context context;
    private final AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private ComponentName componentName;

    private Scheduler(Context context){
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public static Scheduler getInstance(Context context) {
        if (scheduler == null) {
            synchronized (Scheduler.class) {
                if (scheduler == null) {
                    scheduler = new Scheduler(context);
                }
            }
        }
        return scheduler;
    }

    public void schedule(Intent intent, int hours) {
        long msec = hours * 60 * 60 * 1000;
        pendingIntent = PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + msec, msec, pendingIntent);
        componentName = intent.getComponent();
        Log.d(TAG, componentName.getClassName() + " is scheduled");
    }

    public void cancel() {
        alarmManager.cancel(pendingIntent);
        Log.d(TAG, componentName.getClassName() + " is canceled");
    }
}
