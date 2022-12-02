package com.labwork3.parsers;

import javafx.util.Pair;
import org.openqa.selenium.WebDriver;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public abstract class SeleniumParserBase {
    public String InitURL = "";
    public String DownloadFilepath = "";
    public String BankName = null;
    public List<String> CurrencyNames;
    public List<Pair<String, Double>> CurrencyBuyList;
    public List<Pair<String, Double>> CurrencySellList;
    private boolean isConnected = false;

    public boolean isConnected() {
        return isConnected;
    }

    public SeleniumParserBase(){
        CurrencyBuyList = new ArrayList<>();
        CurrencySellList = new ArrayList<>();
    }

    public void connect( WebDriver driver) throws InterruptedException {
        driver.get(InitURL);
        Thread.sleep(500);
        isConnected = true;
    }

    public abstract void fillCurrencyList(WebDriver driver) throws ParseException;
}
