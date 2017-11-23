package com.example.pavl.currencycalc.main;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pavl.currencycalc.R;
import com.example.pavl.currencycalc.model.Currency;

class CurrencyAdapter extends ArrayAdapter<Currency> {
    private Resources resources;
    private String packageName;

    CurrencyAdapter(Context context) {
        super(context, R.layout.currency_item);
        this.resources = context.getResources();
        this.packageName = context.getPackageName();
    }

    @DrawableRes
    private int getFlagResourceId(String charCode) {
        String name = "ic_" + charCode.toLowerCase();
        int id = resources.getIdentifier(name, "drawable", packageName);
        if (id == 0) {
            return R.drawable.flag_unknown;
        }
        return id;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Currency currency = getItem(position);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_drop_item, parent, false);
        TextView currencyName = (TextView) v.findViewById(R.id.currency_name);
        ImageView currencyFlag = (ImageView) v.findViewById(R.id.currency_flag);
        currencyName.setText(currency.getName());
        currencyFlag.setImageResource(getFlagResourceId(currency.getCharCode()));
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
