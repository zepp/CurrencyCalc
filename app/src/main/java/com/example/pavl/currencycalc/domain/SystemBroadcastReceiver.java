package com.example.pavl.currencycalc.domain;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SystemBroadcastReceiver extends BroadcastReceiver {
    private final static String ACTION_FETCH = "com.example.pavl.currencycalc.ACTION_FETCH";

    public static Intent getFetchIntent() {
        return new Intent(ACTION_FETCH);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_FETCH)) {
            Controller.getInstance(context).fetch(currencyList -> {},
                    throwable -> Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show());
        } else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            // do nothing
        }
    }
}
