/*
 * Copyright (c) 2019 Pavel A. Sokolov
 */

package com.example.pavl.currencycalc.model;

import org.simpleframework.xml.transform.Transform;

class CommaDoubleTransform implements Transform<Double> {

    public Double read(String value) {
        return Double.valueOf(value.replace(',','.'));
    }

    public String write(Double value) {
        return value.toString().replace('.',',');
    }
}
