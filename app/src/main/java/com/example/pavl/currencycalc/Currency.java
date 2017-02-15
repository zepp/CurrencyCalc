package com.example.pavl.currencycalc;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Valute")
public class Currency {

    @Attribute(name="ID")
    private String _ID = "";

    @Element(name="NumCode")
    private int _NumCode = 0;

    @Element(name="CharCode")
    private String _CharCode = "RUB";

    @Element(name="Nominal")
    private int _Nominal = 1;

    @Element(name="Name")
    private String _Name = "Rubble";

    @Element(name="Value")
    private double _Rubbles = 1.0;

    public int getNumCode ()
    {
        return _NumCode;
    }

    public String getCharCode ()
    {
        return _CharCode;
    }

    public int getNominal ()
    {
        return _Nominal;
    }

    public String getName ()
    {
        return _Name;
    }

    public double getRubbles ()
    {
        return _Rubbles;
    }
}
