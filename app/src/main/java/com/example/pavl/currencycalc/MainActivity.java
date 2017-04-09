package com.example.pavl.currencycalc;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private final String file = "rates.xml";
    private final String url = "http://www.cbr.ru/scripts/XML_daily.asp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FragmentManager manager;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.fragment_container) == null)
        {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_container, MainFragment.newInstance(url, file), null).commit();
        }
    }
}
