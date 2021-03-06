/*
 * Copyright (c) 2019 Pavel A. Sokolov
 */

package com.currencycalc.main;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;

import com.currencycalc.R;
import com.currencycalc.mvp.MvpActivity;
import com.currencycalc.mvp.MvpPresenterManager;

public class MainActivity extends MvpActivity<MainPresenter, MainState> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager manager = getSupportFragmentManager();
        setSupportActionBar(findViewById(R.id.toolbar));
        if (manager.getBackStackEntryCount() == 0) {
            Fragment fragment = MainFragment.newInstance();
            manager.beginTransaction()
                    .replace(R.id.fragment_container, fragment, fragment.getTag())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onStateChanged(MainState state) {
    }

    @Override
    public MainPresenter onInitPresenter(MvpPresenterManager manager) {
        return manager.gewPresenterInstance(MainPresenter.class, MainState.class);
    }
}
