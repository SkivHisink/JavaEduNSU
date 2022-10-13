package com.labwork2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

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
        String title = driver.getTitle();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        WebElement comboBox = driver.findElement(By.className("finam-ui-dropdown-list"));
        WebElement submitButton = driver.findElement(By.className("finam-ui-dialog-button-cancel"));
        submitButton.click();
        launch();
    }
}
