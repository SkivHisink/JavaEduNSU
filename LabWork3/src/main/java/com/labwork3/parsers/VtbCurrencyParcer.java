package com.labwork3.parsers;

import javafx.util.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

public class VtbCurrencyParcer extends SeleniumParserBase {
    public VtbCurrencyParcer() {
        super();
        InitURL = "https://www.vtb.ru/personal/platezhi-i-perevody/obmen-valjuty/";
        CurrencyNames = new ArrayList<>();
        CurrencyNames.add("USD");
        CurrencyNames.add("USD > 30k");
        CurrencyNames.add("EUR");
        CurrencyNames.add("EUR > 30k");
        CurrencyNames.add("GBP");
        CurrencyNames.add("CHF");
        CurrencyNames.add("CAD");
        CurrencyNames.add("SEK");
        CurrencyNames.add("NOK");
        CurrencyNames.add("JPY");
        CurrencyNames.add("CNY");
        CurrencyNames.add("PLN");
        BankName = "ВТБ";
    }

    @Override
    public void connect( WebDriver driver) throws InterruptedException {
        super.connect(driver);
        Thread.sleep(5000); // втб самый жадный на нормальных программистов
    }

    @Override
    public void fillCurrencyList( WebDriver driver) throws ParseException {
        var currancyList = driver
                .findElements(By.xpath(
                        "//p[@class='typographystyles__Box-foundation-kit__sc-14qzghz-0 jEFSaq numbersstyles__TypographyTitle-foundation-kit__sc-1xhbrzd-4 haHdlc']"));
        int i = 0;
        int j = 0;
        NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE); // втб = Франция
        for (; i < currancyList.size(); ++i) {
            CurrencyBuyList.add(new Pair<>(CurrencyNames.get(j), nf.parse(currancyList.get(i).getText()).doubleValue()));
            i++;
            CurrencySellList.add(new Pair<>(CurrencyNames.get(j), nf.parse(currancyList.get(i).getText()).doubleValue()));
            j++;
        }
    }
}
