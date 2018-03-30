package com.example.pavl.currencycalc;

import com.example.pavl.currencycalc.background.NetworkHandler;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkHandler.getInstance(getApplicationContext());
    }
}
