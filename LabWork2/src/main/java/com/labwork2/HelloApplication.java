package com.labwork2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class HelloApplication extends Application {
    public static String executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //connection.setRequestProperty("access_key", "854e02f58534616ef44487c12ab2d1cf");
            //connection.setRequestProperty("symbols", "AAPL");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            String inputLine;
            final StringBuilder content = new StringBuilder();
            try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                //return content.toString();
            } catch (final Exception ex) {
                ex.printStackTrace();
                //return "";
            }
            //Send request
            //DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
            //wr.writeBytes(urlParameters);
            //wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // don't have RU market
        //executePost("http://api.marketstack.com/v1/eod?access_key=854e02f58534616ef44487c12ab2d1cf&symbols=AAPL", "symbols=AAPL");
        // don't have RU market
        // it works fine for getting tickers
        //executePost("https://api.polygon.io/v3/reference/tickers?active=true&sort=ticker&order=asc&limit=10&apiKey=F0kqtZX1TutW2KpwnZMekqUb_fjsM9IR", "symbols=AAPL");
        var it = new ChromeOptions();
        //it.addArguments("--headless");
        ///it.addArguments("--start-maximized");
        ///it.setExperimentalOption("excludeSwitches", "enable-automation");
        ///it.setExperimentalOption("useAutomationExtension", false);
        WebDriver driver = new ChromeDriver(it);
        driver.get("https://www.finam.ru/profile/moex-akcii/gazprom/export/");
        try {
            var popup_close = driver.findElement(By.className("ld57581d9"));
            if (popup_close != null) {
                popup_close.click();
                // !Yes!
            }
        }
        catch(Exception e){
            // $h*t happens
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        String title = driver.getTitle();
        var comboBox = driver.findElements(By.className("finam-ui-dropdown-list"));
        var comboBoxItemsMarket = comboBox.get(0).findElement(By.xpath("./div")).
                findElement(By.xpath("./ul")).findElements(By.xpath("./li"));
        List<String> itemsListMarkets = new ArrayList<>();
        for(int i=0;i<comboBoxItemsMarket.size();++i){
            itemsListMarkets.add(comboBoxItemsMarket.get(i).getAttribute("innerHTML"));
        }

        var toClick = driver.findElement(By.className("finam-ui-quote-selector-market")).findElement(By.className("finam-ui-quote-selector-arrow"));
        toClick.click();
        var textToSelect = itemsListMarkets.get(0);
        for(int i=0;i<itemsListMarkets.size();++i){
            if(comboBoxItemsMarket.get(i).getAttribute("innerHTML").equals(textToSelect)){
                comboBoxItemsMarket.get(i).click();
                break;
            }
        }
        var comboBoxItemsQuotes = comboBox.get(1).findElement(By.xpath("./div")).
                findElement(By.xpath("./ul")).findElements(By.xpath("./li"));
        var itemListQuotes = new ArrayList<String>();
        for(int i=0;i<comboBoxItemsQuotes.size();++i) {
            itemListQuotes.add(comboBoxItemsQuotes.get(i).getAttribute("innerHTML"));
        }
        toClick = driver.findElement(By.className("finam-ui-quote-selector-quote")).findElement(By.className("finam-ui-quote-selector-arrow"));
        toClick.click();
        textToSelect = itemListQuotes.get(1);
        for(int i=0;i<itemsListMarkets.size();++i){
            if(comboBoxItemsQuotes.get(i).getAttribute("innerHTML").equals(textToSelect)){
                comboBoxItemsQuotes.get(i).click();
                break;
            }
        }
        WebElement submitButton = driver.findElement(By.id("issuer-profile-export-button"));
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();", submitButton);
        } else {
            throw new IllegalStateException("This driver does not support JavaScript!");
        }
        launch();
    }
}
