package com.example.pavl.currencycalc;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Root(name = "ValCurs")
public class CurrencyList {
    @Attribute(name="Date")
    private String _Date = "";

    @Attribute(name="name")
    private String _Name = "";

    @ElementList(inline=true)
    private List<Currency> curList = new ArrayList<>();

    public static double convert(Currency from, Currency to, double amount)
    {
        return amount * (from.getRubbles() / to.getRubbles());
    }

    public List<Currency> getCurrencies()
    {
        return curList;
    }
}
