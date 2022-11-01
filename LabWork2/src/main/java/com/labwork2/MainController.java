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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;

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
    @FXML
    Label contractLabel;
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
                readyPercent += 0.1;
                data.initElements();
                progressIndicator.setProgress(readyPercent);
                readyPercent += 0.1;
                marketList = data.getMarketList();
                progressIndicator.setProgress(readyPercent);
                readyPercent += 0.1;
                quoteList = data.getQuotesList();
                progressIndicator.setProgress(readyPercent);
                readyPercent += 0.1;
                intervalList = data.getIntervalList();
                progressIndicator.setProgress(readyPercent);
                readyPercent += 0.1;
                marketOList = FXCollections.observableList(marketList);//ParseUtils.Split(marketList, ">"); // need to cover it in getter
                progressIndicator.setProgress(readyPercent);
                readyPercent += 0.1;
                quoteOList = FXCollections.observableList(quoteList);//ParseUtils.Split(quoteList, ">"); // need to cover it in getter
                progressIndicator.setProgress(readyPercent);
                readyPercent += 0.1;
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
            intervalCB.setItems(intervalOList);
            quoteCB.setItems(quoteOList);
            setElementDisable(false);
            //JfreeCandlestickChart obj = new JfreeCandlestickChart("title");
            //JFreeChart chart = obj.createChart("dataset");
            //mainChartRegion = new ChartViewer(chart);
        };
        Thread myThread = new Thread(run, "DataThread");
        myThread.start();
        quoteCB.setValue(data.initQuote);
        marketCB.setValue(data.initMarket);
        intervalCB.setValue(data.initInterval);
        JfreeCandlestickChart chart = new JfreeCandlestickChart("hello");
        //mainChartRegion.setChart(chart.createChart("heh"));
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
        contractLabel.setDisable(isDisable);
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }

    public void setMarketValue() {
        if (marketList == null) {
            return;
        }
        Runnable run = () -> {
            setElementDisable(true);
            var marketCBValue = (String) marketCB.getValue();
            boolean isFound = false;
            for (int i = 0; i < marketList.size(); ++i) {
                if (marketList.get(i).contains(marketCBValue)) {
                    marketCBValue = marketList.get(i);
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                try {
                    data.setMarket(marketCBValue, marketList.size());
                    quoteList = data.getQuotesList();
                    quoteOList = FXCollections.observableList(quoteList);//ParseUtils.Split(quoteList, ">");
                    quoteCB.setItems(quoteOList);
                } catch (Exception e) {
                    infoLabel.setText("INFO:" + "Can't connect. Problem:" + e.getMessage());
                }
            }
            setElementDisable(false);
        };
        Thread myThread = new Thread(run, "MarketThread");
        myThread.start();
    }

    public void setQuoteValue() {
        if (quoteList == null) {
            return;
        }
        if (quoteCB.getValue() == null) {
            setElementDisable(false);
            return;
        }
        Runnable run = () -> {
            setElementDisable(true);
            var quoteCBValue = (String) quoteCB.getValue();
            boolean isFound = false;

            for (int i = 0; i < quoteList.size(); ++i) {
                if (quoteList.get(i).contains(quoteCBValue)) {
                    quoteCBValue = quoteList.get(i);
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                try {
                    data.setQuote(quoteCBValue, quoteList.size());
                } catch (Exception e) {
                    infoLabel.setText("INFO:" + "Can't connect. Problem:" + e.getMessage());
                }
            }
            setElementDisable(false);
        };
        Thread myThread = new Thread(run, "QuoteThread");
        myThread.start();
    }

    @FXML
    public void setIntervalValue() {
        if (intervalList == null) {
            return;
        }
        if (intervalCB.getValue() == null) {
            setElementDisable(false);
            return;
        }
        Runnable run = () -> {
            setElementDisable(true);
            try {
                data.setInterval((String) intervalCB.getValue(), intervalList.size());
            } catch (Exception e) {
                //ehhh
            }
            setElementDisable(false);
        };
        Thread myThread = new Thread(run, "IntervalThread");
        myThread.start();
    }

    @FXML
    public void onGetDataButton() {
        try {
            var beginDate = ((String) beginDateTF.getText()).split("\\.");
            data.setBeginData(Integer.parseInt(beginDate[0]),
                    Integer.parseInt(beginDate[1]) - 1,
                    Integer.parseInt(beginDate[2]));
            var endDate = ((String) endDateTF.getText()).split("\\.");
            data.setEndData(Integer.parseInt(endDate[0]),
                    Integer.parseInt(endDate[1]) - 1,
                    Integer.parseInt(endDate[2]));
            data.getData();
            JfreeCandlestickChart temp = new JfreeCandlestickChart("heh");
            String interval = (String) intervalCB.getValue();
            // ticks
            if (interval.equals(intervalList.get(0))) {
                temp.setTimeInterval(1);
            }
            // mins
            else if (interval.equals(intervalList.get(1))) {
                temp.setTimeInterval(1);
            }
            // 5 mins
            else if (interval.equals(intervalList.get(2))) {
                temp.setTimeInterval(5);
            }
            // 10 mins
            else if (interval.equals(intervalList.get(3))) {
                temp.setTimeInterval(10);
            }
            // 15 mins
            else if (interval.equals(intervalList.get(4))) {
                temp.setTimeInterval(15);
            }
            // 30 min
            else if (interval.equals(intervalList.get(5))) {
                temp.setTimeInterval(30);
            }
            // 1 hour
            else if (interval.equals(intervalList.get(6))) {
                temp.setTimeInterval(60);
            }
            // 1 day
            else if (interval.equals(intervalList.get(7))) {
                temp.setTimeInterval(60 * 24);
            }
            // 1 week
            else if (interval.equals(intervalList.get(8))) {
                temp.setTimeInterval(60 * 24 * 7);
            }
            // 1 month
            else if (interval.equals(intervalList.get(9))) {
                temp.setTimeInterval(60 * 24 * 7 * 4); // problemss
            } else {
                // something impossible happend
            }
            for (int i = 0; i < data.data.size(); ++i) {
                temp.onTrade(data.data.get(i));
            }
            JFreeChart chart = temp.createChart((String) marketCB.getValue() + " " +
                    (String) quoteCB.getValue() + " " +
                    beginDateTF.getText() + "-" +
                    endDateTF.getText());
            ChartViewer viewer = new ChartViewer(chart);
            Stage stage = new Stage();
            stage.setScene(new Scene(viewer));
            stage.setTitle("JFreeChart: TimeSeriesFXDemo1.java");
            stage.show();
        } catch (Exception e) {
            infoLabel.setText("INFO:" + "Wrong date. Please check date.");
        }
    }
}