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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends android.support.v4.app.Fragment {
    private static final String ARG_URL = "arg-url";
    private static final String ARG_FILE_NAME = "arg-file-name";

    private Fetcher fetcher;
    private EditText fromAmount;
    private Spinner fromCurrency;
    private EditText toAmount;
    private Spinner toCurrency;
    private CurrencyAdapter currencyAdapter;
    private List<Currency> currencies = new ArrayList<>();
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
        String url = "";
        String fileName = "";

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            url = getArguments().getString(ARG_URL);
            fileName = getArguments().getString(ARG_FILE_NAME);
        }

        fetcher = new Fetcher(getContext(), fileName, url);
        fetcher.setListener(new Fetcher.OnUpdateListener() {
            @Override
            public void onUpdated(List<Currency> currencyList) {
                currencies.addAll(currencyList);
                currencyAdapter.notifyDataSetChanged();
            }
        });
        if (savedInstanceState == null)
            fetcher.get();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        fromCurrency = (Spinner) root.findViewById(R.id.from_currency);
        toCurrency = (Spinner) root.findViewById(R.id.to_currency);
        fromAmount = (EditText) root.findViewById(R.id.from_amount);
        toAmount = (EditText) root.findViewById(R.id.to_amount);

        currencyAdapter = new CurrencyAdapter(getContext(), currencies);
        fromCurrency.setAdapter(currencyAdapter);
        toCurrency.setAdapter(currencyAdapter);

        toCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                to = (Currency)adapterView.getItemAtPosition(i);
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        fromCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                from = (Currency)adapterView.getItemAtPosition(i);
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        fromAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals(""))
                    toAmount.setText("");
                else
                    updateUI();
            }
        });

        return root;
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
}
