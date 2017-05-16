package com.example.pavl.currencycalc;

import com.example.pavl.currencycalc.model.Currency;
import com.example.pavl.currencycalc.model.CurrencyList;
import com.example.pavl.currencycalc.ui.MainView;
import com.example.pavl.currencycalc.ui.MainViewImpl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class MainFragment extends android.support.v4.app.Fragment implements Fetcher.Listener, MainView.Listener{
    private static final String ARG_URL = "arg-url";
    private static final String ARG_FILE_NAME = "arg-file-name";

    private MainView view;
    private Fetcher fetcher;
    private Currency original;
    private Currency result;
    private double amount = -1;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String url, String fileName) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_FILE_NAME, fileName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String url = "";
        String fileName = "";

        if (getArguments() != null) {
            url = getArguments().getString(ARG_URL);
            fileName = getArguments().getString(ARG_FILE_NAME);
        }

        view = new MainViewImpl(inflater, container);
        fetcher = new Fetcher(getContext(), fileName, url);
        return view.getRootView();
    }

    @Override
    public void onStart() {
        super.onStart();
        view.setListener(this);
        // Fetcher is single shot worker
        if (fetcher.getCurrencyList() == null) {
            fetcher.registerListener(this);
            fetcher.fetch();
        } else {
            view.bind(fetcher.getCurrencyList());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // pointless indeed since fragment retains state and can not be collected
        fetcher.unregisterListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        view.saveState(outState);
    }

    @Override
    public void onFetchError(Exception e) {
        // more complicated exception class handling should be put here
        Toast.makeText(getContext(), "failed to fetch data: " + e.getMessage(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDataFetched(CurrencyList list) {
        view.bind(list);
    }

    @Override
    public void originalCurrencyChanged(Currency currency) {
        this.original = currency;
        setResult();
    }

    @Override
    public void originalAmountChanged(double amount) {
        this.amount = amount;
        setResult();
    }

    @Override
    public void resultCurrencyChanged(Currency currency) {
        this.result = currency;
        setResult();
    }

    private void setResult (){
        if (original == null || result == null || amount < 0) {
            return;
        }
        view.setResult(CurrencyList.convert(original, result, amount));
    }
}
