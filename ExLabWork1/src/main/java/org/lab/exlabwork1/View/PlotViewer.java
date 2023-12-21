package org.lab.exlabwork1.View;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import org.lab.exlabwork1.Control.MainControl;

import java.util.ArrayList;
import java.util.List;

public class PlotViewer {
    // Кнопки
    @FXML
    private Button addPlotButton;
    @FXML
    private Button clearPlotButton;
    @FXML
    private Button deletePlotButton;
    // Комбобоксы
    @FXML
    private ComboBox xDataType;
    @FXML
    private ComboBox yDataType;
    @FXML
    private ComboBox plotsComboBox;
    // График
    @FXML
    private LineChart lineChart;
    // Остальное
    private MainControl parent;
    int plotCounter = 0;
    private List<String> plotNamesList;

    public PlotViewer() {
        plotNamesList = new ArrayList<>();
    }

    public void setParent(MainControl parent) {
        this.parent = parent;
        xDataType.setDisable(!xDataType.isDisabled());
        yDataType.setDisable(!yDataType.isDisabled());
        addPlotButton.setDisable(!addPlotButton.isDisabled());
        clearPlotButton.setDisable(!clearPlotButton.isDisabled());
        plotsComboBox.setDisable(!plotsComboBox.isDisabled());
    }

    public void CheckSolutionState() {
        if (this.parent.getIsCalculated()) {
            xDataType.setDisable(!xDataType.isDisabled());
            yDataType.setDisable(!yDataType.isDisabled());
            addPlotButton.setDisable(!addPlotButton.isDisabled());
            clearPlotButton.setDisable(!clearPlotButton.isDisabled());
            plotsComboBox.setDisable(!plotsComboBox.isDisabled());
        }
    }

    public ComboBox getxDataType() {
        return xDataType;
    }

    public ComboBox getyDataType() {
        return yDataType;
    }

    @FXML
    protected void onAddPlotButton() {
        try {
            var xData = parent.getData(this, (String) xDataType.getValue());
            var yData = parent.getData(this, (String) yDataType.getValue());
            XYChart.Series series = new XYChart.Series();
            for (int i = 0; i < xData.size(); ++i) {
                series.getData().add(new XYChart.Data(xData.get(i), Double.parseDouble(yData.get(i))));
            }
            series.setName("plot_" + plotCounter);
            plotNamesList.add(series.getName());
            plotCounter++;
            lineChart.getData().add(series);
            lineChart.setDisable(true);
            lineChart.setDisable(false);
            var tempObsList = FXCollections.observableList(plotNamesList);
            plotsComboBox.setItems(tempObsList);
        } catch (Exception e) {
            parent.sendToLog("Невозможно установить данные для этого типа данных xy. Ошибка:" + e.getMessage());
        }
    }

    public void insideAddSummon() {
        onAddPlotButton();
    }

    @FXML
    protected void onClearPlotButton() {
        lineChart.getData().clear();
        plotNamesList.clear();
    }

    @FXML
    protected void onDeletePlotButton() {
        var index = plotNamesList.indexOf(plotsComboBox.getValue());
        if (index == -1) {
            parent.sendToLog("Выбирите график для удаления!");
            return;
        }
        var data = lineChart.getData();
        data.remove(index);
        plotsComboBox.getItems().remove(index);
    }
}
