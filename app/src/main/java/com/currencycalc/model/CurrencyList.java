/*
 * Copyright (c) 2019 Pavel A. Sokolov
 */

package com.currencycalc.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Root(name = "ValCurs", strict = false)
public class CurrencyList {
    @Attribute(name = "Date")
    private String date = "";

    @ElementList(inline = true)
    private List<Currency> currencies = new ArrayList<>();

    public CurrencyList() {
    }

    public CurrencyList(String date) {
        this.date = date;
    }

    public static double convert(Currency from, Currency to, double amount) {
        if (from.equals(to))
            return amount;
        else
            return amount * (from.getRubbles() / to.getRubbles());
    }

    public String getDate() {
        return date;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void add(Currency currency) {
        currencies.add(currency);
    }

    public Currency get(int numCode) {
        for (Currency c : currencies) {
            if (c.getNumCode() == numCode) {
                return c;
            }
        }
        return null;
    }

    public void sort() {
        Collections.sort(currencies, (o1, o2) -> o1.getName().compareTo(o2.getName()));
    }
}
