package com.labwork3.parsers;

import javafx.util.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SberCurrencyParser extends SeleniumParserBase{
    public SberCurrencyParser()
    {
        super();
        InitURL = "http://www.sberbank.ru/ru/quotes/currencies?tab=sbol&currency=USD,EUR";
        CurrencyNames = new ArrayList<>();
        CurrencyNames.add("USD");
        CurrencyNames.add("EUR");
        BankName = "Сбер";
    }

    @Override
    public void fillCurrencyList( WebDriver driver) {
        var currancyList = driver
                .findElements(By.xpath(
                        "//div[@class='kitt-text kitt-text_size_m']"));
        NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);
        int i = 0;
        int j =0;
        for (; i < currancyList.size(); ++i) {
            try {
                CurrencyBuyList.add(new Pair<>(CurrencyNames.get(j), nf.parse(currancyList.get(i).getText()).doubleValue()));
                i++;
                CurrencySellList.add(new Pair<>(CurrencyNames.get(j), nf.parse(currancyList.get(i).getText()).doubleValue()));
                j++;
            }
            catch(Exception e){

            }
        }
    }
}
