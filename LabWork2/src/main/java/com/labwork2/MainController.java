package com.labwork2;

import com.labwork2.candlestick.JfreeCandlestickChart;
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
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.xy.XYDataset;

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
    private ObservableList<String> marketOList;
    private ArrayList<String> quoteList;
    private ObservableList<String> quoteOList;
    private ArrayList<String> intervalList;
    private ObservableList<String> intervalOList;
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
                double readyPercent = 0.3;
                data.connect();
                progressIndicator.setProgress(readyPercent);
                readyPercent+=0.1;
                data.initElements();
                progressIndicator.setProgress(readyPercent);
                readyPercent+=0.1;
                marketList = data.getMarketList();
                progressIndicator.setProgress(readyPercent);
                readyPercent+=0.1;
                //quoteList = data.getQuotesList();
                //progressIndicator.setProgress(readyPercent);
                readyPercent+=0.1;
                intervalList = data.getIntervalList();
                progressIndicator.setProgress(readyPercent);
                readyPercent+=0.1;
                marketOList = FXCollections.observableList(marketList);//ParseUtils.Split(marketList, ">"); // need to cover it in getter
                progressIndicator.setProgress(readyPercent);
                readyPercent+=0.1;
                //quoteOList = FXCollections.observableList(quoteList);//ParseUtils.Split(quoteList, ">"); // need to cover it in getter
                progressIndicator.setProgress(readyPercent);
                readyPercent+=0.1;
                intervalOList = FXCollections.observableList(intervalList);//ParseUtils.Split(intervalList, ">"); // need to cover it in getter
                progressIndicator.setProgress(readyPercent);
            } catch (Exception e) {
                infoLabel.setText("INFO:" + "Can't connect. Problem:" + e.getMessage());
                //connectionLabel.setText("Not connected");
                progressIndicator.setProgress(0.0);
                setElementDisable(false);
                return;
            }
            //connectionLabel.setText("Connected");
            marketCB.setItems(marketOList);
            //quoteCB.setItems(quoteOList);
            setElementDisable(false);
            //JfreeCandlestickChart obj = new JfreeCandlestickChart("title");
            //JFreeChart chart = obj.createChart("dataset");
            //mainChartRegion = new ChartViewer(chart);
        };
        Thread myThread = new Thread(run, "DataThread");
        myThread.start();
        //JfreeCandlestickChart chart = new JfreeCandlestickChart("hello");
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

    public void setMarketValue() {
        var marketCBValue = (String)marketCB.getValue();
        boolean isFound = false;
        for(int i=0;i<marketList.size();++i){
            if(marketList.get(i).contains(marketCBValue)){
                marketCBValue = marketList.get(i);
                isFound=true;
                break;
            }
        }
        if(isFound) {
            try
            {
                data.setMarket(marketCBValue, marketList.size());
                quoteList = data.getQuotesList();
                quoteOList = FXCollections.observableList(quoteList);//ParseUtils.Split(quoteList, ">");
                quoteCB.setItems(quoteOList);
            }
            catch(Exception e){
                infoLabel.setText("INFO:" + "Can't connect. Problem:" + e.getMessage());
            }
        }
    }
    public void setQuoteValue(){
        var quoteCBValue = (String) quoteCB.getValue();
        boolean isFound = false;
        for(int i=0;i<quoteList.size();++i){
            if(quoteList.get(i).contains(quoteCBValue)){
                quoteCBValue = quoteList.get(i);
                isFound=true;
                break;
            }
        }
        if(isFound) {
            try
            {
                data.setQuote(quoteCBValue, quoteList.size());
                //quoteList = data.getQuotesList();
                //quoteOList = FXCollections.observableList(quoteList);//ParseUtils.Split(quoteList, ">");
                //quoteCB.setItems(quoteOList);
            }
            catch(Exception e){
                infoLabel.setText("INFO:" + "Can't connect. Problem:" + e.getMessage());
            }
        }
    }
}