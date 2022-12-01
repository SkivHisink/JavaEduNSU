package com.labwork3;

import com.labwork3.parsers.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainController {
    @FXML
    public Button ConnectButton;
    @FXML
    private TableView resultTable;
    @FXML
    private Label welcomeText;
    private List<SeleniumParserBase> bankList;
    private WebDriver driver;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    private Pair<ChromeOptions, String> stdOptionsInit() {
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

    public MainController() {
        // TODO: make some fabric?
        driver = new ChromeDriver(stdOptionsInit().getKey());
        VtbCurrencyParcer vtb = new VtbCurrencyParcer();
        SberCurrencyParser sber = new SberCurrencyParser();
        AlfaCurrencyParser alfa = new AlfaCurrencyParser();
        OpenCurrencyParser open = new OpenCurrencyParser();
        List<SeleniumParserBase> bankList = new ArrayList<>();
        bankList.add(vtb);
        bankList.add(sber);
        bankList.add(alfa);
        bankList.add(open);
    }

    private void fillTable(List<SeleniumParserBase> bankList) {
        resultTable.getColumns().clear();
        resultTable.getColumns().add(new TableColumn<>("Bank"));
        List<TableColumn> currencyColList = new ArrayList<>();
        //we add currency only if we have more than two banks with that currency
        List<String> bestCurrencyList = new ArrayList<>();
        double value = 1;
        //finding bank with the biggest number of currencies
        String tempBankName = "";
        for (int i = 0; i < bankList.size(); ++i) {
            var tempBank = bankList.get(i);
            if (tempBank.CurrencyNames.size() > bestCurrencyList.size()) {
                bestCurrencyList = tempBank.CurrencyNames;
                tempBankName = tempBank.BankName;
            }
        }
        //finding duplicated currencies
        for (int i = 0; i < bankList.size(); ++i) {
            var tempBank = bankList.get(i);
            if (tempBankName != tempBank.BankName) {
                for (int j = 0; j < tempBank.CurrencyNames.size(); ++j) {
                    if (tempBank.CurrencyNames.get(j).equals(bestCurrencyList.get(j))) {
                        resultTable.getColumns().add(bestCurrencyList.get(j));
                    }
                }
            }
        }
    }

    private boolean connect(List<SeleniumParserBase> bankList) {
        for (int i = 0; i < bankList.size(); ++i) {
            try {
                var tempBank = bankList.get(i);
                tempBank.connect(driver);
                tempBank.fillCurrencyList();
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @FXML
    protected void onConnectButtonClick() {
        connect(bankList);
        fillTable(bankList);
    }
}