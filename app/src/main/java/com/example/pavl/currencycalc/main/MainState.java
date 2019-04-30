package com.example.pavl.currencycalc.main;

import com.example.pavl.currencycalc.model.Currency;
import com.example.pavl.currencycalc.model.CurrencyList;
import com.example.pavl.currencycalc.mvp.MvpState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainState extends MvpState {
    List<Currency> list = Collections.emptyList();
    String date = "";
    boolean isListChanged;
    Currency originalCurrency = new Currency();
    Currency resultCurrency = new Currency();
    String originalText = "";
    double originalAmount = -1;
    double resultAmount = -1;
    boolean isError;
    String message;

    void setList(CurrencyList list) {
        setChanged(true);
        this.list = list.getCurrencies();
        date = list.getDate();
        originalCurrency = originalCurrency.getNumCode() == 0 ?  list.getCurrencies().get(0) : list.get(originalCurrency.getNumCode());
        resultCurrency = resultCurrency.getNumCode() == 0 ? list.getCurrencies().get(0) : list.get(resultCurrency.getNumCode());
        this.isListChanged = true;
    }

    public void setOriginalCurrency(Currency currency) {
        setChanged(originalCurrency.getNumCode() != currency.getNumCode());
        originalCurrency = currency;
    }

    public void setResultCurrency(Currency currency) {
        setChanged(resultCurrency.getNumCode() != currency.getNumCode());
        this.resultCurrency = currency;
    }

    String getOriginalAmount() {
        return originalText;
    }

    void setOriginalAmount(double originalAmount, String text) {
        setChanged(!originalText.equals(text));
        this.originalAmount = originalAmount;
        this.originalText = text;
    }

    boolean isOriginalReal() {
        return originalText.contains(".");
    }

    String getResultAmount() {
        if (resultAmount == -1) {
            return "";
        }
        return String.format(Locale.US, "%.1f", resultAmount);
    }

    public void setResultAmount(double resultAmount) {
        this.resultAmount = resultAmount;
    }

    void updateResult() {
        setChanged(true);
        resultAmount =  CurrencyList.convert(originalCurrency, resultCurrency, originalAmount);
    }

    void setMessage(String message) {
        setChanged(true);
        this.message = message;
        this.isError = true;
    }

    void swapCurrencies() {
        double originalAmount = this.originalAmount;
        Currency originalCurrency = this.originalCurrency;
        this.originalAmount = resultAmount;
        this.originalText = getResultAmount();
        this.resultAmount = originalAmount;
        this.originalCurrency = resultCurrency;
        this.resultCurrency = originalCurrency;
        setChanged(true);
    }

    @Override
    protected MainState clone() throws CloneNotSupportedException {
        MainState state = (MainState) super.clone();;
        state.list = new ArrayList<>(list);
        return state;
    }

    @Override
    public String toString() {
        return "MainState{" +
                "originalCurrency=" + originalCurrency +
                ", resultCurrency=" + resultCurrency +
                ", originalText='" + originalText + '\'' +
                ", originalAmount=" + originalAmount +
                ", resultAmount=" + resultAmount +
                "} " + super.toString();
    }
}
