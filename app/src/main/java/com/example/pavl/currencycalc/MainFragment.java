package com.example.pavl.currencycalc;

import com.example.pavl.currencycalc.background.Service;
import com.example.pavl.currencycalc.debug.LifeCycleLoggingFragment;
import com.example.pavl.currencycalc.model.Currency;
import com.example.pavl.currencycalc.model.CurrencyList;
import com.example.pavl.currencycalc.model.CustomMatcher;
import com.example.pavl.currencycalc.ui.MainView;
import com.example.pavl.currencycalc.ui.MainViewImpl;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;


public class MainFragment extends LifeCycleLoggingFragment implements MainView.Controller {
    private final static String TAG = MainFragment.class.getSimpleName();
    private Receiver receiver;
    private LocalBroadcastManager manager;
    private MainView view;
    private CurrencyList list;
    private Currency original;
    private Currency resulting;
    private double amount = -1;

    public MainFragment() {
        // Required empty public constructor
        Log.d(TAG, "MainFragment");
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        list = loadCurrencies();
        receiver = new Receiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = new MainViewImpl(getActivity(), inflater, container);
        return view.getRootView();
    }

    @Override
    public void onStart() {
        super.onStart();

        view.setController(this);
        view.start();

        if (list != null) {
            view.bind(list);
            view.setState(original, resulting, amount);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Service.DATA_UPDATED);
        manager = LocalBroadcastManager.getInstance(getContext());
        manager.registerReceiver(receiver, filter);

        if (!BuildConfig.DEBUG) {
            getContext().startService(Service.getIntent(getContext()));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        view.stop();
        manager.unregisterReceiver(receiver);
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
        } else {
            return super.onOptionsItemSelected(item);
        }
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
        this.resulting = currency;
        setResult();
    }

    private void setResult() {
        if (original == null || resulting == null || amount < 0) {
            return;
        }
        view.setResult(CurrencyList.convert(original, resulting, amount));
    }

    private CurrencyList loadCurrencies() {
        CurrencyList list = null;
        try {
            File file = Service.getFile(getContext());
            if (file.exists()) {
                Serializer serializer = new Persister(new CustomMatcher());
                list = serializer.read(CurrencyList.class, file);
                Log.d(TAG, "data updated");
            } else {
                Log.w(TAG, "file does not exist");
            }
        } catch (Exception e) {
            String msg = "error: " + e.getMessage();
            Log.e(TAG, msg);
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        }
        return list;
    }

    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Service.DATA_UPDATED)) {
                list = loadCurrencies();
                if (list != null) {
                    view.bind(list);
                }
            }
        }
    }
}
