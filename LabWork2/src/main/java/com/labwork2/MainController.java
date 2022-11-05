package com.labwork2;

import com.labwork2.candlestick.JfreeCandlestickChart;
import com.labwork2.datasource.ApiExecutor;
import com.labwork2.datasource.DataSourceBase;
import com.labwork2.datasource.DownloadedDataBuffef;
import com.labwork2.datasource.FinamSelenium;
import com.labwork2.utils.TimeUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.block.LineBorder;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.VerticalAlignment;

import java.awt.*;
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
    TextField emaTSTF;
    @FXML
    TextField emaSFTF;
    @FXML
    TextField macdfTSTF;
    @FXML
    TextField macdsTSTF;
    @FXML
    TextField smaTSTF;
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
    //Stage container
    static private ArrayList<Stage> stageList;

    public MainController() {
        stageList = new ArrayList<>();
        dataSourceList = new ArrayList<>();
        dataSourceList.add("Finam");
        dataSourceList.add("PolygonAPI");
        dataSourceList.add("Buffer");
        dataSourceOList = FXCollections.observableList(dataSourceList);
        //Platform.setImplicitExit(false); // Зачем я вообще это использовал? Good question
    }

    public void InitDataSourceCB() {
        if (!isDSCBinit) {
            dataSourceCB.setItems(dataSourceOList);
            isDSCBinit = true;
        }
    }

    private void postBreakConnectingActions(String info) {
        data = null;
        infoLabel.setText("INFO:" + info);
        setElementDisable(false);
        setElementNotMainDisable(true);
        progressIndicator.setProgress(0.0);
        connectionLabel.setText("Not connected");
    }

    private void postBreakConnectingActions() {
        postBreakConnectingActions("Data source not selected");
    }

    public void onConnectButtonClick() {
        progressIndicator.setProgress(0.1);
        setElementDisable(true);
        connectionLabel.setText("Connecting...");
        if (FinamSelenium.driver != null) {
            FinamSelenium.driver.quit();
        }
        if (dataSourceCB.getValue() == null) {
            postBreakConnectingActions();
            return;
        }
        else if (dataSourceCB.getValue().equals(dataSourceList.get(0))) {
            data = new FinamSelenium();
        }
        else if (dataSourceCB.getValue().equals(dataSourceList.get(1))) {
            data = new ApiExecutor();
            // Nowadays, we don't have this type of dataSource
            postBreakConnectingActions("This method is not realized");
            return;
        }
        else if (dataSourceCB.getValue().equals(dataSourceList.get(2))) {
            data = new DownloadedDataBuffef();
            // Nowadays, we don't have this type of dataSource
            postBreakConnectingActions("This method is not realized");
            return;
        }
        else {
            postBreakConnectingActions();
            return;
        }
        progressIndicator.setProgress(0.25);
        Runnable run = () -> {
            try {
                long start = System.nanoTime();
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
                long elapsedTime = System.nanoTime() - start;
                System.out.println(elapsedTime);
            } catch (Exception e) {
                Platform.runLater(() -> {
                    infoLabel.setText("INFO:" + "Can't connect. Problem:" + e.getMessage());
                    connectionLabel.setText("Not connected");
                    progressIndicator.setProgress(0.0);
                    setElementDisable(false);
                });
                return;
            }
            Platform.runLater(() -> {
                marketCB.setItems(marketOList);
                intervalCB.setItems(intervalOList);
                quoteCB.setItems(quoteOList);
                connectionLabel.setText("Connected");
                setElementDisable(false);
            });
        };
        Thread myThread = new Thread(run, "DataThread");
        myThread.start();
        quoteCB.setValue(data.initQuote);
        marketCB.setValue(data.initMarket);
        intervalCB.setValue(data.initInterval);
    }

    private void setElementDisable(boolean isDisable) {
        dataSourceCB.setDisable(isDisable);
        connectButton.setDisable(isDisable);
        setElementNotMainDisable(isDisable);
    }

    private void setElementNotMainDisable(boolean isDisable) {
        marketCB.setDisable(isDisable);
        quoteCB.setDisable(isDisable);
        beginDateTF.setDisable(isDisable);
        endDateTF.setDisable(isDisable);
        getDataButton.setDisable(isDisable);
        intervalCB.setDisable(isDisable);
        contractLabel.setDisable(isDisable);
        emaSFTF.setDisable(isDisable);
        emaSFTF.setDisable(isDisable);
        macdsTSTF.setDisable(isDisable);
        macdsTSTF.setDisable(isDisable);
        smaTSTF.setDisable(isDisable);
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
            var marketCBValue = (String) marketCB.getValue();
            boolean isFound = false;
            int j = 0;
            for (int i = 0; i < marketList.size(); ++i) {
                if (marketList.get(i).contains(marketCBValue)) {
                    marketCBValue = marketList.get(i);
                    j = i;
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                try {
                    data.setMarket(marketCBValue, marketList.size(), j);
                    quoteList = data.getQuotesList();
                    quoteOList = FXCollections.observableList(quoteList);//ParseUtils.Split(quoteList, ">");
                    quoteCB.setItems(quoteOList);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        infoLabel.setText("INFO:" + "Can't connect. Problem:" + e.getMessage());
                    });
                }
            }
            Platform.runLater(() -> {
                setElementDisable(false);
            });
        };
        Thread myThread = new Thread(run, "MarketThread");
        setElementDisable(true);
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
            var quoteCBValue = (String) quoteCB.getValue();
            boolean isFound = false;
            int j = 0 ;
            for (int i = 0; i < quoteList.size(); ++i) {
                if (quoteList.get(i).contains(quoteCBValue)) {
                    quoteCBValue = quoteList.get(i);
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                try {
                    data.setQuote(quoteCBValue, quoteList.size(), j);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        infoLabel.setText("INFO:" + "Can't connect. Problem:" + e.getMessage());
                    });
                }
            }
            Platform.runLater(() -> {
                setElementDisable(false);
            });
        };
        Thread myThread = new Thread(run, "QuoteThread");
        setElementDisable(true);
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
            try {
                data.setInterval((String) intervalCB.getValue(), intervalList.size());
            } catch (Exception e) {
                Platform.runLater(() -> {
                    infoLabel.setText("INFO:" + "Interval problem. Problem:" + e.getMessage());
                });
            }
            Platform.runLater(() -> {
                setElementDisable(false);
            });
        };
        Thread myThread = new Thread(run, "IntervalThread");
        setElementDisable(true);
        myThread.start();
    }

    static public void deleteAllStages() {
        for (int i = 0; i < stageList.size(); ++i) {
            stageList.get(i).close();
        }
    }

    @FXML
    public void onGetDataButton() {
        try {
            JfreeCandlestickChart temp = new JfreeCandlestickChart("heh");
            String interval = (String) intervalCB.getValue();
            if(!temp.setInterval(interval, intervalList, infoLabel){
                return;
            }
            //get date
            var beginDateStr = ((String) beginDateTF.getText());
            var endDateStr = ((String) endDateTF.getText());
            //analyze date
            if (!TimeUtils.isValidDate(beginDateStr)) {
                infoLabel.setText("INFO:" + "Begin date is wrong.");
                return;
            }
            if (!TimeUtils.isValidDate(endDateStr)) {
                infoLabel.setText("INFO:" + "End date is wrong.");
                return;
            }
            var beginDate = beginDateStr.split("\\.");
            var endDate = endDateStr.split("\\.");
            var beginDateDay = Integer.parseInt(beginDate[0]);
            var beginDateMonth = Integer.parseInt(beginDate[1]) - 1;
            var beginDateYear = Integer.parseInt(beginDate[2]);
            var beginMinDate = data.getMinDate().split("\\.");
            //if(beginDateDay>) TODO: Add checking of begin date and final date
            //set date
            data.setBeginData(Integer.parseInt(beginDate[0]),
                    Integer.parseInt(beginDate[1]) - 1,
                    Integer.parseInt(beginDate[2]));
            data.setEndData(Integer.parseInt(endDate[0]),
                    Integer.parseInt(endDate[1]) - 1,
                    Integer.parseInt(endDate[2]));
            data.getData();

            for (int i = 0; i < data.data.size(); ++i) {
                temp.onTrade(data.data.get(i));
            }
            temp.fillIndicators(data.data, 6, 0.5, 12, 26, 9); // TODO: take numbers from text fields and check it before
            var chartAndWindowName = (String) marketCB.getValue() + " " +
                    (String) quoteCB.getValue() + " " +
                    beginDateTF.getText() + "-" +
                    endDateTF.getText();
            JFreeChart chart = temp.createChart(chartAndWindowName);
            LegendTitle legend = new LegendTitle(chart.getPlot(), new ColumnArrangement(HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, 0),
                    new ColumnArrangement(HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, 0));
            legend.setPosition(RectangleEdge.BOTTOM);
            legend.setHorizontalAlignment(HorizontalAlignment.CENTER);
            legend.setBackgroundPaint(Color.WHITE);
            legend.setFrame(new LineBorder());
            legend.setMargin(0, 4, 5, 6);
            chart.addLegend(legend);
            ChartViewer viewer = new ChartViewer(chart);
            Stage stage = new Stage();
            stage.setScene(new Scene(viewer));
            stage.setTitle(chartAndWindowName);
            stage.show();
            stageList.add(stage);
        } catch (Exception e) {
            infoLabel.setText("INFO:" + "Something wrong.Problem:"+e.getMessage());
        }
    }
}