package com.example.pavl.currencycalc;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Valute", strict = false)
public class Currency {
    @Element(name = "CharCode")
    private String charCode = "RUB";

    @Element(name = "Nominal")
    private int amount = 1;

    @Element(name = "Name")
    private String name = "Rubble";

    @Element(name = "Value")
    private double rubbles = 1.0;

    public String getCharCode() {
        return charCode;
    }

    public String getName() {
        return name;
    }

    public double getRubbles() {
        return rubbles / amount;
    }
}
