package com.labwork3;

import com.labwork3.parsers.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    public static WebDriver driver;

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
        driver = new ChromeDriver(stdOptionsInit().getKey());
        // TODO: make some fabric?
        VtbCurrencyParcer vtb = new VtbCurrencyParcer();
        SberCurrencyParser sber = new SberCurrencyParser();
        AlfaCurrencyParser alfa = new AlfaCurrencyParser();
        OpenCurrencyParser open = new OpenCurrencyParser();
        bankList = new ArrayList<>();
        bankList.add(vtb);
        bankList.add(sber);
        bankList.add(alfa);
        bankList.add(open);
    }

    private void fillTable(List<SeleniumParserBase> bankList) {
        resultTable.getColumns().clear();
        int index = 0;
        TableColumn<ObservableList<String>, String> table = new TableColumn<>("Bank");
        int finalIndex2 = index;
        table.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIndex2)));
        index++;
        resultTable.getColumns().add(table);
        List<String> currencyColNamesList = new ArrayList<>();
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
            if (!tempBankName.equals(tempBank.BankName)) {
                for (int j = 0; j < tempBank.CurrencyNames.size(); ++j) {
                    var tempCurrency = tempBank.CurrencyNames.get(j);
                    if (bestCurrencyList.contains(tempCurrency) && !currencyColNamesList.contains(tempCurrency)) {
                        //resultTable.getColumns().add(new TableColumn<>("Buy " + tempCurrency));
                        table = new TableColumn<>("Buy " + tempCurrency);
                        int finalIndex = index;
                        table.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIndex)));
                        index++;
                        resultTable.getColumns().add(table);
                        //((TableColumn)resultTable.getColumns().get(resultTable.getColumns().size()-1)).setCellValueFactory(new PropertyValueFactory<>("Buy" + tempCurrency));
                        //resultTable.getColumns().add(new TableColumn<>("Sell " + tempCurrency));
                        table = new TableColumn<>("Sell " + tempCurrency);
                        int finalIndex1 = index;
                        table.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIndex1)));
                        resultTable.getColumns().add(table);
                        index++;
                        //((TableColumn)resultTable.getColumns().get(resultTable.getColumns().size()-1)).setCellValueFactory(new PropertyValueFactory<>("Sell" + tempCurrency));
                        currencyColNamesList.add(tempCurrency);
                    }
                }
            }
        }
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        for (int i = 0; i < bankList.size(); ++i) {
            ObservableList<String> bankCurrencies = FXCollections.observableArrayList();
            bankCurrencies.clear();
            var tempBank =  bankList.get(i);
            bankCurrencies.add(tempBank.BankName);
            for (int j = 0; j < currencyColNamesList.size(); ++j) {
                int currencyIndex = tempBank.CurrencyNames.indexOf(currencyColNamesList.get(j));
                if (currencyIndex != -1 && tempBank.CurrencyBuyList.size() > currencyIndex) {
                    bankCurrencies.add(tempBank.CurrencyBuyList.get(currencyIndex).getValue().toString());
                    bankCurrencies.add(tempBank.CurrencySellList.get(currencyIndex).getValue().toString());
                }
                else{
                    bankCurrencies.add("?");
                    bankCurrencies.add("?");
                }
            }
            data.add(bankCurrencies);
        }
        resultTable.setItems(data);
    }

    private boolean connect(List<SeleniumParserBase> bankList) {
        for (int i = 0; i < bankList.size(); ++i) {
            try {
                var tempBank = bankList.get(i);
                tempBank.connect(driver);
                tempBank.fillCurrencyList(driver);
            } catch (Exception e) {
                //return false;
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