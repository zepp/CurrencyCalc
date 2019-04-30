package com.example.pavl.currencycalc.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

/* Базовый класс для всех фрагментов, которые реализуют паттерн MVP */
public abstract class MvpFragment<P extends MvpBasePresenter<S>, S extends MvpState> extends Fragment
        implements MvpView<P, S>, View.OnClickListener, AdapterView.OnItemSelectedListener {
    protected MvpStateHandler<P, S> stateHandler;
    protected MvpPresenterManager manager;
    protected P presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateHandler = new MvpStateHandler<>(this);
        getLifecycle().addObserver(stateHandler);
        manager = MvpPresenterManager.getInstance(getContext());
        presenter = onInitPresenter(manager);
        presenter.attach(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detach(this);
        manager.releasePresenter(presenter);
        getLifecycle().removeObserver(stateHandler);
    }

    @Override
    public void handleNewState(S state) {
        stateHandler.post(state);
    }

    @Override
    public void finish() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.execute(() -> presenter.onActivityResult(requestCode, resultCode, data));
    }

    @Override
    public void onClick(View v) {
        presenter.execute(() -> presenter.onViewClicked(v.getId()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        presenter.execute(() -> presenter.onItemSelected(view.getId(), position, id));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
