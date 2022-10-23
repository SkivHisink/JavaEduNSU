package com.labwork2;

import com.labwork2.datasource.ApiExecutor;
import com.labwork2.datasource.DataSourceBase;
import com.labwork2.datasource.DownloadedDataBuffef;
import com.labwork2.datasource.FinamSelenium;
import com.labwork2.utils.ParseUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

import java.util.ArrayList;

public class MainController {
    // GUI elements
    @FXML
    ComboBox dataSourceCB;
    @FXML
    ComboBox intervalCB;
    @FXML
    ComboBox marketCB;
    @FXML
    ComboBox quoteCB;
    @FXML
    Region mainChartRegion;
    @FXML
    Label infoLabel;
    @FXML
    Button connectButton;
    @FXML
    Button getDataButton;
    @FXML
    ProgressIndicator progressIndicator;
    @FXML
    Label connectionLabel;
    @FXML
    TextField beginDateTF;
    @FXML
    TextField endDateTF;
    // support GUI objects
    private ArrayList<String> dataSourceList;
    private ObservableList<String> dataSourceOList;
    private DataSourceBase data;
    private ArrayList<String> marketList;
    private ObservableList<String> marketListOList;
    private ArrayList<String> quoteList;
    private ObservableList<String> quoteListOList;
    //Flags
    boolean isDSCBinit = false;

    public MainController() {
        dataSourceList = new ArrayList<>();
        dataSourceList.add("Finam");
        dataSourceList.add("PolygonAPI");
        dataSourceList.add("Buffer");
        dataSourceOList = FXCollections.observableList(dataSourceList);
    }

    public void Test() {
        infoLabel.setText("!HELLO TEST!");
    }

    public void InitDataSourceCB() {
        if (!isDSCBinit) {
            dataSourceCB.setItems(dataSourceOList);
            isDSCBinit = true;
        }
    }

    public void onConnectButtonClick() {
        progressIndicator.setProgress(0.1);
        setElementDisable(true);
        connectionLabel.setText("Connecting...");
        if (dataSourceCB.getValue() == null) {
            infoLabel.setText("INFO:" + "Data source not selected");
            return;
        } else if (dataSourceCB.getValue().equals(dataSourceList.get(0))) {
            data = new FinamSelenium();
        } else if (dataSourceCB.getValue().equals(dataSourceList.get(1))) {
            data = new ApiExecutor();
        } else if (dataSourceCB.getValue().equals(dataSourceList.get(2))) {
            data = new DownloadedDataBuffef();
        } else {
            infoLabel.setText("INFO:" + "Data source not selected");
            return;
        }
        progressIndicator.setProgress(0.25);
        Runnable run = () -> {
            try {
                data.connect();
                progressIndicator.setProgress(0.4);
                data.initElements();
                progressIndicator.setProgress(0.6);
                marketList = data.getMarketList();
                progressIndicator.setProgress(0.7);
                quoteList = data.getQuotesList();
                progressIndicator.setProgress(0.8);
                marketListOList = ParseUtils.Split(marketList, ">");
                progressIndicator.setProgress(0.9);
                quoteListOList = ParseUtils.Split(quoteList, ">");
                progressIndicator.setProgress(1.0);
            } catch (Exception e) {
                infoLabel.setText("INFO:" + "Can't connect. Problem:" + e.getMessage());
                connectionLabel.setText("Not connected");
                progressIndicator.setProgress(0.0);
                setElementDisable(false);
                return;
            }
            connectionLabel.setText("Connected");
            marketCB.setItems(marketListOList);
            quoteCB.setItems(quoteListOList);
            setElementDisable(false);
        };
        Thread myThread = new Thread(run, "DataThread");
        myThread.start();
    }

    private void setElementDisable(boolean isDisable) {
        dataSourceCB.setDisable(isDisable);
        marketCB.setDisable(isDisable);
        quoteCB.setDisable(isDisable);
        connectButton.setDisable(isDisable);
        beginDateTF.setDisable(isDisable);
        endDateTF.setDisable(isDisable);
        getDataButton.setDisable(isDisable);
        intervalCB.setDisable(isDisable);
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }

}