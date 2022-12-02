package com.labwork3.parsers;

import javafx.util.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OpenCurrencyParser extends SeleniumParserBase {
    public OpenCurrencyParser()
    {
        super();
        InitURL = "https://www.open.ru/exchange-person";
        CurrencyNames = new ArrayList<>();
        CurrencyNames.add("USD");
        CurrencyNames.add("EUR");
        CurrencyNames.add("GBP");
        CurrencyNames.add("CHF");
        CurrencyNames.add("JPY");
        CurrencyNames.add("CNY");
        CurrencyNames.add("KZT");
        BankName = "Открытие";
    }

    @Override
    public void fillCurrencyList( WebDriver driver) {
        var currencyBuyList = driver
                .findElements(By.xpath(
                        "//td[@class='card-rates-table__cell card-rates-table__sale large-text']"));
        var currencySellList = driver
                .findElements(By.xpath(
                        "//td[@class='card-rates-table__cell card-rates-table__purchase large-text']"));
        NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);
        for (int i = 0; i < currencyBuyList.size(); ++i) {
            try {
                CurrencyBuyList.add(new Pair<>(CurrencyNames.get(i), nf.parse(currencyBuyList.get(i).getText()).doubleValue()));
                CurrencySellList.add(new Pair<>(CurrencyNames.get(i), nf.parse(currencySellList.get(i).getText()).doubleValue()));
            }
            catch(Exception e){

            }
        }
    }
}
