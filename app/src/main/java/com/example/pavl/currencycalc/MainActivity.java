package com.example.pavl.currencycalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private Spinner spnOrigCode, spnDestCode;
    private EditText edtOrigAmount, edtDestCur;
    private Button btnConv;
    private ArrayAdapter<String> codeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spnOrigCode = (Spinner) findViewById(R.id.spnOrigCode);
        spnDestCode = (Spinner) findViewById(R.id.spnDestCode);
        edtOrigAmount = (EditText) findViewById(R.id.edtOrigAmount);
        edtDestCur = (EditText) findViewById(R.id.edtDestCur);
        btnConv = (Button) findViewById(R.id.btnConv);

        btnConv.setEnabled(false);
        edtDestCur.setEnabled(false);

        codeAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item);
        codeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnOrigCode.setAdapter(codeAdapter);
        spnDestCode.setAdapter(codeAdapter);

        converter = new Converter(MainActivity.this, file, url) {
            @Override
            public void onUpdated(boolean result) {
                if (result) {
                    btnConv.setEnabled(true);
                    for (String s : charCodes()) {
                        codeAdapter.add(s);
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

    public void onConvBtnClick(View v)
    {
        try {
            double result = converter.process(spnOrigCode.getSelectedItem().toString(),
                    spnDestCode.getSelectedItem().toString(),
                    Double.valueOf(edtOrigAmount.getText().toString()));
            edtDestCur.setText(String.format("%.2f", result));
        }
        catch (Exception e)
        {
            Toast.makeText(this, "conversion error - " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
