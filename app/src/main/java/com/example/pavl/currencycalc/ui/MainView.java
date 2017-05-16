package com.example.pavl.currencycalc.ui;

import com.example.pavl.currencycalc.Currency;
import com.example.pavl.currencycalc.CurrencyList;

public interface MainView extends BaseView {
    void setListener(Listener listener);
    void bind(CurrencyList list);
    void setResult(double amount);

    interface Listener {
        void originalCurrencyChanged(Currency currency);
        void originalAmountChanged(double amount);
        void resultCurrencyChanged(Currency currency);
    }
}
