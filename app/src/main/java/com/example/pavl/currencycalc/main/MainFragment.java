package com.example.pavl.currencycalc.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavl.currencycalc.R;
import com.example.pavl.currencycalc.mvp.MvpFragment;
import com.example.pavl.currencycalc.mvp.MvpPresenterManager;


public class MainFragment extends MvpFragment<MainPresenter, MainState> {
    private TextView originalAmount;
    private Spinner originalCurrency;
    private TextView resultAmount;
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
    private CurrencyAdapter originalAdapter;
    private CurrencyAdapter resultAdapter;

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
        originalAdapter = new CurrencyAdapter(presenter, getContext(), R.layout.currency_item, R.id.currency_name);
        originalAdapter.setDropDownViewResource(R.layout.currency_drop_item);
        resultAdapter = new CurrencyAdapter(presenter, getContext(), R.layout.currency_item, R.id.currency_name);
        resultAdapter.setDropDownViewResource(R.layout.currency_drop_item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        originalAmount = root.findViewById(R.id.original_amount);
        originalCurrency = root.findViewById(R.id.original_currency);
        originalCurrency.setAdapter(originalAdapter);
        swap = root.findViewById(R.id.swap);
        resultAmount = root.findViewById(R.id.result_amount);
        resultCurrency = root.findViewById(R.id.result_currency);
        resultCurrency.setAdapter(resultAdapter);
        num1 = root.findViewById(R.id.num_1);
        num2 = root.findViewById(R.id.num_2);
        num3 = root.findViewById(R.id.num_3);
        num4 = root.findViewById(R.id.num_4);
        num5 = root.findViewById(R.id.num_5);
        num6 = root.findViewById(R.id.num_6);
        num7 = root.findViewById(R.id.num_7);
        num8 = root.findViewById(R.id.num_8);
        num9 = root.findViewById(R.id.num_9);
        num0 = root.findViewById(R.id.num_0);
        point = root.findViewById(R.id.num_point);
        del = root.findViewById(R.id.del);
        dataDate = root.findViewById(R.id.date);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        originalCurrency.setOnItemSelectedListener(this);
        resultCurrency.setOnItemSelectedListener(this);
        swap.setOnClickListener(this);
        num1.setOnClickListener(this);
        num2.setOnClickListener(this);
        num3.setOnClickListener(this);
        num4.setOnClickListener(this);
        num5.setOnClickListener(this);
        num6.setOnClickListener(this);
        num7.setOnClickListener(this);
        num8.setOnClickListener(this);
        num9.setOnClickListener(this);
        num0.setOnClickListener(this);
        point.setOnClickListener(this);
        del.setOnClickListener(this);
    }

    @Override
    public void onStateChanged(MainState state) {
        if (state.isListChanged) {
            originalAdapter.replace(state.list);
            resultAdapter.replace(state.list);
            if (!state.isInitial()) {
                Toast.makeText(getContext(), R.string.data_updated, Toast.LENGTH_SHORT).show();
            }
        }
        originalAmount.setText(state.getOriginalAmount());
        originalCurrency.setSelection(state.list.indexOf(state.originalCurrency));
        originalAdapter.setNumCode(state.originalCurrency.getNumCode());
        resultAmount.setText(state.getResultAmount());
        resultCurrency.setSelection(state.list.indexOf(state.resultCurrency));
        resultAdapter.setNumCode(state.resultCurrency.getNumCode());
        dataDate.setText(state.date);
        if (state.message != null) {
            Toast.makeText(getContext(), state.message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public MainPresenter onInitPresenter(MvpPresenterManager manager) {
        return manager.newPresenterInstance(MainPresenter.class, MainState.class);
    }
}
