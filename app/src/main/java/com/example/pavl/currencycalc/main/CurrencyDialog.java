package com.example.pavl.currencycalc.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pavl.currencycalc.R;
import com.example.pavl.currencycalc.mvp.MvpDialogFragment;
import com.example.pavl.currencycalc.mvp.MvpPresenterManager;

public class CurrencyDialog extends MvpDialogFragment<MainPresenter, MainState> {
    private RecyclerView list;
    private Button cancel;

    public CurrencyDialog() {
    }

    public CurrencyDialog newInstance() {
        CurrencyDialog dialog = new CurrencyDialog();
        Bundle args = new Bundle();
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_currency, container, false);
        list = view.findViewById(R.id.currency_list);
        cancel = view.findViewById(R.id.currency_cancel);
        return view;
    }

    @Override
    public void onStateChanged(MainState state) {
    }

    @Override
    public MainPresenter onInitPresenter(MvpPresenterManager manager) {
        return manager.gewPresenterInstance(MainPresenter.class, MainState.class);
    }
}
