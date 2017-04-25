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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        if (amount != currency.amount) return false;
        if (Double.compare(currency.rubbles, rubbles) != 0) return false;
        return charCode.equals(currency.charCode);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = charCode.hashCode();
        result = 31 * result + amount;
        temp = Double.doubleToLongBits(rubbles);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return charCode + " ( " + name + " )";
    }
}
