package com.example.pavl.currencycalc.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pavl.currencycalc.R;
import com.example.pavl.currencycalc.model.Currency;

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
        return new CurrencyHolder(inflater.inflate(R.layout.currency_drop_item, parent, false));
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

    public class CurrencyHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final ImageView flag;

        CurrencyHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.currency_name);
            flag = itemView.findViewById(R.id.currency_flag);
        }

        void bind(Currency currency) {
            name.setText(currency.getName());
            flag.setImageDrawable(presenter.getFlagDrawable(currency));
        }
    }
}
