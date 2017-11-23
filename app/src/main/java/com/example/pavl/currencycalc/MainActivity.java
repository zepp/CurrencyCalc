package com.example.pavl.currencycalc;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pavl.currencycalc.main.MainFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.fragment_container) == null) {
            manager.beginTransaction().add(R.id.fragment_container, MainFragment.newInstance(), null).commit();
        }
    }
}
