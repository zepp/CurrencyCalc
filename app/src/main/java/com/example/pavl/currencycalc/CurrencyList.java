package com.example.pavl.currencycalc;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "ValCurs", strict = false)
public class CurrencyList {
    @Attribute(name = "Date")
    private String date = "";

    @ElementList(inline = true)
    private List<Currency> currencies = new ArrayList<>();

    public static double convert(Currency from, Currency to, double amount) {
        if (from == to)
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
}
