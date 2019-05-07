package com.example.pavl.currencycalc.main;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pavl.currencycalc.R;
import com.example.pavl.currencycalc.model.Currency;

import java.util.List;

class CurrencyAdapter extends ArrayAdapter<Currency> {
    private final MainPresenter presenter;
    private int numCode;

    CurrencyAdapter(MainPresenter presenter, @NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.presenter = presenter;
    }

    void setNumCode(int numCode) {
        this.numCode = numCode;
    }

    void replace(List<Currency> list) {
        clear();
        addAll(list);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Currency currency = getItem(position);
        View v = super.getDropDownView(position, convertView, parent);
        v.setBackgroundColor(currency.getNumCode() == numCode ? Color.LTGRAY : Color.TRANSPARENT);
        TextView currencyName = v.findViewById(R.id.currency_name);
        ImageView currencyFlag = v.findViewById(R.id.currency_flag);
        currencyName.setText(currency.getName());
        currencyFlag.setImageDrawable(presenter.getFlagDrawable(currency));
        return v;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CheckedTextView v = (CheckedTextView) super.getView(position, convertView, parent);
        v.setText(getItem(position).getCharCode());
        return v;
    }
}
