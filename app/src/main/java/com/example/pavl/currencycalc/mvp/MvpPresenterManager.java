package com.example.pavl.currencycalc.mvp;


import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* данный класс управляет представителями и позволяет для любого представления получить
* соответствующего представителя */
public final class MvpPresenterManager {
    private static volatile MvpPresenterManager manager;
    private final String tag = getClass().getSimpleName();
    private final Context context;
    private final Map<Integer, MvpPresenter> map;

    private MvpPresenterManager(Context context) {
        this.context = context;
        this.map = Collections.synchronizedMap(new HashMap<>());
    }

    public static MvpPresenterManager getInstance(Context context) {
        if (manager == null) {
            synchronized (MvpPresenterManager.class) {
                if (manager == null) {
                    manager = new MvpPresenterManager(context.getApplicationContext());
                }
            }
        }
        return manager;
    }

    // создаёт или возвращает ссылку на представителя
    public <P extends MvpPresenter<S>, S extends MvpState> P newPresenterInstance(Class<P> pClass,
                                                                                  Class<S> sClass) {
        S state = newState(sClass);
        P presenter = newPresenter(pClass, sClass, state);
        map.put(presenter.hashCode(), presenter);
        Log.d(tag, "new presenter: " + presenter);
        return presenter;
    }

    // удаляет ссылку на представителя, делая его таким образом доступным для GC
    public <P extends MvpPresenter<S>, S extends MvpState> void releasePresenter(P presenter) {
        if (presenter.isDetached()) {
            Log.d(tag, "release presenter: " + presenter);
            map.remove(presenter.hashCode());
        }
    }

    private <P extends MvpPresenter<S>, S extends MvpState> P newPresenter(Class<P> pClass, Class<S> sClass, S state) {
        try {
            return pClass.getConstructor(Context.class, sClass).newInstance(context, state);
        } catch (NoSuchMethodException|IllegalAccessException|InstantiationException| InvocationTargetException e) {
            Log.e(tag, e.getLocalizedMessage());
            return null;
        }
    }

    private <S extends MvpState> S newState(Class<S> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException|IllegalAccessException|InstantiationException| InvocationTargetException e) {
            Log.e(tag, e.getLocalizedMessage());
            return null;
        }
    }
}
