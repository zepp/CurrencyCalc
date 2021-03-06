/*
 * Copyright (c) 2019 Pavel A. Sokolov
 */

package com.currencycalc;

import com.currencycalc.domain.Controller;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Controller.getInstance(getApplicationContext()).initialize();
    }
}
