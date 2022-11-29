package com.labwork3.parsers;

import javafx.util.Pair;
import org.openqa.selenium.By;

import java.util.ArrayList;

public class AlfaCurrencyParser extends SeleniumParserBase {
    public AlfaCurrencyParser()
    {
        super();
        InitURL = "https://alfabank.ru/currency/";
        currancyNames = new ArrayList<>();
        currancyNames.add("USD");
        currancyNames.add("EUR");
        currancyNames.add("CNY");
    }

    @Override
    public void fillCurrancyList() {
        var currancyList = driver
                .findElements(By.xpath(
                        "//span[@class='a1jIK y1jIK H1jIK eG2mw SG2mw']"));
        for (int i = 0; i < currancyList.size(); ++i) {
            try {
                CurrancyBuyList.add(new Pair<>(currancyNames.get(i), Double.parseDouble(currancyList.get(i).getText().split("\\s+")[0])));
                CurrancySellList.add(new Pair<>(currancyNames.get(i), Double.parseDouble(currancyList.get(i).getText().split("\\s+")[0])));
            }
            catch(Exception e){

            }
        }
    }
}
