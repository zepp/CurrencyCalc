package com.currencycalc;

import com.currencycalc.model.Currency;
import com.currencycalc.model.CurrencyList;
import com.currencycalc.model.CustomMatcher;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

public class ParserTests {
    private final static String xml = "<?xml version=\"1.0\" encoding=\"utf8\" ?>\n" +
            "<ValCurs Date=\"14.06.2017\" name=\"Foreign Currency Market\">\n" +
            "<Valute ID=\"R01010\">\n" +
            "\t<NumCode>036</NumCode>\n" +
            "\t<CharCode>AUD</CharCode>\n" +
            "\t<Nominal>1</Nominal>\n" +
            "\t<Name>Австралийский доллар</Name>\n" +
            "\t<Value>42,9554</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01020A\">\n" +
            "\t<NumCode>944</NumCode>\n" +
            "\t<CharCode>AZN</CharCode>\n" +
            "\t<Nominal>1</Nominal>\n" +
            "\t<Name>Азербайджанский манат</Name>\n" +
            "\t<Value>33,4330</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01035\">\n" +
            "\t<NumCode>826</NumCode>\n" +
            "\t<CharCode>GBP</CharCode>\n" +
            "\t<Nominal>1</Nominal>\n" +
            "\t<Name>Фунт стерлингов Соединенного королевства</Name>\n" +
            "\t<Value>72,3093</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01060\">\n" +
            "\t<NumCode>051</NumCode>\n" +
            "\t<CharCode>AMD</CharCode>\n" +
            "\t<Nominal>100</Nominal>\n" +
            "\t<Name>Армянских драмов</Name>\n" +
            "\t<Value>11,7947</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01090B\">\n" +
            "\t<NumCode>933</NumCode>\n" +
            "\t<CharCode>BYN</CharCode>\n" +
            "\t<Nominal>1</Nominal>\n" +
            "\t<Name>Белорусский рубль</Name>\n" +
            "\t<Value>30,3550</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01100\">\n" +
            "\t<NumCode>975</NumCode>\n" +
            "\t<CharCode>BGN</CharCode>\n" +
            "\t<Nominal>1</Nominal>\n" +
            "\t<Name>Болгарский лев</Name>\n" +
            "\t<Value>32,6354</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01115\">\n" +
            "\t<NumCode>986</NumCode>\n" +
            "\t<CharCode>BRL</CharCode>\n" +
            "\t<Nominal>1</Nominal>\n" +
            "\t<Name>Бразильский реал</Name>\n" +
            "\t<Value>17,1507</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01135\">\n" +
            "\t<NumCode>348</NumCode>\n" +
            "\t<CharCode>HUF</CharCode>\n" +
            "\t<Nominal>100</Nominal>\n" +
            "\t<Name>Венгерских форинтов</Name>\n" +
            "\t<Value>20,7897</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01200\">\n" +
            "\t<NumCode>344</NumCode>\n" +
            "\t<CharCode>HKD</CharCode>\n" +
            "\t<Nominal>10</Nominal>\n" +
            "\t<Name>Гонконгских долларов</Name>\n" +
            "\t<Value>72,9666</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01215\">\n" +
            "\t<NumCode>208</NumCode>\n" +
            "\t<CharCode>DKK</CharCode>\n" +
            "\t<Nominal>10</Nominal>\n" +
            "\t<Name>Датских крон</Name>\n" +
            "\t<Value>85,8469</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01235\">\n" +
            "\t<NumCode>840</NumCode>\n" +
            "\t<CharCode>USD</CharCode>\n" +
            "\t<Nominal>1</Nominal>\n" +
            "\t<Name>Доллар США</Name>\n" +
            "\t<Value>56,9096</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01239\">\n" +
            "\t<NumCode>978</NumCode>\n" +
            "\t<CharCode>EUR</CharCode>\n" +
            "\t<Nominal>1</Nominal>\n" +
            "\t<Name>Евро</Name>\n" +
            "\t<Value>63,7729</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01270\">\n" +
            "\t<NumCode>356</NumCode>\n" +
            "\t<CharCode>INR</CharCode>\n" +
            "\t<Nominal>100</Nominal>\n" +
            "\t<Name>Индийских рупий</Name>\n" +
            "\t<Value>88,3895</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01335\">\n" +
            "\t<NumCode>398</NumCode>\n" +
            "\t<CharCode>KZT</CharCode>\n" +
            "\t<Nominal>100</Nominal>\n" +
            "\t<Name>Казахстанских тенге</Name>\n" +
            "\t<Value>18,0105</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01350\">\n" +
            "\t<NumCode>124</NumCode>\n" +
            "\t<CharCode>CAD</CharCode>\n" +
            "\t<Nominal>1</Nominal>\n" +
            "\t<Name>Канадский доллар</Name>\n" +
            "\t<Value>42,9183</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01370\">\n" +
            "\t<NumCode>417</NumCode>\n" +
            "\t<CharCode>KGS</CharCode>\n" +
            "\t<Nominal>100</Nominal>\n" +
            "\t<Name>Киргизских сомов</Name>\n" +
            "\t<Value>83,1404</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01375\">\n" +
            "\t<NumCode>156</NumCode>\n" +
            "\t<CharCode>CNY</CharCode>\n" +
            "\t<Nominal>10</Nominal>\n" +
            "\t<Name>Китайских юаней</Name>\n" +
            "\t<Value>83,7115</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01500\">\n" +
            "\t<NumCode>498</NumCode>\n" +
            "\t<CharCode>MDL</CharCode>\n" +
            "\t<Nominal>10</Nominal>\n" +
            "\t<Name>Молдавских леев</Name>\n" +
            "\t<Value>31,3465</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01535\">\n" +
            "\t<NumCode>578</NumCode>\n" +
            "\t<CharCode>NOK</CharCode>\n" +
            "\t<Nominal>10</Nominal>\n" +
            "\t<Name>Норвежских крон</Name>\n" +
            "\t<Value>67,6328</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01565\">\n" +
            "\t<NumCode>985</NumCode>\n" +
            "\t<CharCode>PLN</CharCode>\n" +
            "\t<Nominal>1</Nominal>\n" +
            "\t<Name>Польский злотый</Name>\n" +
            "\t<Value>15,2291</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01585F\">\n" +
            "\t<NumCode>946</NumCode>\n" +
            "\t<CharCode>RON</CharCode>\n" +
            "\t<Nominal>1</Nominal>\n" +
            "\t<Name>Румынский лей</Name>\n" +
            "\t<Value>13,9882</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01589\">\n" +
            "\t<NumCode>960</NumCode>\n" +
            "\t<CharCode>XDR</CharCode>\n" +
            "\t<Nominal>1</Nominal>\n" +
            "\t<Name>СДР (специальные права заимствования)</Name>\n" +
            "\t<Value>78,7225</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01625\">\n" +
            "\t<NumCode>702</NumCode>\n" +
            "\t<CharCode>SGD</CharCode>\n" +
            "\t<Nominal>1</Nominal>\n" +
            "\t<Name>Сингапурский доллар</Name>\n" +
            "\t<Value>41,1792</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01670\">\n" +
            "\t<NumCode>972</NumCode>\n" +
            "\t<CharCode>TJS</CharCode>\n" +
            "\t<Nominal>10</Nominal>\n" +
            "\t<Name>Таджикских сомони</Name>\n" +
            "\t<Value>64,5966</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01700J\">\n" +
            "\t<NumCode>949</NumCode>\n" +
            "\t<CharCode>TRY</CharCode>\n" +
            "\t<Nominal>1</Nominal>\n" +
            "\t<Name>Турецкая лира</Name>\n" +
            "\t<Value>16,1533</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01710A\">\n" +
            "\t<NumCode>934</NumCode>\n" +
            "\t<CharCode>TMT</CharCode>\n" +
            "\t<Nominal>1</Nominal>\n" +
            "\t<Name>Новый туркменский манат</Name>\n" +
            "\t<Value>16,2855</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01717\">\n" +
            "\t<NumCode>860</NumCode>\n" +
            "\t<CharCode>UZS</CharCode>\n" +
            "\t<Nominal>1000</Nominal>\n" +
            "\t<Name>Узбекских сумов</Name>\n" +
            "\t<Value>14,5802</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01720\">\n" +
            "\t<NumCode>980</NumCode>\n" +
            "\t<CharCode>UAH</CharCode>\n" +
            "\t<Nominal>10</Nominal>\n" +
            "\t<Name>Украинских гривен</Name>\n" +
            "\t<Value>21,8967</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01760\">\n" +
            "\t<NumCode>203</NumCode>\n" +
            "\t<CharCode>CZK</CharCode>\n" +
            "\t<Nominal>10</Nominal>\n" +
            "\t<Name>Чешских крон</Name>\n" +
            "\t<Value>24,3557</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01770\">\n" +
            "\t<NumCode>752</NumCode>\n" +
            "\t<CharCode>SEK</CharCode>\n" +
            "\t<Nominal>10</Nominal>\n" +
            "\t<Name>Шведских крон</Name>\n" +
            "\t<Value>65,5648</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01775\">\n" +
            "\t<NumCode>756</NumCode>\n" +
            "\t<CharCode>CHF</CharCode>\n" +
            "\t<Nominal>1</Nominal>\n" +
            "\t<Name>Швейцарский франк</Name>\n" +
            "\t<Value>58,8578</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01810\">\n" +
            "\t<NumCode>710</NumCode>\n" +
            "\t<CharCode>ZAR</CharCode>\n" +
            "\t<Nominal>10</Nominal>\n" +
            "\t<Name>Южноафриканских рэндов</Name>\n" +
            "\t<Value>44,4575</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01815\">\n" +
            "\t<NumCode>410</NumCode>\n" +
            "\t<CharCode>KRW</CharCode>\n" +
            "\t<Nominal>1000</Nominal>\n" +
            "\t<Name>Вон Республики Корея</Name>\n" +
            "\t<Value>50,3939</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01820\">\n" +
            "\t<NumCode>392</NumCode>\n" +
            "\t<CharCode>JPY</CharCode>\n" +
            "\t<Nominal>100</Nominal>\n" +
            "\t<Name>Японских иен</Name>\n" +
            "\t<Value>51,6562</Value>\n" +
            "</Valute>\n" +
            "</ValCurs>\n";
    private Serializer serializer;

    public ParserTests() {
        this.serializer = new Persister(new CustomMatcher());
    }

    @Test
    public void parserTest() throws Exception{
        CurrencyList list = serializer.read(CurrencyList.class, new ByteArrayInputStream(xml.getBytes()));
        assertEquals(list.getDate(), "14.06.2017");
        assertEquals(list.getCurrencies().size(), 34);
        Currency usd = list.get(840);
        assertEquals(usd.getNumCode(), 840);
        assertEquals(usd.getCharCode(), "USD");
        assertEquals(usd.getRubbles(), 56.9096, 0.0001);
        Currency jpy = list.get(392);
        assertEquals(jpy.getNumCode(), 392);
        assertEquals(jpy.getCharCode(), "JPY");
        assertEquals(jpy.getRubbles(), 0.516562, 0.0001);
    }
}
