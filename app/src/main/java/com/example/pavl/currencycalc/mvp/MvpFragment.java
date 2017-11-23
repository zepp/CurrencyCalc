package com.example.pavl.currencycalc.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

public abstract class MvpFragment<P extends MvpPresenter<S>, S extends MvpState> extends Fragment implements MvpView<S> {
    private final static String MVP_FRAGMENT_STATE = "mvp-fragment-state";
    private final String tag = this.getTag();
    protected P presenter;
    protected S state;
    private boolean isAutoCommit = true;
    private boolean isReadyToHandle;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = onCreatePresenter();
        if (savedInstanceState == null) {
            state = onCreateState();
        } else {
            state = (S) savedInstanceState.getSerializable(MVP_FRAGMENT_STATE);
            Log.d(tag, "state restored: " + presenter.getState());
        }
        presenter.setState(state);
    }

    @Override
    public void onStart() {
        super.onStart();
        isReadyToHandle = true;
        presenter.setState(state);
        presenter.onStart();
        if (isAutoCommit) {
            presenter.commit();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isReadyToHandle = false;
        presenter.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MVP_FRAGMENT_STATE, presenter.getState());
        Log.d(tag, "state saved: " + presenter.getState());
    }

    public abstract S onCreateState();

    public abstract P onCreatePresenter();

    public boolean isAutoCommit() {
        return isAutoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        isAutoCommit = autoCommit;
    }

    public boolean isReadyToHandle() {
        return isReadyToHandle;
    }

}
