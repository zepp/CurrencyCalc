package com.example.pavl.currencycalc.mvp;

import android.content.Intent;

public interface MvpPresenter<S extends MvpState> {
    void attach(MvpView view);
    void detach(MvpView view);
    boolean isDetached();
    void commit();
    void finish();
    boolean onActivityResult(int requestCode, int resultCode, Intent data);
}
