package com.example.pavl.currencycalc.mvp;

import android.arch.lifecycle.Lifecycle;

// интерфейс описывающий представление
public interface MvpView<P extends MvpPresenter<S>, S extends MvpState> {
    // вызывается при изменение состояния
    void onStateChanged(S state);

    // вызывается при необходимости создать представителя
    P onInitPresenter(MvpPresenterManager manager);

    // возвращает объект, описывающий жизненный цикл представления
    Lifecycle getLifecycle();

    // завершает работу представления
    void finish();
}
