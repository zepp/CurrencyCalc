package com.example.pavl.currencycalc.main;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;

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

    private static char getChar(int viewId) {
        switch (viewId) {
            case R.id.num_0:
                return '0';
            case R.id.num_1:
                return '1';
            case R.id.num_2:
                return '2';
            case R.id.num_3:
                return '3';
            case R.id.num_4:
                return '4';
            case R.id.num_5:
                return '5';
            case R.id.num_6:
                return '6';
            case R.id.num_7:
                return '7';
            case R.id.num_8:
                return '8';
            case R.id.num_9:
                return '9';
            case R.id.num_point:
                return '.';
            default:
                return ' ';
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        onUpdate();
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

    @Override
    public void onViewClicked(int viewId) {
        super.onViewClicked(viewId);
        if (viewId == R.id.swap) {
            state.swapCurrencies();
        } else if (viewId == R.id.del) {
            String text = state.originalText;
            setOriginalAmount(text.substring(0, text.length() - 1));
        } else if (viewId == R.id.num_point && !state.isOriginalReal()) {
            setOriginalAmount(state.originalText + '.');
        } else {
            setOriginalAmount(state.originalText + getChar(viewId));
        }
        commit();
    }


    void onOriginalCurrencyChanged(Currency currency) {
        state.setOriginalCurrency(currency);
        if (state.isChanged()) {
            state.updateResult();
            commit();
        }
    }

    private void setOriginalAmount(String text) {
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
    }

    void onResultCurrencyChanged(Currency currency) {
        state.setResultCurrency(currency);
        if (state.isChanged()) {
            state.updateResult();
            commit();
        }
    }

    Drawable getFlagDrawable(String charCode) {
        String name = "ic_" + charCode.toLowerCase();
        synchronized (flags) {
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
    }

    @Override
    public void commit() {
        super.commit();
        state.isError = false;
        state.isListChanged = false;
    }
}
