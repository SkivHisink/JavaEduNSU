package com.labwork3.parsers;

import javafx.util.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AlfaCurrencyParser extends SeleniumParserBase {
    public AlfaCurrencyParser() {
        super();
        InitURL = "https://alfabank.ru/currency/";
        CurrencyNames = new ArrayList<>();
        CurrencyNames.add("USD");
        CurrencyNames.add("EUR");
        CurrencyNames.add("CNY");
        BankName = "Альфа-банк";
    }

    @Override
    public void fillCurrencyList(WebDriver driver) {
        var currencyList = driver
                .findElements(By.xpath(
                        "//span[@class='a1jIK y1jIK H1jIK eG2mw SG2mw']"));
        int i = 0;
        int j = 0;
        NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);
        for (; j < CurrencyNames.size(); ++i) {
            try {
                CurrencyBuyList.add(new Pair<>(CurrencyNames.get(j), nf.parse(currencyList.get(i).getText()).doubleValue()));
                ++i;
                CurrencySellList.add(new Pair<>(CurrencyNames.get(j), nf.parse(currencyList.get(i).getText()).doubleValue()));
                ++j;
            } catch (Exception e) {

            }
        }
    }
}
