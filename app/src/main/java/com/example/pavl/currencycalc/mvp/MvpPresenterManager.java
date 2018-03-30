package com.example.pavl.currencycalc.mvp;


import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/* данный класс управляет представителями и позволяет для любого представления получить
* соответствующего представителя */
public final class MvpPresenterManager {
    private static volatile MvpPresenterManager manager;
    private final Context context;
    private final Map<String, MvpPresenter> map;

    private MvpPresenterManager(Context context) {
        this.context = context;
        this.map = new HashMap<>();
    }

    public static MvpPresenterManager getInstance(Context applicationContext) {
        if (manager == null) {
            synchronized (MvpPresenterManager.class) {
                if (manager == null) {
                    manager = new MvpPresenterManager(applicationContext);
                }
            }
        }
        return manager;
    }

    // удобная обертка для получения представителя внутри активности или фрагмента
    public <P extends MvpPresenter<S>, S extends MvpState> P getMyPresenter(MvpView<P, S> view) {
        return (P) getPresenterInstance(view);
    }

    // создаёт или возвращает ссылку на представителя
    public synchronized MvpPresenter getPresenterInstance(MvpView view) {
        String name = view.getClass().getName();
        MvpPresenter presenter = map.get(name);
        if (presenter == null) {
            presenter = view.onCreatePresenter(context);
            map.put(name, presenter);
        }
        if (presenter.getState() == null) {
            presenter.setState(view.onCreateState());
        }
        return presenter;
    }

    // удаляет ссылку на представителя, делая его таким образом доступным для GC
    public synchronized void releasePresenter(MvpView view) {
        map.remove(view.getClass().getName());
    }
}
