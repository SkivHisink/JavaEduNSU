package com.labwork2.datasource;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FinamSelenium {
    //Driver and its trinkets
    private String initURL = "https://www.finam.ru/profile/moex-akcii/gazprom/export/";
    private String downloadFilepath = "";
    private WebDriver driver;
    private ChromeOptions options;
    //Flags
    private boolean isConnected = false;
    private boolean isInited = false;
    // WebElements
    private List<WebElement> marketDropDownList;
    private List<WebElement> marketComboBoxList;
    private List<WebElement> quotesComboBoxList;
    private WebElement buttonToOpenMarketCB;
    private WebElement buttonToOpenQuoteCB;

    // driver init
    public FinamSelenium(ChromeOptions options) {
        driver = new ChromeDriver(options);
    }

    // if someone need it to change
    public ChromeOptions stdOptionsInit() {
        options = new ChromeOptions();
        try{
            downloadFilepath = System.getProperty("user.dir");
            System.out.print("Executing at =>"+downloadFilepath.replace("\\", "/"));
        }catch (Exception e){
            System.out.println("Exception caught ="+e.getMessage());
        }
        options.addArguments("--headless"); // ninja mode
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
    }

    public void connect() {
        driver.get(initURL);
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
        if (!isInited) {
            throw new Exception("You are not initialized web elements. Please try to initialize web elements.");
        }
    }

    public void initElements() throws Exception {
        checkFlags(false);
        marketDropDownList = driver.findElements(By.className("finam-ui-dropdown-list"));
        marketComboBoxList = marketDropDownList.get(0).findElement(By.xpath("./div")).
                findElement(By.xpath("./ul")).findElements(By.xpath("./li"));
        quotesComboBoxList = marketDropDownList.get(1).findElement(By.xpath("./div")).
                findElement(By.xpath("./ul")).findElements(By.xpath("./li"));
        buttonToOpenMarketCB = driver.findElement(By.className("finam-ui-quote-selector-market")).findElement(By.className("finam-ui-quote-selector-arrow"));
        buttonToOpenQuoteCB = driver.findElement(By.className("finam-ui-quote-selector-quote")).findElement(By.className("finam-ui-quote-selector-arrow"));
        isInited = true;
    }

    private ArrayList<String> getList(List<WebElement> comboBoxList) throws Exception {
        checkFlags();
        var resultList = new ArrayList<String>();
        for (int i = 0; i < comboBoxList.size(); ++i) {
            resultList.add(comboBoxList.get(i).getAttribute("innerHTML"));
        }
        return resultList;
    }

    public ArrayList<String> getMarketList() throws Exception {
        return getList(marketComboBoxList);
    }

    public ArrayList<String> getQuotesList() throws Exception {
        return getList(quotesComboBoxList);
    }

    private void setComboBoxValue(String varName, int comboboxSize, List<WebElement> comboBoxList, WebElement button) throws Exception {
        checkFlags();
        button.click();
        for (int i = 0; i < comboboxSize; ++i) {
            if (comboBoxList.get(i).getAttribute("innerHTML").equals(varName)) {
                comboBoxList.get(i).click();
                break;
            }
        }
    }

    public void setMarket(String marketName, int marketNumber) throws Exception {
        setComboBoxValue(marketName, marketNumber, marketComboBoxList, buttonToOpenMarketCB);
    }


    public void setQuote(String quoteName, int quoteNumber) throws Exception {
        setComboBoxValue(quoteName, quoteNumber, quotesComboBoxList, buttonToOpenQuoteCB);
    }

    public void setBeginDate(){

    }

    public void setEndDate(){

    }

    public ArrayList<String> getDataSteps(){
        var result= new ArrayList<String>();
        //
        return result;
    }

    public void setDataStep()
    {

    }

    public void getData(){

    }
}
