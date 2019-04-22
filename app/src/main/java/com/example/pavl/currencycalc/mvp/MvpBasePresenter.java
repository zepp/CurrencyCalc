package com.example.pavl.currencycalc.mvp;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class MvpBasePresenter<S extends MvpState> implements LifecycleObserver, MvpPresenter<S>{
    protected final String tag = getClass().getSimpleName();
    protected final Context context;
    protected final List<MvpView> views = new CopyOnWriteArrayList<>();
    protected final Handler handler;
    protected final S state;

    public MvpBasePresenter(Context context, S state) {
        this.context = context;
        this.state = state;
        this.handler = new Handler(Looper.getMainLooper());
    }

    // добавляет представление в список клиентов текущего представителя
    public void attach(MvpView view) {
        synchronized (views) {
            views.add(view);
            if (views.size() == 1) {
                handler.post(() -> onStart());
            }
        }
        handler.post(() -> view.handleNewState(getStateSnapshot()));
    }

    // удаляет представление из списка клиентов текущего представления
    public void detach(MvpView view) {
        synchronized (views) {
            views.remove(view);
            if (views.size() == 0) {
                handler.post(() -> onStop());
            }
        }
    }

    @Override
    public boolean isDetached() {
        return views.isEmpty();
    }

    public S getState() {
        return (S)getStateSnapshot();
    }

    // делает копию состояния и отправляет её на отображение
    // копия делается с целью избежания ситуации, когда состояние изменяется в процессе отображения
    public void commit() {
        if (state.isChanged() || state.isInitial()) {
            MvpState snapshot = getStateSnapshot();
            state.clearChanged();
            for (MvpView view : views) {
                handler.post(() -> view.handleNewState(snapshot));
            }
        }
    }

    public void finish() {
        synchronized (views) {
            for (MvpView view : views) {
                handler.post(view::finish);
            }
        }
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(tag,"onActivityResult("+requestCode+ ", " + resultCode + ", " + data + ")");
        return true;
    }

    @CallSuper
    protected void onStart() {
        Log.d(tag, "onStart");
    }

    @CallSuper
    protected void onStop() {
        Log.d(tag, "onStop");
    }

    protected MvpState getStateSnapshot() {
        try {
            return state.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
