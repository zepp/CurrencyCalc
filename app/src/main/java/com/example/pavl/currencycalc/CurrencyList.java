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

    public List<String> getCharCodes()
    {
        List<String> list = new ArrayList<>(curList.size());
        for(Currency cur: curList) {
            list.add(cur.getCharCode());
        }
        return list;
    }

    public double getRubbles (String charCode) throws InvalidParameterException
    {
        for(Currency cur: curList) {
            if (cur.getCharCode() == charCode)
                return cur.getRubbles();
        }
        throw new InvalidParameterException("char code \"" + charCode + "\" is unknown");
    }
}
