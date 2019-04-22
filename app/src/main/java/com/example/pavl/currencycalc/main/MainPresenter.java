package com.example.pavl.currencycalc.main;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.example.pavl.currencycalc.R;
import com.example.pavl.currencycalc.domain.Controller;
import com.example.pavl.currencycalc.model.Currency;
import com.example.pavl.currencycalc.mvp.MvpBasePresenter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainPresenter extends MvpBasePresenter<MainState> {
    private final Controller controller;
    private final Resources resources;
    private final Map<String, Drawable> flags;

    public MainPresenter(Context context, MainState state) {
        super(context, state);
        controller = Controller.getInstance(context);
        flags = Collections.synchronizedMap(new HashMap<>());
        resources = context.getResources();
    }

    @Override
    public void onStart() {
        super.onStart();
        onUpdate();
    }

    void onUpdate() {
        controller.fetch(currencyList -> handler.post(() -> {
            state.setList(currencyList);
            for (Currency currency : currencyList.getCurrencies()) {
                getFlagDrawable(currency.getCharCode());
            }
            commit();
        }), throwable -> handler.post(() -> {
            state.setMessage(throwable.getMessage());
            commit();
        }));
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
