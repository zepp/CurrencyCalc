/*
 * Copyright (c) 2019 Pavel A. Sokolov
 */

package com.currencycalc.mvp;

import android.content.Intent;
import android.support.annotation.IdRes;

public interface MvpPresenter<S extends MvpState> {
    void attach(MvpView<?, S> view);

    void detach(MvpView<?, S> view);

    boolean isDetached();

    void commit();

    void finish();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onViewClicked(@IdRes int viewId);

    void onOptionsItemSelected(@IdRes int itemId);

    void onItemSelected(@IdRes int viewId, Object item);
}
