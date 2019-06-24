/*
 * Copyright (c) 2019 Pavel A. Sokolov
 */

package com.currencycalc.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.currencycalc.domain.Executor;

import java.util.concurrent.ExecutorService;

public abstract class MvpDialogFragment<P extends MvpPresenter<S>, S extends MvpState> extends DialogFragment implements MvpView<P, S>, View.OnClickListener {
    protected ExecutorService executor;
    protected MvpStateHandler<S> stateHandler;
    protected MvpPresenterManager manager;
    protected P presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executor = Executor.getExecutor();
        stateHandler = new MvpStateHandler<>(this);
        getLifecycle().addObserver(stateHandler);
        manager = MvpPresenterManager.getInstance(getContext());
        presenter = onInitPresenter(manager);
        presenter.attach(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detach(this);
        manager.releasePresenter(presenter);
        getLifecycle().removeObserver(stateHandler);
    }

    @Override
    public void post(S state) {
        stateHandler.post(state);
    }

    @Override
    public void finish() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onClick(View v) {
        executor.execute(() -> presenter.onViewClicked(v.getId()));
    }
}
