package com.example.pavl.currencycalc.ui;

import com.example.pavl.currencycalc.model.Currency;
import com.example.pavl.currencycalc.model.CurrencyList;

public interface MainView extends BaseView {
    void setController(Controller controller);

    void bind(CurrencyList list);

    void setResult(double amount);

    interface Controller {
        void originalCurrencyChanged(Currency currency);

        void originalAmountChanged(double amount);

        void resultCurrencyChanged(Currency currency);
    }
}
