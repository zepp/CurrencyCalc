package com.example.pavl.currencycalc;

import org.simpleframework.xml.transform.Transform;

public class CommaDoubleTransform implements Transform<Double> {

    public Double read(String value) {
        return Double.valueOf(value.replace(',','.'));
    }

    public String write(Double value) {
        return value.toString().replace('.',',');
    }
}
