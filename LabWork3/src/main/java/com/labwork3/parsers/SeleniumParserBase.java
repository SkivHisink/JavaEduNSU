package com.labwork3.parsers;

import javafx.util.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SeleniumParserBase {
    public String InitURL = "";
    public String DownloadFilepath = "";
    public List<String> currancyNames;
    public List<Pair<String, Double>> CurrancyBuyList;
    public List<Pair<String, Double>> CurrancySellList;
    static public WebDriver driver = null;
    private boolean isConnected = false;

    public boolean isConnected() {
        return isConnected;
    }

    public Pair<ChromeOptions, String> stdOptionsInit() {
        ChromeOptions options = new ChromeOptions();
        String downloadFilepath = "";
        //options.addArguments("--headless"); // ninja mode
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        try {
            downloadFilepath = System.getProperty("user.dir");
            System.out.print("Executing at =>" + downloadFilepath.replace("\\", "/"));
            chromePrefs.put("download.default_directory", downloadFilepath);
        } catch (Exception e) {
            System.out.println("Exception caught =" + e.getMessage());
        }
        options.setExperimentalOption("prefs", chromePrefs);
        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        options.setCapability(ChromeOptions.CAPABILITY, options);
        return new Pair<>(options, downloadFilepath);
    }

    public SeleniumParserBase(){
        driver = new ChromeDriver(stdOptionsInit().getKey());
        CurrancyBuyList = new ArrayList<>();
        CurrancySellList = new ArrayList<>();
    }

    public void connect() throws InterruptedException {
        driver.get(InitURL);
        Thread.sleep(500);
        isConnected = true;
    }

    public void fillCurrancyList(){
    }
}
