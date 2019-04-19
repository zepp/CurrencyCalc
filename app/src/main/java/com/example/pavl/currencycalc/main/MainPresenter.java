package com.example.pavl.currencycalc.main;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.example.pavl.currencycalc.R;
import com.example.pavl.currencycalc.domain.Controller;
import com.example.pavl.currencycalc.model.Currency;
import com.example.pavl.currencycalc.model.CurrencyList;
import com.example.pavl.currencycalc.mvp.MvpPresenter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class MainPresenter extends MvpPresenter<MainState> {
    private final Controller controller;
    private final Resources resources;
    private final Map<String, Drawable> flags;

    public MainPresenter(Context applicationContext) {
        super(applicationContext);
        controller = Controller.getInstance(applicationContext);
        flags = Collections.synchronizedMap(new HashMap<>());
        resources = context.getResources();
    }

    @Override
    public void onStart() {
        onUpdate();
    }

    @Override
    protected void onStop() {
    }

    void onUpdate() {
        controller.fetch(currencyList -> {
            state.setList(currencyList);
            for (Currency currency : currencyList.getCurrencies()) {
                getFlagDrawable(currency.getCharCode());
            }
            commit();
        }, throwable -> {
            state.setMessage(throwable.getMessage());
            commit();
        });
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

    Drawable getFlagDrawable(String charCode) {
        String name = "ic_" + charCode.toLowerCase();
        Drawable flag = flags.get(name);
        if (flag == null) {
            int id = resources.getIdentifier(name, "drawable", context.getPackageName());
            if (id == 0) {
                flag = resources.getDrawable(R.drawable.flag_unknown, null);
            } else {
                flag = resources.getDrawable(id, null);
            }
            flags.put(name, flag);
        }
        return flag;
    }

    @Override
    public void commit() {
        super.commit();
        state.isError = false;
        state.isListChanged = false;
    }
}
