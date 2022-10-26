package com.labwork2.datasource;

import com.labwork2.utils.ParseUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FinamSelenium extends DataSourceBase {
    //Driver and its trinkets
    private String initURL = "https://www.finam.ru/profile/moex-akcii/gazprom/export/";
    private String downloadFilepath = "";
    static public WebDriver driver;
    private ChromeOptions options;
    //Flags
    private boolean isConnected = false;
    private boolean isInited = false;
    // WebElements
    private List<WebElement> marketDropDownList;
    private List<WebElement> marketComboBoxList;
    private List<WebElement> quotesComboBoxList;
    private List<WebElement> intervalComboBoxList;
    private WebElement buttonToOpenMarketCB;
    private WebElement buttonToOpenQuoteCB;
    private WebElement buttonToOpenIntervalCB;

    private ArrayList<String> intervalList = new ArrayList<>();

    // driver init
    public FinamSelenium(ChromeOptions options) {
        driver = new ChromeDriver(options);
    }

    // if someone need it to change
    public ChromeOptions stdOptionsInit() {
        options = new ChromeOptions();
        try {
            downloadFilepath = System.getProperty("user.dir");
            System.out.print("Executing at =>" + downloadFilepath.replace("\\", "/"));
        } catch (Exception e) {
            System.out.println("Exception caught =" + e.getMessage());
        }
        //options.addArguments("--headless"); // ninja mode
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadFilepath);
        options.setExperimentalOption("prefs", chromePrefs);
        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        options.setCapability(ChromeOptions.CAPABILITY, options);
        return options;
    }

    // driver init by std options
    public FinamSelenium() {
        stdOptionsInit();
        driver = new ChromeDriver(options);
        initMarket = "МосБиржа акции";
        initQuote = "ГАЗПРОМ ао";
        initContract = "GAZP";
        initInterval = "1 час";
    }

    @Override
    public void connect() throws InterruptedException {
        driver.get(initURL);
        Thread.sleep(500);
        //We need to remove ad
        try {
            var popup_close = driver.findElement(By.className("ld57581d9"));
            if (popup_close != null) {
                popup_close.click();
            }
        } catch (Exception e) {
            // We are lucky and we don't have ad
        }
        isConnected = true;
    }

    private void checkFlags() throws Exception {
        checkFlags(true);
    }

    private void checkFlags(boolean isCheckAfterFirst) throws Exception {
        if (!isConnected) {
            throw new Exception("You are not connected to finam. Please try to reconnect");
        }
        if (!isCheckAfterFirst) {
            return;
        }
        if (!isInited) {
            throw new Exception("You are not initialized web elements. Please try to initialize web elements.");
        }
    }

    private void updateAllVariables() {
        marketDropDownList = driver.findElements(By.className("finam-ui-dropdown-list"));
        marketComboBoxList = marketDropDownList.get(0).findElement(By.xpath("./div")).
                findElement(By.xpath("./ul")).findElements(By.xpath("./li"));
        quotesComboBoxList = marketDropDownList.get(1).findElement(By.xpath("./div")).
                findElement(By.xpath("./ul")).findElements(By.xpath("./li"));
        intervalComboBoxList = marketDropDownList.get(2).findElement(By.xpath("./div")).
                findElement(By.xpath("./ul")).findElements(By.xpath("./li"));
        buttonToOpenMarketCB = driver.findElement(By.className("finam-ui-quote-selector-market")).
                findElement(By.className("finam-ui-quote-selector-arrow"));
        buttonToOpenQuoteCB = driver.findElement(By.className("finam-ui-quote-selector-quote")).
                findElement(By.className("finam-ui-quote-selector-arrow"));
        buttonToOpenIntervalCB = driver.findElement(By.className("finam-ui-controls-select"));
    }

    @Override
    public void initElements() throws Exception {
        checkFlags(false);
        updateAllVariables();
        isInited = true;
    }

    private ArrayList<String> getList(List<WebElement> comboBoxList) throws Exception {
        checkFlags();
        var resultList = new ArrayList<String>();
        for (int i = 0; i < comboBoxList.size(); ++i) { // we need multithreading
            resultList.add(comboBoxList.get(i).getAttribute("innerHTML"));
        }
        return resultList;
    }

    @Override
    public ArrayList<String> getMarketList() throws Exception {
        return ParseUtils.SplitAr(getList(marketComboBoxList), ">");
    }

    public ArrayList<String> getQuotesList() throws Exception {
        return ParseUtils.SplitAr(getList(quotesComboBoxList), ">");
    }

    @Override
    public ArrayList<String> getIntervalList() throws Exception {
        return ParseUtils.SplitAr(getList(intervalComboBoxList), ">");
    }

    private void setComboBoxValue(String varName, int comboboxSize, List<WebElement> comboBoxList, WebElement button) throws Exception {
        checkFlags();
        button.click();
        for (int i = 0; i < comboboxSize; ++i) {
            if (comboBoxList.get(i).getAttribute("innerHTML").contains(varName)) {
                comboBoxList.get(i).click();
                break;
            }
        }
    }

    @Override
    public void setMarket(String marketName, int marketNumber) throws Exception {
        setComboBoxValue(marketName, marketNumber, marketComboBoxList, buttonToOpenMarketCB);
        buttonToOpenMarketCB = driver.findElement(By.className("finam-ui-quote-selector-market")).
                findElement(By.className("finam-ui-quote-selector-arrow"));
        quotesComboBoxList = marketDropDownList.get(1).findElement(By.xpath("./div")).
                findElement(By.xpath("./ul")).findElements(By.xpath("./li"));
        buttonToOpenQuoteCB = driver.findElement(By.className("finam-ui-quote-selector-quote")).
                findElement(By.className("finam-ui-quote-selector-arrow"));
        buttonToOpenIntervalCB = driver.findElement(By.className("finam-ui-controls-select"));
    }

    @Override
    public void setQuote(String quoteName, int quoteNumber) throws Exception {
        setComboBoxValue(quoteName, quoteNumber, quotesComboBoxList, buttonToOpenQuoteCB);
        Thread.sleep(500);
        updateAllVariables();
    }

    @Override
    public void setInterval(String intervalName, int intervalNumber) throws Exception {
        setComboBoxValue(intervalName, intervalNumber, intervalComboBoxList, buttonToOpenIntervalCB);
    }

    @Override
    public void setBeginDate() {
    }

    @Override
    public void setEndDate() {

    }
    public void setDataStep() {

    }

    @Override
    public void getData() {
        WebElement submitButton = driver.findElement(By.id("issuer-profile-export-button"));
        WebElement filename = driver.findElement(By.id("issuer-profile-export-file-name"));
        var filepath = filename.getAttribute("value");

        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        } else {
            throw new IllegalStateException("This driver does not support JavaScript!");
        }
    }

    @Override
    public void setBeginData(int day, int month, int year){
        var dayWeb = driver.findElement(By.id("issuer-profile-export-from-d"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('type', '');", dayWeb);
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', arguments[1]);", dayWeb, day);
        dayWeb.clear();
        dayWeb.sendKeys( Integer.toString(day));
        var monthWeb = driver.findElement(By.id("issuer-profile-export-from-m"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('type', '');", monthWeb);
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', arguments[1]);", monthWeb, month);
        monthWeb.clear();
        monthWeb.sendKeys(Integer.toString(month));
        var yearWeb = driver.findElement(By.id("issuer-profile-export-from-y"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('type', '');", yearWeb);
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', arguments[1]);", yearWeb, year);
        yearWeb.clear();
        yearWeb.sendKeys(Integer.toString(year));
    }
    @Override
    public  void setEndData(int day, int month, int year){
        var dayWeb = driver.findElement(By.id("issuer-profile-export-to-d"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('type', '');", dayWeb);
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', arguments[1]);", dayWeb, day);
        dayWeb.clear();
        dayWeb.sendKeys(Integer.toString(day));
        var monthWeb = driver.findElement(By.id("issuer-profile-export-to-m"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('type', '');", monthWeb);
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', arguments[1]);", monthWeb, month);
        monthWeb.clear();
        monthWeb.sendKeys(Integer.toString(month));
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', arguments[1]);", monthWeb, month);
        var yearWeb = driver.findElement(By.id("issuer-profile-export-to-y"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('type', '');", yearWeb);
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', arguments[1]);", yearWeb, year);
        yearWeb.clear();
        yearWeb.sendKeys(Integer.toString(year));
    }
}
