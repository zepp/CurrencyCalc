package com.example.pavl.currencycalc.mvp;

import android.os.Handler;
import android.util.Log;

public abstract class MvpPresenter<S extends MvpState> {
    protected final MvpView<S> view;
    protected final Handler handler;
    protected final String tag;
    protected S state;
    private boolean isInitialCommit;
    private boolean isInitialCommitSubmitted;

    public MvpPresenter(MvpView<S> view, Handler handler) {
        this.view = view;
        this.handler = handler;
        this.tag = view.getClass().getSimpleName();
    }

    public S getState() {
        return state;
    }

    public void setState(S state) {
        this.state = state;
        this.isInitialCommit = true;
        this.isInitialCommitSubmitted = false;
    }

    public boolean isInitialCommit() {
        return isInitialCommit;
    }

    public void commit() {
        if (state.isChanged() || !isInitialCommitSubmitted) {
            state.setChanged(false);
            isInitialCommitSubmitted = true;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (view.isReadyToHandle()) {
                        Log.d(tag, state.toString());
                        view.onStateChanged(state);
                    } else {
                        Log.d(tag, "is not ready to handle state change");
                    }
                    isInitialCommit = false;
                }
            });
        }
    }

    public abstract void onStart();

    public abstract void onStop();
}
