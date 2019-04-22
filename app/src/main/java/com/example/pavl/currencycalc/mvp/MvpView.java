package com.example.pavl.currencycalc.mvp;

import android.arch.lifecycle.Lifecycle;

// интерфейс описывающий представление
public interface MvpView<P extends MvpPresenter<S>, S extends MvpState> {
    // вызывается при изменение состояния
    void onStateChanged(S state);

    // вызывается презентером, когда есть новое состояние
    void handleNewState(S state);

    // вызывается при необходимости создать представителя
    P onInitPresenter(MvpPresenterManager manager);

    // завершает работу представления
    void finish();
}
