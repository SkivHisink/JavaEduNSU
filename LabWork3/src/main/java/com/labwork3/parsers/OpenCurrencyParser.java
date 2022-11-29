package com.labwork3.parsers;

import javafx.util.Pair;
import org.openqa.selenium.By;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OpenCurrencyParser extends SeleniumParserBase {
    public OpenCurrencyParser()
    {
        super();
        InitURL = "https://www.open.ru/exchange-person";
        currancyNames = new ArrayList<>();
        currancyNames.add("USD");
        currancyNames.add("EUR");
        currancyNames.add("GBP");
        currancyNames.add("CHF");
        currancyNames.add("JPY");
        currancyNames.add("CNY");
        currancyNames.add("KZT");
    }

    @Override
    public void fillCurrancyList() {
        var currancyBuyList = driver
                .findElements(By.xpath(
                        "//td[@class='card-rates-table__cell card-rates-table__sale large-text']"));
        var currancySellList = driver
                .findElements(By.xpath(
                        "//td[@class='card-rates-table__cell card-rates-table__purchase large-text']"));
        NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE); // открытие = Франция
        for (int i = 0; i < currancyBuyList.size(); ++i) {
            try {
                CurrancyBuyList.add(new Pair<>(currancyNames.get(i), nf.parse(currancyBuyList.get(i).getText()).doubleValue()));
                CurrancySellList.add(new Pair<>(currancyNames.get(i), nf.parse(currancySellList.get(i).getText()).doubleValue()));
            }
            catch(Exception e){

            }
        }
    }
}
