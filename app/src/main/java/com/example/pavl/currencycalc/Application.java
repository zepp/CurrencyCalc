package com.example.pavl.currencycalc;

import com.example.pavl.currencycalc.domain.Controller;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Controller.getInstance(getApplicationContext()).initialize();
    }
}
