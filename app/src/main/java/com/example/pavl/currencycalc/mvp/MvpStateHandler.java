package com.example.pavl.currencycalc.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Deque;

final class MvpStateHandler<P extends MvpPresenter<S>, S extends MvpState> implements LifecycleObserver {
    private final static int QUEUE_SIZE = 8;
    private final String tag = getClass().getSimpleName();
    private final MvpView<P, S> view;
    private final Deque<S> queue = new ArrayDeque<>();
    private boolean isResumed;
    private boolean isEnabled = true;

    public MvpStateHandler(MvpView<P, S> view) {
        this.view = view;
    }

    void setEnabled(boolean enabled) {
        if (isEnabled != enabled) {
            isEnabled = enabled;
            Log.d(tag, "event handling is " + (isResumed() ? "enabled" : "disabled"));
            if (isResumed()) {
                onResumed();
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResumed() {
        isResumed = true;
        if (isResumed()) {
            flushQueue();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPaused() {
        isResumed = false;
    }

    final boolean isResumed() {
        return isResumed && isEnabled;
    }

    boolean post(S state) {
        if (isResumed()) {
            view.onStateChanged(state);
        } else {
            while (queue.size() > QUEUE_SIZE) {
                queue.pollLast();
            }
            return queue.offer(state);
        }
        return true;
    }

    protected void flushQueue() {
        Log.d(tag, "flushing event queue");
        for (S event = queue.pollLast(); event != null; event = queue.pollLast()) {
            view.onStateChanged(event);
        }
    }
}
