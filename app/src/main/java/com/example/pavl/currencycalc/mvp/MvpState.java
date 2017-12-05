package com.example.pavl.currencycalc.mvp;

import java.io.Serializable;

public abstract class MvpState implements Serializable {
    private boolean isChanged = true;

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean isChanged) {
        this.isChanged |= isChanged;
    }

    void clearChanged() {
        this.isChanged = false;
    }

    @Override
    public String toString() {
        return "MvpState {" + "changed: " + isChanged + '}';
    }
}
