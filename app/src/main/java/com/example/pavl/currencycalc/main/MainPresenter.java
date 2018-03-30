package com.example.pavl.currencycalc.main;

import android.content.Context;

import com.example.pavl.currencycalc.domain.Controller;
import com.example.pavl.currencycalc.mvp.MvpPresenter;
import com.example.pavl.currencycalc.network.EventHandler;

class MainPresenter extends MvpPresenter<MainState> {
    private final EventHandler handler;
    private final EventListener listener;

    public MainPresenter(Context applicationContext) {
        super(applicationContext);
        handler = EventHandler.getInstance();
        listener = new EventListener();
    }

    @Override
    public void onStart() {
        handler.register(listener);
        state.setList(Controller.getInstance(context).getCurrencies());
    }

    @Override
    public void onStop() {
        handler.unregister(listener);
    }

    void onOriginalCurrencyChanged(int position) {
        state.setOriginalPosition(position);
        if (state.isChanged()) {
            state.updateResult();
        }
        commit();
    }

    void onOriginalAmountChanged(String text) {
        double amount;
        try {
            amount = Double.valueOf(text);
        } catch (NumberFormatException e) {
            amount = -1;
        }
        state.setOriginalAmount(amount, text);
        if (state.isChanged()) {
            state.updateResult();
        }
        commit();
    }

    void onResultCurrencyChanged(int position) {
        state.setResultPosition(position);
        if (state.isChanged()) {
            state.updateResult();
        }
        commit();
    }

    void onCurrenciesSwap() {
        state.swapCurrencies();
        commit();
    }

    @Override
    public void commit() {
        super.commit();
        state.isError = false;
        state.isListChanged = false;
    }

    private class EventListener implements EventHandler.Listener {
        @Override
        public void onError(final Throwable e) {
            state.setMessage(e.getMessage());
            commit();
        }

        @Override
        public void onDataUpdated() {
            state.setList(Controller.getInstance(context).getCurrencies());
            commit();
        }
    }
}
