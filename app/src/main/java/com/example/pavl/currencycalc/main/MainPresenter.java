package com.example.pavl.currencycalc.main;

import android.content.Context;

import com.example.pavl.currencycalc.domain.Controller;
import com.example.pavl.currencycalc.model.CurrencyList;
import com.example.pavl.currencycalc.mvp.MvpPresenter;

class MainPresenter extends MvpPresenter<MainState> {
    private Controller controller;

    public MainPresenter(Context applicationContext) {
        super(applicationContext);
        controller = Controller.getInstance(applicationContext);
    }

    @Override
    public void onStart() {
        controller.setListener(new OnCurrencyListUpdated());
        state.setList(controller.getCurrencies());
    }

    @Override
    protected void onStop() {
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

    private class OnCurrencyListUpdated implements Controller.CurrencyListListener {
        @Override
        public void onCurrencyListUpdated(CurrencyList list) {
            state.setList(list);
            commit();
        }

        @Override
        public void onError(Throwable e) {
            state.setMessage(e.getMessage());
            commit();
        }
    }
}
