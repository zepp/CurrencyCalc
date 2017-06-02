package com.example.pavl.currencycalc;

import com.example.pavl.currencycalc.background.Scheduler;
import com.example.pavl.currencycalc.background.Service;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Scheduler.getInstance(this).schedule(Service.getIntent(this), 24);
    }
}
