package com.example.pavl.currencycalc.main;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavl.currencycalc.R;
import com.example.pavl.currencycalc.background.Service;
import com.example.pavl.currencycalc.mvp.MvpFragment;


public class MainFragment extends MvpFragment<MainPresenter, MainState> {
    private EditText originalAmount;
    private Spinner originalCurrency;
    private EditText resultAmount;
    private Spinner resultCurrency;
    private TextView dataDate;
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
    private CurrencyAdapter adapter;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new CurrencyAdapter(getContext());
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        originalAmount = (EditText) root.findViewById(R.id.original_amount);
        originalCurrency = (Spinner) root.findViewById(R.id.original_currency);
        originalCurrency.setAdapter(adapter);
        swap = (ImageButton) root.findViewById(R.id.swap);
        resultAmount = (EditText) root.findViewById(R.id.result_amount);
        resultCurrency = (Spinner) root.findViewById(R.id.result_currency);
        resultCurrency.setAdapter(adapter);
        num1 = (Button) root.findViewById(R.id.num_1);
        num2 = (Button) root.findViewById(R.id.num_2);
        num3 = (Button) root.findViewById(R.id.num_3);
        num4 = (Button) root.findViewById(R.id.num_4);
        num5 = (Button) root.findViewById(R.id.num_5);
        num6 = (Button) root.findViewById(R.id.num_6);
        num7 = (Button) root.findViewById(R.id.num_7);
        num8 = (Button) root.findViewById(R.id.num_8);
        num9 = (Button) root.findViewById(R.id.num_9);
        num0 = (Button) root.findViewById(R.id.num_0);
        point = (Button) root.findViewById(R.id.num_point);
        del = (Button) root.findViewById(R.id.del);
        dataDate = (TextView) root.findViewById(R.id.date);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        NumListener numListener = new NumListener();

        originalCurrency.setOnItemSelectedListener(new OriginalCurrencyListener());
        resultCurrency.setOnItemSelectedListener(new ResultCurrencyListener());
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.update) {
            getContext().startService(Service.getIntent(getContext()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStateChanged(MainState state) {
        if (state.isListChanged) {
            state.isListChanged = false;
            adapter.clear();
            adapter.addAll(state.list.getCurrencies());
            Toast.makeText(getContext(), R.string.data_updated, Toast.LENGTH_LONG).show();
        }
        originalAmount.setText(state.getOriginalAmount());
        originalCurrency.setSelection(state.originalPosition);
        resultAmount.setText(state.getResultAmount());
        resultCurrency.setSelection(state.resultPosition);
        if (state.isError) {
            state.isError = false;
            Toast.makeText(getContext(), state.message, Toast.LENGTH_LONG).show();
        }
        dataDate.setText(state.getDate());
    }

    @Override
    public MainState onCreateState() {
        return new MainState();
    }

    @Override
    public MainPresenter onCreatePresenter() {
        return new MainPresenter(this);
    }

    private class ResultCurrencyListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            presenter.onResultCurrencyChanged(i);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    private class OriginalCurrencyListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            presenter.onOriginalCurrencyChanged(i);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    private class SwapListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            presenter.onCurrenciesSwap();
        }
    }

    private class NumListener implements View.OnClickListener {
        char getChar(View v) {
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
            Editable text = new Editable.Factory().newEditable(state.originalText);
            if (v.getId() == R.id.del) {
                if (text.length() > 0) {
                    text.delete(text.length() - 1, text.length());
                }
            } else if (v.getId() == R.id.num_point) {
                if (!state.isOriginalReal()) {
                    if (text.length() == 0) {
                        text.append('0');
                    }
                    text.append('.');
                }
            } else {
                text.append(getChar(v));
            }
            presenter.onOriginalAmountChanged(text.toString());
        }
    }
}
