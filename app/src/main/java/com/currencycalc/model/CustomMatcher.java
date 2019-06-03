/*
 * Copyright (c) 2019 Pavel A. Sokolov
 */

package com.currencycalc.model;

import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

public class CustomMatcher implements Matcher {
    @Override
    public Transform match(Class type) throws Exception {
        if (type.equals(Double.TYPE))
            return new CommaDoubleTransform();
        return null;
    }
}
