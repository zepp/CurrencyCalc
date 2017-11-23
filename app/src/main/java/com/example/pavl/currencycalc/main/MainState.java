package com.example.pavl.currencycalc.main;

import com.example.pavl.currencycalc.model.Currency;
import com.example.pavl.currencycalc.model.CurrencyList;
import com.example.pavl.currencycalc.mvp.MvpState;

import java.util.Locale;

class MainState extends MvpState {
    CurrencyList list;
    boolean isListChanged;
    Currency originalCurrency;
    Currency resultCurrency;
    double originalAmount = -1;
    double resultAmount = -1;
    boolean isError;
    String message;

    void setList(CurrencyList list) {
        setChanged(true);
        this.list = list;
        this.isListChanged = true;
    }

    void setOriginalCurrency(Currency originalCurrency) {
        setChanged(!this.originalCurrency.equals(originalCurrency));
        this.originalCurrency = originalCurrency;
    }

    void setResultCurrency(Currency resultCurrency) {
        setChanged(!this.resultCurrency.equals(resultCurrency));
        this.resultCurrency = resultCurrency;
    }

    String getOriginalAmount() {
        return String.format(Locale.US, "%.1f", originalAmount);
    }

    void setOriginalAmount(double originalAmount) {
        setChanged(this.originalAmount != originalAmount);
        this.originalAmount = originalAmount;
    }

    String getResultAmount() {
        return String.format(Locale.US, "%.1f", resultAmount);
    }

    void setResultAmount(double resultAmount) {
        setChanged(this.resultAmount != resultAmount);
        this.resultAmount = resultAmount;
    }

    void setMessage(String message) {
        setChanged(true);
        this.message = message;
        this.isError = true;
    }

    void updateResult() {
        if (originalCurrency != null && resultCurrency != null && originalAmount != -1) {
            setChanged(true);
            resultAmount = CurrencyList.convert(originalCurrency, resultCurrency, originalAmount);
        }
    }

    void swap() {
        double originalAmount = this.originalAmount;
        Currency originalCurrency = this.originalCurrency;
        this.originalAmount = resultAmount;
        this.originalCurrency = resultCurrency;
        this.resultAmount = originalAmount;
        this.resultCurrency = originalCurrency;
        setChanged(true);
    }

    @Override
    public String toString() {
        return "MainState { " +
                ", originalCurrency: " + originalCurrency +
                ", originalAmount: " + originalAmount +
                ", resultCurrency: " + resultCurrency +
                ", resultAmount: " + resultAmount +
                '}';
    }
}
