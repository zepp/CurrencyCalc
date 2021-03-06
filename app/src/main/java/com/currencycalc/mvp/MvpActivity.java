/*
 * Copyright (c) 2019 Pavel A. Sokolov
 */

package com.currencycalc.mvp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.currencycalc.domain.Executor;

import java.util.concurrent.ExecutorService;

/* Базовый класс для всех Activity, которые реализуют паттерн MVP */
public abstract class MvpActivity<P extends MvpBasePresenter<S>, S extends MvpState> extends AppCompatActivity
        implements MvpView<P, S>, View.OnClickListener {
    protected ExecutorService executor;
    protected MvpStateHandler<S> stateHandler;
    protected MvpPresenterManager manager;
    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        executor = Executor.getExecutor();
        stateHandler = new MvpStateHandler<>(this);
        getLifecycle().addObserver(stateHandler);
        manager = MvpPresenterManager.getInstance(this);
        presenter = onInitPresenter(manager);
        presenter.attach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach(this);
        if (isFinishing()) {
            manager.releasePresenter(presenter);
        }
        getLifecycle().removeObserver(stateHandler);

    }

    @Override
    public void post(S state) {
        stateHandler.post(state);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        executor.execute(() -> presenter.onOptionsItemSelected(item.getItemId()));
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        executor.execute(() -> presenter.onActivityResult(requestCode, resultCode, data));
    }

    @Override
    public void onClick(View v) {
        executor.execute(() -> presenter.onViewClicked(v.getId()));
    }
}
