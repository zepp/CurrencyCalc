package com.example.pavl.currencycalc.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pavl.currencycalc.R;
import com.example.pavl.currencycalc.domain.Controller;
import com.example.pavl.currencycalc.model.Currency;

class CurrencyAdapter extends ArrayAdapter<Currency> {
    private final Controller controller;

    CurrencyAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.controller = Controller.getInstance(context);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Currency currency = getItem(position);
        View v = super.getDropDownView(position, convertView, parent);
        TextView currencyName = (TextView) v.findViewById(R.id.currency_name);
        ImageView currencyFlag = (ImageView) v.findViewById(R.id.currency_flag);
        currencyName.setText(currency.getName());
        currencyFlag.setImageDrawable(controller.getFlagDrawable(currency.getCharCode()));
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
