package com.example.pavl.currencycalc.ui;

import com.example.pavl.currencycalc.model.Currency;
import com.example.pavl.currencycalc.model.CurrencyList;
import com.example.pavl.currencycalc.R;

import android.app.Activity;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainViewImpl implements MainView {
    private final Activity activity;
    private final Resources resources;
    private final String packageName;
    private final List<Currency> currencies = new ArrayList<>();
    private final CurrencyAdapter currencyAdapter;

    private Controller controller;

    private View root;
    private EditText originalAmount;
    private Spinner originalCurrency;
    private EditText resultAmount;
    private Spinner resultCurrency;
    private TextView dataDate;
    private ImageView originalFlag;
    private ImageView resultFlag;
    private ImageButton swap;
    private Button num1;
    private Button num2;
    private Button num3;
    private Button num4;
    private Button num5;
    private Button num6;
    private Button num7;
    private Button num8;
    private Button num9;
    private Button num0;
    private Button point;
    private Button del;

    public MainViewImpl(Activity activity, LayoutInflater inflater, ViewGroup container) {
        this.activity = activity;

        init(inflater.inflate(R.layout.fragment_main, container, false));

        currencyAdapter = new CurrencyAdapter(root.getContext(), currencies);
        originalCurrency.setAdapter(currencyAdapter);
        resultCurrency.setAdapter(currencyAdapter);
        originalAmount.setShowSoftInputOnFocus(false);
        resultAmount.setShowSoftInputOnFocus(false);

        resources = activity.getBaseContext().getResources();
        packageName = activity.getBaseContext().getPackageName();
    }

    private void init (View root){
        this.root = root;
        this.originalFlag = (ImageView) root.findViewById(R.id.original_flag);
        this.originalAmount = (EditText) root.findViewById(R.id.original_amount);
        this.originalCurrency = (Spinner) root.findViewById(R.id.original_currency);
        this.swap = (ImageButton) root.findViewById(R.id.swap);
        this.resultFlag = (ImageView) root.findViewById(R.id.result_flag);
        this.resultAmount = (EditText) root.findViewById(R.id.result_amount);
        this.resultCurrency = (Spinner) root.findViewById(R.id.result_currency);
        this.num1 = (Button)root.findViewById(R.id.num_1);
        this.num2 = (Button)root.findViewById(R.id.num_2);
        this.num3 = (Button)root.findViewById(R.id.num_3);
        this.num4 = (Button)root.findViewById(R.id.num_4);
        this.num5 = (Button)root.findViewById(R.id.num_5);
        this.num6 = (Button)root.findViewById(R.id.num_6);
        this.num7 = (Button)root.findViewById(R.id.num_7);
        this.num8 = (Button)root.findViewById(R.id.num_8);
        this.num9 = (Button)root.findViewById(R.id.num_9);
        this.num0 = (Button)root.findViewById(R.id.num_0);
        this.point = (Button)root.findViewById(R.id.num_point);
        this.del = (Button)root.findViewById(R.id.del);
        this.dataDate = (TextView) root.findViewById(R.id.date);
    }

    private int getFlagResourceId (Currency currency) {
        String name = "ic_" + currency.getCharCode().toLowerCase();
        return resources.getIdentifier(name, "drawable", packageName);
    }

    @Override
    public void start() {
        NumListener numListener = new NumListener();

        originalCurrency.setOnItemSelectedListener(new OriginalCurrencyListener());
        resultCurrency.setOnItemSelectedListener(new ResultCurrencyListener());
        originalAmount.addTextChangedListener(new AmountTextWatcher());
        swap.setOnClickListener(new SwapListener());
        num1.setOnClickListener(numListener);
        num2.setOnClickListener(numListener);
        num3.setOnClickListener(numListener);
        num4.setOnClickListener(numListener);
        num5.setOnClickListener(numListener);
        num6.setOnClickListener(numListener);
        num7.setOnClickListener(numListener);
        num8.setOnClickListener(numListener);
        num9.setOnClickListener(numListener);
        num0.setOnClickListener(numListener);
        point.setOnClickListener(numListener);
        del.setOnClickListener(numListener);
    }

    @Override
    public void stop() {
        controller = null;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public View getRootView() {
        return root;
    }

    @Override
    public void bind(CurrencyList list) {
        // since ArrayAdapter does not provide a way to notify about particular item change
        // all data is just replaced by new one :-(
        list.sort();
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
            double amount = -1;
            if (editable.length() > 0) {
                try {
                    amount = Double.valueOf(editable.toString());
                } catch (Exception e) {
                    resultAmount.setText("");
                }
            } else {
                resultAmount.setText("");
            }
            if (controller != null) {
                controller.originalAmountChanged(amount);
            }
        }
    }

    private class ResultCurrencyListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Currency result = (Currency) adapterView.getItemAtPosition(i);
            int id = getFlagResourceId(result);
            if (id == 0) {
                resultFlag.setImageResource(R.drawable.flag_unknown);
            } else {
                resultFlag.setImageResource(id);
            }
            if (controller != null) {
                controller.resultCurrencyChanged(result);
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
                originalFlag.setImageResource(R.drawable.flag_unknown);
            } else {
                originalFlag.setImageResource(id);
            }
            if (controller != null) {
                controller.originalCurrencyChanged(original);
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
            originalAmount.setText(resultAmount.getText());
        }
    }

    private class NumListener implements View.OnClickListener {
        char getChar (View v) {
            switch (v.getId()) {
                case R.id.num_0:
                    return '0';
                case R.id.num_1:
                    return '1';
                case R.id.num_2:
                    return '2';
                case R.id.num_3:
                    return '3';
                case R.id.num_4:
                    return '4';
                case R.id.num_5:
                    return '5';
                case R.id.num_6:
                    return '6';
                case R.id.num_7:
                    return '7';
                case R.id.num_8:
                    return '8';
                case R.id.num_9:
                    return '9';
                case R.id.num_point:
                    return '.';
                default:
                    return ' ';
            }
        }

        @Override
        public void onClick(View v) {
            View focused = activity.getCurrentFocus();
            if (focused instanceof EditText) {
                EditText edit = (EditText) focused;
                if (!edit.isEnabled())
                    return;
                Editable text = edit.getText();
                if (v.getId() == R.id.del) {
                    if (text.length() > 0) {
                        text.delete(text.length() - 1, text.length());
                    }
                } else if (v.getId() == R.id.num_point) {
                    if (text.toString().indexOf('.') == -1) {
                        if (text.length() == 0) {
                            text.append('0');
                        }
                        text.append('.');
                    }
                } else {
                    text.append(getChar(v));
                }
            }
        }
    }
}
