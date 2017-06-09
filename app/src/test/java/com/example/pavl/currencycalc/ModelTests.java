package com.example.pavl.currencycalc;

import com.example.pavl.currencycalc.model.Currency;
import com.example.pavl.currencycalc.model.CurrencyList;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ModelTests {
    private final static SimpleDateFormat formatter = new SimpleDateFormat("dd.mm.yyyy", Locale.getDefault());
    private final static int numCodeAMD = 51;
    private final static int numCodeUSD = 840;
    private final static int numCodeGBP = 826;
    private final static String nameAMD = "Армянских драмов";
    private final static String nameUSD = "Доллар США";

    @Test
    public void currencyTest() throws Exception {
        Currency amd1 = new Currency("AMD", numCodeAMD, nameAMD, 100, 11.8139);
        Currency amd2 = new Currency("AMD", numCodeAMD, nameAMD, 100, 11.8139);
        Currency amd3 = new Currency("AMD", numCodeAMD, "", 100, 11.0);
        assertEquals(amd1, amd2);
        assertNotEquals(amd1, amd3);
        assertEquals(amd1.hashCode(), amd2.hashCode());
        assertNotEquals(amd1.hashCode(), amd3.hashCode());
        assertThat(amd1.numCodeEquals(amd3), is(true));
    }

    @Test
    public void currencyListTest () throws Exception {
        CurrencyList list = new CurrencyList(formatter.format(Calendar.getInstance().getTime()));
        Currency amd = new Currency("AMD", numCodeAMD, nameAMD, 100, 11.8139);
        Currency usd = new Currency("USD", numCodeUSD, nameUSD, 1, 57.0020);
        Currency gbp = new Currency("GBP", numCodeGBP, ",", 1, 72.5464);
        assertEquals(CurrencyList.convert(usd, amd, 1), 482.499, 0.001);
        list.add(usd);
        list.add(amd);
        list.add(gbp);
        list.sort();
        assertEquals(list.getCurrencies().get(0), amd);
        assertEquals(list.getCurrencies().get(1), gbp);
        assertEquals(list.getCurrencies().get(2), usd);
        assertEquals(list.get(numCodeUSD), usd);
        assertEquals(list.get(numCodeAMD), amd);
        assertEquals(list.get(numCodeGBP), gbp);
        assertEquals(list.get(0), null);
    }
}
