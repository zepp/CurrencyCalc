/*
 * Copyright (c) 2019 Pavel A. Sokolov
 */

package com.currencycalc.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Valute", strict = false)
public class Currency {
    @Element(name = "NumCode")
    private int numCode = 0;

    @Element(name = "CharCode")
    private String charCode = "RUB";

    @Element(name = "Nominal")
    private int amount = 1;

    @Element(name = "Name")
    private String name = "Rubble";

    @Element(name = "Value")
    private double rubbles = 1.0;

    public int getNumCode() {
        return numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public String getName() {
        return name;
    }

    public double getRubbles() {
        return rubbles / amount;
    }

    public Currency(){
    }

    public Currency(String charCode, int numCode, String name, int amount, double rubbles) {
        this.numCode = numCode;
        this.charCode = charCode;
        this.amount = amount;
        this.name = name;
        this.rubbles = rubbles;
    }

    public boolean numCodeEquals (Currency currency) {
        return numCode == currency.numCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        if (numCode != currency.numCode) return false;
        if (amount != currency.amount) return false;
        if (Double.compare(currency.rubbles, rubbles) != 0) return false;
        return charCode.equals(currency.charCode);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = charCode.hashCode();
        result = 31 * result + numCode;
        result = 31 * result + amount;
        temp = Double.doubleToLongBits(rubbles);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "numCode=" + numCode +
                ", charCode='" + charCode + '\'' +
                ", amount=" + amount +
                ", name='" + name + '\'' +
                ", rubbles=" + rubbles +
                '}';
    }
}
