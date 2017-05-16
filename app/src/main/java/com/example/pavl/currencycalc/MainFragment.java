package com.example.pavl.currencycalc;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends android.support.v4.app.Fragment implements Fetcher.Listener{
    private static final String ARG_URL = "arg-url";
    private static final String ARG_FILE_NAME = "arg-file-name";
    private final List<Currency> currencies = new ArrayList<>();
    private Fetcher fetcher;
    private EditText fromAmount;
    private Spinner fromCurrency;
    private EditText toAmount;
    private Spinner toCurrency;
    private CurrencyAdapter currencyAdapter;
    private TextView date;
    private Currency from;
    private Currency to;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String url, String fileName) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_FILE_NAME, fileName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String url = "";
        String fileName = "";

        if (getArguments() != null) {
            url = getArguments().getString(ARG_URL);
            fileName = getArguments().getString(ARG_FILE_NAME);
        }

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        fromCurrency = (Spinner) root.findViewById(R.id.from_currency);
        toCurrency = (Spinner) root.findViewById(R.id.to_currency);
        fromAmount = (EditText) root.findViewById(R.id.from_amount);
        toAmount = (EditText) root.findViewById(R.id.to_amount);
        date = (TextView) root.findViewById(R.id.date);

        currencyAdapter = new CurrencyAdapter(getContext(), currencies);
        fromCurrency.setAdapter(currencyAdapter);
        fromCurrency.setOnItemSelectedListener(new FromCurrencyListener());
        toCurrency.setAdapter(currencyAdapter);
        toCurrency.setOnItemSelectedListener(new ToCurrencyListener());
        fromAmount.addTextChangedListener(new CurrencyTextWatcher());
        fetcher = new Fetcher(getContext(), fileName, url);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetcher.registerListener(this);
        fetcher.fetch();
    }

    @Override
    public void onStop() {
        super.onStop();
        fetcher.unregisterListener(this);
    }

    @Override
    public void onFetchError(Exception e) {
        // more complicated exception class handling should be put here
        Toast.makeText(getContext(), "failed to fetch data: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDataFetched(CurrencyList list) {
        // since ArrayAdapter does not provide a way to notify about particular item change
        // all data is just replaced by new one :-(
        currencyAdapter.clear();
        currencyAdapter.addAll(list.getCurrencies());
        date.setText(list.getDate());
    }

    private void updateUI()
    {
        try {
            double amount = Double.valueOf(fromAmount.getText().toString());
            toAmount.setText(String.format("%.2f", CurrencyList.convert(from, to, amount)));
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), "Failed to convert currency: " + e, Toast.LENGTH_SHORT);
        }
    }

    private class CurrencyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() == 0)
                toAmount.setText("");
            else
                updateUI();
        }
    }

    private class ToCurrencyListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            to = (Currency)adapterView.getItemAtPosition(i);
            updateUI();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    }

    private class FromCurrencyListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            from = (Currency)adapterView.getItemAtPosition(i);
            updateUI();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    }
}
