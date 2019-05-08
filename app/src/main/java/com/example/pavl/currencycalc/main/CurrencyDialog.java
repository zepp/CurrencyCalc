package com.example.pavl.currencycalc.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pavl.currencycalc.R;
import com.example.pavl.currencycalc.model.Currency;
import com.example.pavl.currencycalc.mvp.MvpDialogFragment;
import com.example.pavl.currencycalc.mvp.MvpPresenterManager;

public class CurrencyDialog extends MvpDialogFragment<MainPresenter, MainState> implements CurrencyAdapter.ItemListener {
    private final static String NUM_CODE = "num-code";
    private RecyclerView list;
    private Button cancel;
    private CurrencyAdapter adapter;

    public CurrencyDialog() {
    }

    public CurrencyDialog newInstance(int numCode) {
        CurrencyDialog dialog = new CurrencyDialog();
        Bundle args = new Bundle();
        args.putInt(NUM_CODE, numCode);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CurrencyAdapter(presenter, this);
        adapter.setNumCode(getArguments().getInt(NUM_CODE));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_currency, container, false);
        list = view.findViewById(R.id.currency_list);
        list.setAdapter(adapter);
        cancel = view.findViewById(R.id.currency_cancel);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cancel.setOnClickListener(this);
    }

    @Override
    public void onStateChanged(MainState state) {
        adapter.replace(state.list);
    }

    @Override
    public void onItemSelected(Currency currency) {
        executor.execute(() -> presenter.onItemSelected(list.getId(), currency));
    }

    @Override
    public MainPresenter onInitPresenter(MvpPresenterManager manager) {
        return manager.gewPresenterInstance(MainPresenter.class, MainState.class);
    }
}
