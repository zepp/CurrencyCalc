package com.example.pavl.currencycalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getCanonicalName();

    private final String file = "rates.xml";
    private final String url = "http://www.cbr.ru/scripts/XML_daily.asp";

    private Converter converter;
    private EditText fromAmount;
    private Spinner fromCurrency;
    private EditText toAmount;
    private Spinner toCurrency;
    private Button go;
    private ArrayAdapter<String> currencyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fromCurrency = (Spinner) findViewById(R.id.from_currency);
        toCurrency = (Spinner) findViewById(R.id.to_currency);
        fromAmount = (EditText) findViewById(R.id.from_amount);
        toAmount = (EditText) findViewById(R.id.to_amount);
        go = (Button) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    double result = converter.process(fromCurrency.getSelectedItem().toString(),
                            toCurrency.getSelectedItem().toString(),
                            Double.valueOf(fromAmount.getText().toString()));
                    toAmount.setText(String.format("%.2f", result));
                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "conversion error - " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        go.setEnabled(false);
        toAmount.setEnabled(false);

        currencyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        fromCurrency.setAdapter(currencyAdapter);
        toCurrency.setAdapter(currencyAdapter);

        converter = new Converter(MainActivity.this, file, url) {
            @Override
            public void onUpdated(boolean result) {
                if (result) {
                    go.setEnabled(true);
                    for (String s : charCodes()) {
                        currencyAdapter.add(s);
                    }
                }
                else
                    Toast.makeText(MainActivity.this, "failed to update XML data",
                            Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        converter.update();
    }
}
