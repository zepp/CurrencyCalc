package com.example.pavl.currencycalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getCanonicalName();

    private final String file = "rates.xml";
    private final String url = "http://www.cbr.ru/scripts/XML_daily.asp";

    private Fetcher fetcher;
    private EditText fromAmount;
    private Spinner fromCurrency;
    private EditText toAmount;
    private Spinner toCurrency;
    private CurrencyAdapter currencyAdapter;
    private List<Currency> currencies = new ArrayList<>();
    private Currency from;
    private Currency to;

    private void updateUI()
    {
        try {
            double amount = Double.valueOf(fromAmount.getText().toString());
            toAmount.setText(String.format("%.2f", CurrencyList.convert(from, to, amount)));
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Failed to convert currency: " + e, Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fromCurrency = (Spinner) findViewById(R.id.from_currency);
        toCurrency = (Spinner) findViewById(R.id.to_currency);
        fromAmount = (EditText) findViewById(R.id.from_amount);
        toAmount = (EditText) findViewById(R.id.to_amount);

        currencyAdapter = new CurrencyAdapter(this, currencies);
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

        fetcher = new Fetcher(MainActivity.this, file, url);
        fetcher.setListener(new Fetcher.OnUpdateListener() {
            @Override
            public void onUpdated(List<Currency> currencyList) {
                currencies.addAll(currencyList);
                currencyAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetcher.get();
    }
}
