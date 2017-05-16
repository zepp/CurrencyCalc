package com.example.pavl.currencycalc.ui;

import android.os.Bundle;
import android.view.View;

public interface BaseView {
    View getRootView();
    void saveState(Bundle state);
}
