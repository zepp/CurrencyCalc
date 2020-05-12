/*
 * Copyright (c) 2019 Pavel A. Sokolov
 */

package com.currencycalc.main;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.currencycalc.R;
import com.currencycalc.model.Currency;

import java.util.Collections;
import java.util.List;

class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder> {
    private final MainPresenter presenter;
    private final ItemListener listener;
    private List<Currency> list = Collections.emptyList();
    private int numCode;

    CurrencyAdapter(MainPresenter presenter, ItemListener listener) {
        setHasStableIds(true);
        this.presenter = presenter;
        this.listener = listener;
    }

    void setNumCode(int numCode) {
        this.numCode = numCode;
    }

    void replace(List<Currency> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public CurrencyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CurrencyHolder(inflater.inflate(R.layout.currency_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CurrencyHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getNumCode();
    }

    public interface ItemListener {
        void onItemSelected(Currency currency);
    }

    public class CurrencyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView name;
        final ImageView flag;
        Currency currency;

        CurrencyHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.currency_name);
            flag = itemView.findViewById(R.id.currency_flag);
        }

        void bind(Currency currency) {
            this.currency = currency;
            itemView.setOnClickListener(this);
            itemView.setBackgroundColor(currency.getNumCode() == numCode ? Color.LTGRAY : Color.TRANSPARENT);
            name.setText(currency.getName());
            flag.setImageDrawable(presenter.getFlagDrawable(currency));
        }

        @Override
        public void onClick(View v) {
            listener.onItemSelected(currency);
        }
    }
}
