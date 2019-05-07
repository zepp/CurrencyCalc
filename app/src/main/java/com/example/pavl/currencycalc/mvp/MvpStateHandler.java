package com.example.pavl.currencycalc.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

final class MvpStateHandler<P extends MvpPresenter<S>, S extends MvpState> implements LifecycleObserver {
    private final static int QUEUE_SIZE = 8;
    private final String tag = getClass().getSimpleName();
    private final MvpView<P, S> view;
    private final Queue<S> queue = new LinkedList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isResumed;

    MvpStateHandler(MvpView<P, S> view) {
        this.view = view;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResumed() {
        isResumed = true;
        if (isResumed) {
            flushQueue();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPaused() {
        isResumed = false;
    }

    void post(S state) {
        if (isResumed) {
            view.onStateChanged(state);
        } else {
            queue.offer(state);
            while (queue.size() > QUEUE_SIZE) {
                queue.poll();
            }
        }
    }

    private void flushQueue() {
        Log.d(tag, "flushing event queue");
        while (!queue.isEmpty()) {
            handler.post(() -> view.onStateChanged(queue.poll()));
        }
    }
}
