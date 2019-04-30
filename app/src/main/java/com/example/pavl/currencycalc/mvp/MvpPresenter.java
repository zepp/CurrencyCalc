package com.example.pavl.currencycalc.mvp;

import android.content.Intent;
import android.support.annotation.IdRes;

public interface MvpPresenter<S extends MvpState> {
    void execute(Runnable method);
    void attach(MvpView view);
    void detach(MvpView view);
    boolean isDetached();
    void commit();
    void finish();
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onViewClicked(@IdRes int viewId);
    void onItemSelected(@IdRes int viewId, int position, long id);
}
