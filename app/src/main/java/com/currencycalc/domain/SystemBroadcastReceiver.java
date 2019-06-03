/*
 * Copyright (c) 2019 Pavel A. Sokolov
 */

package com.currencycalc.domain;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SystemBroadcastReceiver extends BroadcastReceiver {
    private final static String ACTION_FETCH = "com.currencycalc.ACTION_FETCH";

    public static Intent getFetchIntent() {
        return new Intent(ACTION_FETCH);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_FETCH)) {
            Controller.getInstance(context).asyncFetch();
        } else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            // do nothing
        }
    }
}
