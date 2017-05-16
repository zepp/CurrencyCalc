package com.example.pavl.currencycalc.ui;

import com.example.pavl.currencycalc.Currency;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import java.util.List;

public class CurrencyAdapter extends ArrayAdapter<Currency> {

    class Holder
    {
        CheckedTextView view;
        Currency currency;

        public void bind (Currency currency)
        {
            this.currency = currency;
            view.setText(currency.getCharCode());
        }

        public void bindFull (Currency currency)
        {
            this.currency = currency;
            view.setText(currency.getCharCode() + " (" + currency.getName() + ")");
        }

        public Holder(View view) {
            this.view = (CheckedTextView)view;
        }
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Holder holder;

        View v = super.getView(position, convertView, parent);
        if (v.getTag() == null) {
            holder = new Holder(v);
            v.setTag(holder);
        }
        else
            holder = (Holder)v.getTag();
        holder.bindFull(getItem(position));
        return v;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Holder holder;

        View v = super.getView(position, convertView, parent);
        if (v.getTag() == null) {
            holder = new Holder(v);
            v.setTag(holder);
        }
        else
            holder = (Holder)v.getTag();
        holder.bind(getItem(position));
        return v;
    }

    public CurrencyAdapter(Context context, List<Currency> model)
    {
        super(context, android.R.layout.simple_spinner_dropdown_item, model);
    }
}
