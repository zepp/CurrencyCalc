package com.example.pavl.currencycalc.ui;

import com.example.pavl.currencycalc.model.Currency;
import com.example.pavl.currencycalc.model.CurrencyList;
import com.example.pavl.currencycalc.R;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainViewImpl implements MainView {
    private final List<Currency> currencies = new ArrayList<>();
    private Listener listener;
    private View root;
    private TextView originalTitle;
    private EditText originalAmount;
    private Spinner originalCurrency;
    private AmountTextWatcher fromTextWatcher = new AmountTextWatcher();
    private TextView resultTitle;
    private EditText resultAmount;
    private Spinner resultCurrency;
    private CurrencyAdapter currencyAdapter;
    private TextView dataDate;

    public MainViewImpl(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.fragment_main, container, false);

        originalTitle = (TextView) root.findViewById(R.id.original_title);
        originalCurrency = (Spinner) root.findViewById(R.id.original_currency);
        originalAmount = (EditText) root.findViewById(R.id.original_amount);
        resultTitle = (TextView) root.findViewById(R.id.result_title);
        resultCurrency = (Spinner) root.findViewById(R.id.result_currency);
        resultAmount = (EditText) root.findViewById(R.id.result_amount);
        dataDate = (TextView) root.findViewById(R.id.date);

        currencyAdapter = new CurrencyAdapter(root.getContext(), currencies);

        originalCurrency.setAdapter(currencyAdapter);
        originalCurrency.setOnItemSelectedListener(new OriginalCurrencyListener());
        originalAmount.addTextChangedListener(fromTextWatcher);
        resultCurrency.setAdapter(currencyAdapter);
        resultCurrency.setOnItemSelectedListener(new ResultCurrencyListener());
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public View getRootView() {
        return root;
    }

    @Override
    public void saveState(Bundle state) {
    }

    @Override
    public void bind(CurrencyList list) {
        // since ArrayAdapter does not provide a way to notify about particular item change
        // all data is just replaced by new one :-(
        currencyAdapter.clear();
        currencyAdapter.addAll(list.getCurrencies());
        dataDate.setText(list.getDate());
        originalAmount.setEnabled(true);
        resultAmount.setEnabled(true);
    }

    @Override
    public void setResult(double amount) {
        resultAmount.setText(String.format("%.2f", amount));
    }

    private class AmountTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            double amount = -1;
            if (text.length() > 0) {
                amount = Double.valueOf(text);
            } else {
                resultAmount.setText("");
            }
            if (listener != null) {
                listener.originalAmountChanged(amount);
            }
        }
    }

    private class ResultCurrencyListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Currency result = (Currency) adapterView.getItemAtPosition(i);
            resultTitle.setText(result.getName());
            if (listener != null) {
                listener.resultCurrencyChanged(result);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    private class OriginalCurrencyListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Currency original = (Currency) adapterView.getItemAtPosition(i);
            originalTitle.setText(original.getName());
            if (listener != null) {
                listener.originalCurrencyChanged(original);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }
}
