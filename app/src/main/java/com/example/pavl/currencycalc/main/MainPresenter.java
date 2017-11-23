package com.example.pavl.currencycalc.main;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.pavl.currencycalc.background.EventHandler;
import com.example.pavl.currencycalc.background.Service;
import com.example.pavl.currencycalc.model.CurrencyList;
import com.example.pavl.currencycalc.model.CustomMatcher;
import com.example.pavl.currencycalc.mvp.MvpPresenter;
import com.example.pavl.currencycalc.mvp.MvpView;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

class MainPresenter extends MvpPresenter<MainState> {
    private final EventHandler handler;
    private final EventListener listener;

    MainPresenter(MvpView<MainState> view) {
        super(view, new Handler(Looper.getMainLooper()));
        handler = EventHandler.getInstance();
        listener = new EventListener();
    }

    @Override
    public void onStart() {
        handler.register(listener);
        state.setList(loadCurrencies());
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

    private CurrencyList loadCurrencies() {
        CurrencyList list = null;
        try {
            File file = Service.getFile(view.getContext());
            if (file.exists()) {
                Serializer serializer = new Persister(new CustomMatcher());
                list = serializer.read(CurrencyList.class, file);
                Log.d(tag, "data updated");
            } else {
                Log.w(tag, "file does not exist");
            }
        } catch (Exception e) {
            state.setMessage("error: " + e.getMessage());
        }
        return list;
    }

    private class EventListener implements EventHandler.Listener {
        @Override
        public void onError(final Throwable e) {
            state.setMessage(e.getMessage());
            commit();
        }

        @Override
        public void onDataUpdated() {
            state.setList(loadCurrencies());
            commit();
        }
    }
}
