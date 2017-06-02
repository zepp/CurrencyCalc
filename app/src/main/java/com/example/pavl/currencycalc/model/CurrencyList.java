package com.example.pavl.currencycalc.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Root(name = "ValCurs", strict = false)
public class CurrencyList {
    @Attribute(name = "Date")
    private String date = "";

    @ElementList(inline = true)
    private List<Currency> currencies = new ArrayList<>();

    public static double convert(Currency from, Currency to, double amount) {
        if (from.equals(to))
            return amount;
        else
            return amount * (from.getRubbles() / to.getRubbles());
    }

    public String getDate() {
        return date;
    }

    public void sort ()
    {
        currencies.sort(new Comparator<Currency>() {
            @Override
            public int compare(Currency o1, Currency o2) {
                return o1.getCharCode().compareTo(o2.getCharCode());
            }
        });
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }
}
