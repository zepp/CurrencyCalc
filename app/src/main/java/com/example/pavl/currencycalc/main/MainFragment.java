package com.example.pavl.currencycalc.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavl.currencycalc.R;
import com.example.pavl.currencycalc.mvp.MvpFragment;
import com.example.pavl.currencycalc.mvp.MvpPresenterManager;


public class MainFragment extends MvpFragment<MainPresenter, MainState> {
    private TextView originalAmount;
    private TextView originalCurrency;
    private TextView resultAmount;
    private TextView resultCurrency;
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
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        originalAmount = root.findViewById(R.id.original_amount);
        originalCurrency = root.findViewById(R.id.original_currency);
        swap = root.findViewById(R.id.swap);
        resultAmount = root.findViewById(R.id.result_amount);
        resultCurrency = root.findViewById(R.id.result_currency);
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
        originalCurrency.setOnClickListener(v ->
                CurrencyDialog.newInstance(presenter.getState().originalCurrency.getNumCode())
                        .show(getFragmentManager(), "original"));
        resultCurrency.setOnClickListener(v ->
                CurrencyDialog.newInstance(presenter.getState().resultCurrency.getNumCode())
                        .show(getFragmentManager(), "result"));
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
        originalAmount.setText(state.getOriginalAmount());
        originalCurrency.setText(state.originalCurrency.getCharCode());
        resultAmount.setText(state.getResultAmount());
        resultCurrency.setText(state.resultCurrency.getCharCode());
        dataDate.setText(state.date);
        if (state.message != null) {
            Toast.makeText(getContext(), state.message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public MainPresenter onInitPresenter(MvpPresenterManager manager) {
        return manager.gewPresenterInstance(MainPresenter.class, MainState.class);
    }
}
