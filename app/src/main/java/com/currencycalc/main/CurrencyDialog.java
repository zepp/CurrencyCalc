/*
 * Copyright (c) 2019 Pavel A. Sokolov
 */

package com.currencycalc.main;

import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.currencycalc.R;
import com.currencycalc.model.Currency;
import com.currencycalc.mvp.MvpDialogFragment;
import com.currencycalc.mvp.MvpPresenterManager;

public class CurrencyDialog extends MvpDialogFragment<MainPresenter, MainState> implements CurrencyAdapter.ItemListener {
    private final static String NUM_CODE = "num-code";
    private final static String VIEW_ID = "view-id";
    private RecyclerView list;
    private Button cancel;
    private CurrencyAdapter adapter;
    private int numCode;

    public CurrencyDialog() {
    }

    public static CurrencyDialog newInstance(@IdRes int viewId, int numCode) {
        CurrencyDialog dialog = new CurrencyDialog();
        Bundle args = new Bundle();
        args.putInt(NUM_CODE, numCode);
        args.putInt(VIEW_ID, viewId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_currency;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numCode = getArguments().getInt(NUM_CODE);
        adapter = new CurrencyAdapter(presenter, this);
        adapter.setNumCode(numCode);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = view.findViewById(R.id.currency_list);
        list.setAdapter(adapter);
        cancel = view.findViewById(R.id.currency_cancel);
    }

    @Override
    public void onStart() {
        super.onStart();
        cancel.setOnClickListener(v -> finish());
    }

    @Override
    public void onStateChanged(MainState state) {
        adapter.replace(state.list);
        list.scrollToPosition(state.getCurrencyPosition(numCode));
    }

    @Override
    public void onItemSelected(Currency currency) {
        executor.execute(() -> presenter.onItemSelected(getArguments().getInt(VIEW_ID), currency));
        finish();
    }

    @Override
    public MainPresenter onInitPresenter(MvpPresenterManager manager) {
        return manager.gewPresenterInstance(MainPresenter.class, MainState.class);
    }
}
