package com.labwork3.parsers;

import javafx.util.Pair;
import org.openqa.selenium.By;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SberCurrencyParser extends SeleniumParserBase{
    public SberCurrencyParser()
    {
        super();
        InitURL = "http://www.sberbank.ru/ru/quotes/currencies?tab=sbol&currency=USD,EUR";
        currancyNames = new ArrayList<>();
        currancyNames.add("USD");
        currancyNames.add("USD");
        currancyNames.add("EUR");
        currancyNames.add("EUR");
    }

    @Override
    public void fillCurrancyList() {
        var currancyList = driver
                .findElements(By.xpath(
                        "//div[@class='kitt-text kitt-text_size_m']"));
        NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE); // сбер = Франция
        for (int i = 0; i < currancyList.size(); ++i) {
            try {
                CurrancyBuyList.add(new Pair<>(currancyNames.get(i), nf.parse(currancyList.get(i).getText()).doubleValue()));
                i++;
                CurrancySellList.add(new Pair<>(currancyNames.get(i), nf.parse(currancyList.get(i).getText()).doubleValue()));
            }
            catch(Exception e){

            }
        }
    }
}
