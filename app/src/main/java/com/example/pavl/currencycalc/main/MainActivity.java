package com.example.pavl.currencycalc.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.pavl.currencycalc.R;
import com.example.pavl.currencycalc.mvp.MvpActivity;
import com.example.pavl.currencycalc.mvp.MvpPresenterManager;

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
