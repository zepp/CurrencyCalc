package com.example.pavl.currencycalc.ui;

import com.example.pavl.currencycalc.model.Currency;
import com.example.pavl.currencycalc.model.CurrencyList;
import com.example.pavl.currencycalc.R;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainViewImpl implements MainView {
    private final List<Currency> currencies = new ArrayList<>();
    private final Resources resources;
    private final String packageName;
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
    private ImageView originalFlag;
    private ImageView resultFlag;
    private ImageButton swap;

    public MainViewImpl(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.fragment_main, container, false);
        Context context = root.getContext();

        originalFlag = (ImageView) root.findViewById(R.id.original_flag);
        originalTitle = (TextView) root.findViewById(R.id.original_title);
        originalCurrency = (Spinner) root.findViewById(R.id.original_currency);
        originalAmount = (EditText) root.findViewById(R.id.original_amount);
        swap = (ImageButton) root.findViewById(R.id.swap);
        resultFlag = (ImageView) root.findViewById(R.id.result_flag);
        resultTitle = (TextView) root.findViewById(R.id.result_title);
        resultCurrency = (Spinner) root.findViewById(R.id.result_currency);
        resultAmount = (EditText) root.findViewById(R.id.result_amount);
        dataDate = (TextView) root.findViewById(R.id.date);

        currencyAdapter = new CurrencyAdapter(context, currencies);

        originalCurrency.setAdapter(currencyAdapter);
        originalCurrency.setOnItemSelectedListener(new OriginalCurrencyListener());
        originalAmount.addTextChangedListener(fromTextWatcher);
        resultCurrency.setAdapter(currencyAdapter);
        resultCurrency.setOnItemSelectedListener(new ResultCurrencyListener());
        swap.setOnClickListener(new SwapListener());

        resources = context.getResources();
        packageName = context.getPackageName();

    }

    private int getFlagResourceId (Currency currency) {
        String name = "ic_" + currency.getCharCode().toLowerCase();
        return resources.getIdentifier(name, "drawable", packageName);
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
        resultAmount.setText(String.format("%.1f", amount));
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
            int id = getFlagResourceId(result);
            if (id == 0) {
                resultFlag.setVisibility(View.GONE);
            } else {
                resultFlag.setVisibility(View.VISIBLE);
                resultFlag.setImageResource(id);
            }
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
            int id = getFlagResourceId(original);
            if (id == 0) {
                originalFlag.setVisibility(View.GONE);
            } else {
                originalFlag.setVisibility(View.VISIBLE);
                originalFlag.setImageResource(getFlagResourceId(original));
            }
            if (listener != null) {
                listener.originalCurrencyChanged(original);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    private class SwapListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int original = resultCurrency.getSelectedItemPosition();
            int result = originalCurrency.getSelectedItemPosition();
            resultCurrency.setSelection(result);
            originalCurrency.setSelection(original);
        }
    }
}
