package com.example.pavl.currencycalc.mvp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/* Базовый класс для всех фрагментов, которые реализуют паттерн MVP */
public abstract class MvpFragment<P extends MvpPresenter<S>, S extends MvpState> extends Fragment implements MvpView<P, S> {
    protected MvpPresenterManager manager;
    protected P presenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        manager = MvpPresenterManager.getInstance(getContext());
        presenter = onInitPresenter(manager);
        presenter.attach(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detach(this);
        manager.releasePresenter(presenter);
    }

    @Override
    public void finish() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (presenter.handleResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
