/*
 * Copyright (c) 2019 Pavel A. Sokolov
 */

package com.currencycalc.main;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.currencycalc.R;
import com.currencycalc.mvp.MvpFragment;
import com.currencycalc.mvp.MvpPresenterManager;


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
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        originalAmount = view.findViewById(R.id.original_amount);
        originalCurrency = view.findViewById(R.id.original_currency);
        swap = view.findViewById(R.id.swap);
        resultAmount = view.findViewById(R.id.result_amount);
        resultCurrency = view.findViewById(R.id.result_currency);
        num1 = view.findViewById(R.id.num_1);
        num2 = view.findViewById(R.id.num_2);
        num3 = view.findViewById(R.id.num_3);
        num4 = view.findViewById(R.id.num_4);
        num5 = view.findViewById(R.id.num_5);
        num6 = view.findViewById(R.id.num_6);
        num7 = view.findViewById(R.id.num_7);
        num8 = view.findViewById(R.id.num_8);
        num9 = view.findViewById(R.id.num_9);
        num0 = view.findViewById(R.id.num_0);
        point = view.findViewById(R.id.num_point);
        del = view.findViewById(R.id.del);
        dataDate = view.findViewById(R.id.date);
    }

    @Override
    public void onStart() {
        super.onStart();
        originalCurrency.setOnClickListener(v ->
                CurrencyDialog.newInstance(v.getId(), presenter.getState().originalCurrency.getNumCode())
                        .show(getFragmentManager(), "original"));
        resultCurrency.setOnClickListener(v ->
                CurrencyDialog.newInstance(v.getId(), presenter.getState().resultCurrency.getNumCode())
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
