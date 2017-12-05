package com.example.pavl.currencycalc.main;

import com.example.pavl.currencycalc.model.Currency;
import com.example.pavl.currencycalc.model.CurrencyList;
import com.example.pavl.currencycalc.mvp.MvpState;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class MainState extends MvpState {
    List<Currency> list = new ArrayList<>();
    String date = "";
    boolean isListChanged;
    int originalPosition = -1;
    int resultPosition = -1;
    String originalText = "";
    double originalAmount = -1;
    double resultAmount = -1;
    boolean isError;
    String message;

    void setList(CurrencyList list) {
        setChanged(true);
        if (originalPosition != -1) {
            Currency currency = list.getCurrencies().get(originalPosition);
            originalPosition = list.getPosition(currency.getNumCode());
        }
        if (resultPosition != -1) {
            Currency currency = list.getCurrencies().get(resultPosition);
            resultPosition = list.getPosition(currency.getNumCode());
        }
        this.list.clear();
        this.list.addAll(list.getCurrencies());
        this.date = list.getDate();
        this.isListChanged = true;
    }

    void setOriginalPosition(int originalPosition) {
        setChanged(this.originalPosition != originalPosition);
        this.originalPosition = originalPosition;
    }

    void setResultPosition(int resultPosition) {
        setChanged(this.resultPosition != resultPosition);
        this.resultPosition = resultPosition;
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

    void updateResult() {
        if (originalPosition != -1 && resultPosition != -1 && originalAmount != -1) {
            Currency original = list.get(originalPosition);
            Currency result = list.get(resultPosition);
            resultAmount =  CurrencyList.convert(original, result, originalAmount);
        }
    }

    void setMessage(String message) {
        setChanged(true);
        this.message = message;
        this.isError = true;
    }

    void swapCurrencies() {
        double originalAmount = this.originalAmount;
        int originalPosition = this.originalPosition;
        this.originalAmount = resultAmount;
        this.originalPosition = resultPosition;
        this.originalText = getResultAmount();
        this.resultAmount = originalAmount;
        this.resultPosition = originalPosition;
        setChanged(true);
    }

    @Override
    public String toString() {
        return "MainState { " +
                "originalPosition: " + originalPosition +
                ", originalAmount: " + getOriginalAmount() +
                ", resultPosition: " + resultPosition +
                ", resultAmount: " + getResultAmount() +
                '}';
    }
}
