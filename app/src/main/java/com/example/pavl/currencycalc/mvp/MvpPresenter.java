package com.example.pavl.currencycalc.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public abstract class MvpPresenter<S extends MvpState> implements LifecycleObserver {
    protected final String tag = getClass().getSimpleName();
    protected final Context context;
    protected final List<AttachedView> views = new ArrayList<>();
    protected final Handler handler;
    protected volatile S state;

    // флаг указывающий на то, что данный представитель запущен. Используется методами stop/start
    private volatile int startCounter = 0;

    public MvpPresenter(Context applicationContext) {
        this.context = applicationContext;
        this.handler = new Handler(Looper.getMainLooper());
    }

    // добавляет представление в список клиентов текущего представителя
    public void attach(MvpView view) {
        synchronized (views) {
            views.add(new AttachedView(view).subscribe(this));
        }
        state.setInitial(true);
    }

    // удаляет представление из списка клиентов текущего представления
    public void detach(MvpView view) {
        synchronized (views) {
            for (AttachedView wrapper : views) {
                if (wrapper.isAttachedTo(view)) {
                    views.remove(wrapper.unsubscribe(this));
                    break;
                }
            }
        }
    }

    public S getState() {
        return state;
    }

    public void setState(S state) {
        state.setInitial(true);
        state.setChanged(false);
        this.state = state;
    }

    public boolean isStarted() {
        return startCounter > 0;
    }

    public void enableView(MvpView view, boolean value) {
        synchronized (views) {
            for (AttachedView wrapper : views) {
                if (wrapper.isAttachedTo(view)) {
                    wrapper.isEnabled = value;
                    break;
                }
            }
        }
    }

    // делает копию состояния и отправляет её на отображение
    // копия делается с целью избежания ситуации, когда состояние изменяется в процессе отображения
    public void commit() {
        try {
            if (state.isChanged() || state.isInitial()) {
                final S snapshot = (S) state.clone();
                state.clearChanged();
                synchronized (views) {
                    for (AttachedView wrapper : views) {
                        wrapper.commit(snapshot);
                    }
                }
            }
        } catch (CloneNotSupportedException e) {
        }
    }

    // метод для запуска представителя, атомарная операция
    public synchronized void start() {
        startCounter++;
        if (startCounter == 1) {
            Log.d(tag, "onStart");
            onStart();
        }
    }

    // метод для останова представителя, атомарная операция
    public synchronized void stop() {
        startCounter--;
        if (startCounter == 0) {
            Log.d(tag, "onStop");
            onStop();
        }
    }

    public void finish() {
        synchronized (views) {
            for (AttachedView wrapper : views) {
                wrapper.finish();
            }
        }
    }

    // метод для передачи результатов на обработку представителю
    // возвращаемые значения:
    // false - данные не требуют дальнейшей обработки
    // true - данные должны быть обработаны вызывающей стороной
    public boolean handleResult(int requestCode, int resultCode, Intent data) {
        if (isStarted()) {
            return onResult(requestCode, resultCode, data);
        } else {
            return true;
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected final void onLifecycleStart() {
        start();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected final void onLifecycleStop() {
        stop();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected final void onLifecycleResume() {
        commit();
    }

    protected abstract void onStart();

    protected abstract void onStop();

    protected boolean onResult(int requestCode, int resultCode, Intent data) {
        return true;
    }

    private class AttachedView {
        final MvpView view;
        final Lifecycle lifecycle;
        final String tag;

        // отображать состояние, когда LifecycleOwner перешёл в состояние Resumed
        boolean isEnabled = true;

        AttachedView(MvpView view) {
            this.view = view;
            this.lifecycle = view.getLifecycle();
            this.tag = view.getClass().getSimpleName();
        }

        /* отображает состояние на представление, если выставлен флаг onResumeTransition, значит
         * функция вызвана из метода onResume */
        void commit(final S state) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (isEnabled && lifecycle.getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                        Log.d(tag, state.toString());
                        view.onStateChanged(state);
                        getState().setInitial(false);
                    } else {
                        Log.d(tag, "view is not ready to handle state change");
                    }
                }
            });
        }

        boolean isAttachedTo(MvpView view) {
            return this.view.equals(view);
        }

        AttachedView subscribe(LifecycleObserver observer) {
            lifecycle.addObserver(observer);
            return this;
        }

        AttachedView unsubscribe(LifecycleObserver observer) {
            lifecycle.removeObserver(observer);
            return this;
        }

        void finish() {
            view.finish();
        }
    }
}
