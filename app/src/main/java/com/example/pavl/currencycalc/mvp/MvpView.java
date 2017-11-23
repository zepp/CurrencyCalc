package com.example.pavl.currencycalc.mvp;

import android.content.Context;

public interface MvpView<S extends MvpState> {
    void onStateChanged(S state);

    boolean isReadyToHandle();

    Context getContext();
}
