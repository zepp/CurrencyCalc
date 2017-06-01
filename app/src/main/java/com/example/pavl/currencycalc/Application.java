package com.example.pavl.currencycalc;

import com.example.pavl.currencycalc.background.Scheduler;
import com.example.pavl.currencycalc.background.Service;

import android.content.Intent;

public class Application extends android.app.Application {
    private Scheduler scheduler;

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = Service.getIntent(this);
        scheduler = Scheduler.getInstance(this);
        scheduler.schedule(intent, 24);
        if (!BuildConfig.DEBUG) {
            startService(intent);
        }
    }
}
