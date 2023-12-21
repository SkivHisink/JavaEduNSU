package org.lab.exlabwork1.View;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.lab.exlabwork1.Control.MainControl;
import org.lab.exlabwork1.Utility.Utility;

import java.io.*;

public class MainViewer {

    private MainControl parent;
    @FXML
    private ComboBox calculationType;
    @FXML
    private Button saveInExcelButton;
    @FXML
    private Button calculateButton;
    @FXML
    private Button openPlotWindowButton;
    @FXML
    private Button openTableWindowButton;
    @FXML
    private Label logLabel;
    @FXML
    private Label creditAmount;
    @FXML
    private Label creditTerm;
    @FXML
    private Label interestRate;
    @FXML
    private Label percentPaySum;
    @FXML
    private Label paymentDate;
    @FXML
    private Label dayOfTheContract;

    public MainViewer() {
    }


    public String getCalculationTypeValue() {
        return calculationType.getValue().toString();
    }

    boolean isOpenDialog = false;

    // Открыть окно с таблицей
    @FXML
    protected void onOpenTableWindowButton() {
        try {
            parent.openTableWindow(this);
        } catch (Exception e) {
            sendToLog("Не удалось открыть окно графика");
        }
    }

    // Сохранение результатов в Excel
    @FXML
    protected void onOpenExcelButtonClick() {
        FileChooser.ExtensionFilter extFilter;
        FileChooser openFileChooser = new FileChooser();
        extFilter = new FileChooser.ExtensionFilter(
                "Excel files (*.xlsx;*.xls)",
                "*.xlsx", "*.xls");
        openFileChooser.getExtensionFilters().add(extFilter);
        extFilter = new FileChooser.ExtensionFilter(
                "Any files (*.*)",
                "*.*");
        openFileChooser.getExtensionFilters().add(extFilter);
        openFileChooser.setTitle("Открытие файла");
        String info = "";
        if (!isOpenDialog) {
            isOpenDialog = true;
            File file = openFileChooser.showOpenDialog(new Stage());
            if (file != null) {
                var reader = parent.getReader();
                if (!reader.Read(file, info)) {
                    sendToLog(info);
                    isOpenDialog = false;
                    return;
                }
                // установка данных в интерфейс
                setInitialParamsToInterface();
                // Инициализация завершена
                calculationType.setDisable(false);
                calculateButton.setDisable(false);
                logLabel.setText(info);
            }
            isOpenDialog = false;
        }
    }

    // Установка начальных параметров в лейблы
    protected void setInitialParamsToInterface() {
        creditAmount.setText(Utility.CreditAmountHeader + parent.getReader().getCreditAmountVal() + "₽");
        creditTerm.setText(Utility.CreditTermHeader + parent.getReader().getCreditTermVal() + " месяц -а/-ев");
        interestRate.setText(Utility.InterestRateHeader + parent.getReader().getInterestRateVal() + "%");
        paymentDate.setText(Utility.PaymentDateHeader + parent.getReader().getPaymentDateVal());
        dayOfTheContract.setText(Utility.DayOfTheContractHeader + parent.getReader().getDayofTheContractVal());
        calculationType.setItems(FXCollections.observableList(parent.getPaymentTypesList()));
        calculationType.setValue(parent.getPaymentTypesList().get(0));
    }

    public void setPercentPaySum(String result) {
        percentPaySum.setText(result);
    }

    @FXML
    protected void onCalculateButton() throws IllegalAccessException {
        parent.calculatePayment(this);
        if (parent.getIsCalculated() == false) {
            return;
        }
        openPlotWindowButton.setDisable(false);
        openTableWindowButton.setDisable(false);
        saveInExcelButton.setDisable(false);
        sendToLog("Успешно рассчитано.");
        if (parent != null) {
            parent.insideAddSummon();
        }
    }

    @FXML
    protected void onOpenPlotWindowButton() {
        try {
            parent.openPlotWindow(this);
        } catch (Exception e) {
            sendToLog("Не удалось открыть окно графика");
        }
    }

    @FXML
    protected void onSaveInExcelButton() {
        parent.saveInExcel();
    }

    public void sendToLog(String info) {
        logLabel.setText(Utility.logHeader + info);
    }

    public void setParent(MainControl parent) {
        this.parent = parent;
    }

    public boolean getIsOpenDialog() {
        return isOpenDialog;
    }

    public void setIsOpenDialog(boolean isOpen) {
        isOpenDialog = isOpen;
    }
}