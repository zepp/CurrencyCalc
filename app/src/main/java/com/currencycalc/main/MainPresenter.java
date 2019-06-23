/*
 * Copyright (c) 2019 Pavel A. Sokolov
 */

package com.currencycalc.main;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.currencycalc.R;
import com.currencycalc.domain.Controller;
import com.currencycalc.model.Currency;
import com.currencycalc.model.CurrencyList;
import com.currencycalc.mvp.MvpBasePresenter;

import java.util.HashMap;
import java.util.Map;

public class MainPresenter extends MvpBasePresenter<MainState> {
    private final Controller controller;
    private final Map<Integer, Drawable> flags;

    public MainPresenter(Context context, MainState state) {
        super(context, state);
        controller = Controller.getInstance(context);
        flags = new HashMap<>();
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
        try {
            CurrencyList list = controller.load();
            state.setList(list);
            loadFlagDrawables(list);
        } catch (Throwable e) {
            state.setMessage(e.getMessage());
        }
        commit();
    }

    @Override
    public void onViewClicked(int viewId) {
        super.onViewClicked(viewId);
        String text = state.originalText;
        if (viewId == R.id.swap) {
            state.swapCurrencies();
        } else if (viewId == R.id.del) {
            if (!text.isEmpty()) {
                setOriginalAmount(text.substring(0, text.length() - 1));
            }
        } else if (viewId == R.id.num_point) {
            if (!state.isOriginalReal()) {
                setOriginalAmount(text + '.');
            }
        } else {
            setOriginalAmount(text + getChar(viewId));
        }
        commit();
    }

    @Override
    public void onOptionsItemSelected(int itemId) {
        super.onOptionsItemSelected(itemId);
        if (itemId == R.id.update) {
            controller.asyncFetch((list, e) -> {
                if (e == null) {
                    state.setList(list);
                    state.setMessage(context.getString(R.string.data_updated));
                    loadFlagDrawables(list);
                } else {
                    state.setMessage(e.getMessage());
                }
                commit();
            });
        }
    }

    @Override
    public void onItemSelected(int viewId, Object item) {
        super.onItemSelected(viewId, item);
        if (viewId == R.id.original_currency) {
            state.setOriginalCurrency((Currency) item);
        } else {
            state.setResultCurrency((Currency) item);
        }
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

    Drawable getFlagDrawable(Currency currency) {
        synchronized (flags) {
            Drawable flag = flags.get(currency.getNumCode());
            if (flag == null) {
                String name = "ic_" + currency.getCharCode().toLowerCase();
                int id = resources.getIdentifier(name, "drawable", context.getPackageName());
                if (id == 0) {
                    flag = resources.getDrawable(R.drawable.flag_unknown, null);
                } else {
                    flag = resources.getDrawable(id, null);
                }
                flags.put(currency.getNumCode(), flag);
            }
            return flag;
        }
    }

    private void loadFlagDrawables(CurrencyList list) {
        for (Currency currency : list.getCurrencies()) {
            getFlagDrawable(currency);
        }
    }

    @Override
    public void commit() {
        super.commit();
        state.message = null;
    }
}
