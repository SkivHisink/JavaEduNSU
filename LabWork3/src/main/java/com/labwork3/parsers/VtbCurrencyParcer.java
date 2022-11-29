package com.labwork3.parsers;

import javafx.util.Pair;
import org.openqa.selenium.By;

import java.util.ArrayList;

public class VtbCurrencyParcer extends SeleniumParserBase {
    public VtbCurrencyParcer() {
        super();
        InitURL = "https://www.vtb.ru/personal/platezhi-i-perevody/obmen-valjuty/";
        currancyNames = new ArrayList<>();
        currancyNames.add("USD < 30k");
        currancyNames.add("USD > 30k");
        currancyNames.add("EUR < 30k");
        currancyNames.add("EUR > 30k");
        currancyNames.add("GBP");
        currancyNames.add("CHF");
        currancyNames.add("CAD");
        currancyNames.add("SEK");
        currancyNames.add("NOK");
        currancyNames.add("JPY");
        currancyNames.add("CNY");
        currancyNames.add("PLN");
    }

    @Override
    public void connect() throws InterruptedException {
        super.connect();
        Thread.sleep(3000); // втб самый жадный на нормальных программистов
    }

    @Override
    public void fillCurrancyList() {
        var currancyList = driver
                .findElements(By.xpath(
                        "//p[@class='typographystyles__Box-foundation-kit__sc-14qzghz-0 jEFSaq numbersstyles__TypographyTitle-foundation-kit__sc-1xhbrzd-4 haHdlc']"));
        int i = 0;
        int j = 0;
        for (; i < currancyList.size(); ++i) {
            CurrancyBuyList.add(new Pair<>(currancyNames.get(j), Double.parseDouble(currancyList.get(i).getText())));
            i++;
            CurrancySellList.add(new Pair<>(currancyNames.get(j), Double.parseDouble(currancyList.get(i).getText())));
            j++;
        }
    }
}
