package com.labwork3.parsers;

import javafx.util.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

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
        var currancyList = driver
                .findElements(By.xpath(
                        "//span[@class='a1jIK y1jIK H1jIK eG2mw SG2mw']"));
        for (int i = 0; i < currancyList.size(); ++i) {
            try {
                CurrencyBuyList.add(new Pair<>(CurrencyNames.get(i), Double.parseDouble(currancyList.get(i).getText().split("\\s+")[0])));
                CurrencySellList.add(new Pair<>(CurrencyNames.get(i), Double.parseDouble(currancyList.get(i).getText().split("\\s+")[0])));
            } catch (Exception e) {

            }
        }
    }
}
