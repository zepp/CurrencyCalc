package com.example.pavl.currencycalc.background;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {
    private static volatile EventHandler handler;
    private final List<ListenerWrapper> listeners = new ArrayList<>();

    private EventHandler() {
    }

    public static EventHandler getInstance() {
        if (handler == null) {
            synchronized (EventHandler.class) {
                if (handler == null) {
                    handler = new EventHandler();
                }
            }
        }
        return handler;
    }

    public boolean register(Listener listener) {
        synchronized (listeners) {
            return listeners.add(new ListenerWrapper(new Handler(Looper.myLooper()), listener));
        }
    }

    public void unregister(Listener listener) {
        synchronized (listeners) {
            for (int i = 0; i < listeners.size(); i++) {
                if (listeners.get(i).getListener().equals(listener)) {
                    listeners.remove(i);
                }
            }
        }
    }

    public void notifyError(final Throwable e) {
        synchronized (listeners) {
            for (ListenerWrapper listener : listeners) {
                listener.onError(e);
            }
        }
    }

    public void notifyDataUpdated() {
        synchronized (listeners) {
            for (ListenerWrapper listener : listeners) {
                listener.onDataUpdated();
            }
        }
    }

    public interface Listener {
        void onError(Throwable e);

        void onDataUpdated();
    }

    private class ListenerWrapper implements Listener {
        private final Handler handler;
        private final Listener listener;

        ListenerWrapper(Handler handler, Listener listener) {
            this.handler = handler;
            this.listener = listener;
        }

        public Listener getListener() {
            return listener;
        }

        @Override
        public void onError(final Throwable e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onError(e);
                }
            });
        }

        @Override
        public void onDataUpdated() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onDataUpdated();
                }
            });
        }
    }
}
