package org.lab.labwork1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class Controller {
    // Elements in UI
    @FXML
    private TableView resultTable;
    @FXML
    private TableColumn col1;
    @FXML
    private TableColumn col2;
    @FXML
    private TableColumn col3;
    @FXML
    private TableColumn col4;
    @FXML
    private TableColumn col5;
    @FXML
    private TableColumn col6;
    @FXML
    private TableColumn col7;
    @FXML
    private ComboBox calculationType;
    @FXML
    private ComboBox xDataType;
    @FXML
    private ComboBox yDataType;
    @FXML
    private ComboBox deleteGraphicNames;
    @FXML
    private Button saveInExcelButton;
    @FXML
    private Button calculateButton;
    @FXML
    private Button drawGraphicButton;
    @FXML
    private Label infoText; // like a log for user
    private List<String> val;
    private List<String> xyDataTypeNames;
    private ObservableList<String> observableList;
    private ObservableList<String> xyObservableList;
    @FXML
    private Label creditAmount;
    @FXML
    private Label creditTerm;
    @FXML
    private Label interestRate;
    private String paymentDateLabelText = "Payment date:";
    @FXML
    private Label paymentDate;
    private String dayOfTheContractLabelText = "Credit date:";
    @FXML
    private Label dayOfTheContract;
    @FXML
    private LineChart graphicLineChart;
    //Data
    private InitParamReader reader;
    // Additional
    private boolean isCalculated = false;
    private PaymentBase paymentMethod = null;
    private Stage helpStage;
    private boolean isRuLang = true;
    @FXML
    private TextArea readmeTextArea;
    @FXML
    private Button addGraphicButton;
    @FXML
    private Button deleteGraphicButton;
    @FXML
    private ComboBox graphicComboBox;
    Map<String, XYChart.Series> graphicsContainer;
    private List<String> graphicNamesList;
    int counterGraph = 0;
    private String info = "";

    public Controller() {
        //Generating payment types
        val = new ArrayList<String>();
        val.add("Annuity payment");
        val.add("Different payment");
        xyDataTypeNames = new ArrayList<String>();
        xyDataTypeNames.add("N");
        xyDataTypeNames.add("Кол-во дней пользования заемными средствами");
        xyDataTypeNames.add("Дата платежа");
        xyDataTypeNames.add("Общая сумма платежа");
        xyDataTypeNames.add("Сумма процентов");
        xyDataTypeNames.add("Сумма погашаемого долга");
        xyDataTypeNames.add("Остаток задолжности");
        observableList = FXCollections.observableList(val);
        xyObservableList = FXCollections.observableList(xyDataTypeNames);
        reader = new InitParamReader();
        graphicsContainer = new HashMap<>();
        graphicNamesList = new ArrayList<>();
    }
boolean isOpenDialog = false;
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
        openFileChooser.setTitle("Open File");
        col1.setCellValueFactory(new PropertyValueFactory<>("N"));
        col2.setCellValueFactory(new PropertyValueFactory<>("dayOfUsing"));
        col3.setCellValueFactory(new PropertyValueFactory<>("paymentDateVal"));
        col4.setCellValueFactory(new PropertyValueFactory<>("generalPaymentSize"));
        col5.setCellValueFactory(new PropertyValueFactory<>("percentSum"));
        col6.setCellValueFactory(new PropertyValueFactory<>("sumOfFee"));
        col7.setCellValueFactory(new PropertyValueFactory<>("feeLeft"));
        if(!isOpenDialog) {
            isOpenDialog = true;
            File file = openFileChooser.showOpenDialog(new Stage());
            if (file != null) {
                if (!reader.Read(file, info)) {
                    infoText.setText(Utility.InfoBegin + info);
                    isOpenDialog = false;
                    return;
                }
                // setting data to interface
                creditAmount.setText(Utility.CreditAmountText + reader.getCreditAmountVal() + "₽");
                creditTerm.setText(Utility.CreditTermText + reader.getCreditTermVal() + " month");
                interestRate.setText(Utility.InterestRateText + reader.getInterestRateVal() + "%");
                paymentDate.setText(paymentDateLabelText + reader.getPaymentDateVal());
                dayOfTheContract.setText(dayOfTheContractLabelText + reader.getDayofTheContractVal());
                //
                calculationType.setItems(observableList);
                calculationType.setValue(val.get(0));
                //
                xDataType.setItems(xyObservableList);
                xDataType.setValue(xyObservableList.get(0));
                yDataType.setItems(xyObservableList);
                yDataType.setValue(xyObservableList.get(6));
                // Finish
                reader.setRead(true);
                calculationType.setDisable(false);
                calculateButton.setDisable(false);
                resultTable.setDisable(false);
                infoText.setText(info);
            }
            isOpenDialog = false;
        }
    }

    private void setPaymentType() {
        if (calculationType.getValue() == val.get(0)) {
            paymentMethod = new AnnuityPayment(
                    reader.getCreditAmountVal(),
                    reader.getCreditTermVal(),
                    reader.getInterestRateVal(),
                    reader.getPaymentDateVal(),
                    reader.getDayofTheContractVal());
        }
        // Different payment
        else if (calculationType.getValue() == val.get(1)) {
            paymentMethod = new DifferentPayment(
                    reader.getCreditAmountVal(),
                    reader.getCreditTermVal(),
                    reader.getInterestRateVal(),
                    reader.getPaymentDateVal(),
                    reader.getDayofTheContractVal());
        } else {
            paymentMethod = null;
            info = "Payment method problem";
        }
    }

    @FXML
    protected void onCalculateButton() {
        isCalculated = false;
        for (int i = 0; i < resultTable.getItems().size(); i++) {
            resultTable.getItems().clear();
        }
        setPaymentType();
        if (paymentMethod == null) {
            infoText.setText(Utility.InfoBegin + info);
            return;
        }
        try {
            DataForTable tmp = null;
            tmp = paymentMethod.getFirstMonthFee();
            tmp.setN(0);
            resultTable.getItems().add(tmp);
            for (int i = 1; i < reader.getCreditTermVal() + 1; ++i) {
                tmp = paymentMethod.getNotFirstMonthFee(i, info);
                if (tmp == null) {
                    break;
                }
                tmp.setN(i);
                resultTable.getItems().add(tmp);
            }
            if (tmp == null) {
                infoText.setText(Utility.InfoBegin + info);
                return;
            }
            tmp = (DataForTable) resultTable.getItems().get(resultTable.getItems().size() - 1);
            tmp.setGeneralPaymentSize(Utility.bankingRound(
                    tmp.getGeneralPaymentSize() + tmp.getFeeLeft()));
            tmp.setFeeLeft(0);
            tmp.setSumOfFee(Utility.bankingRound(
                    tmp.getGeneralPaymentSize()-tmp.getPercentSum()));
            resultTable.getItems().set(resultTable.getItems().size() - 1, (Object) tmp);
        } catch (Exception e) {
            infoText.setText(Utility.InfoBegin +
                    "Problem in calculations. Error: " + e.getMessage());
            return;
        }
        drawGraphicButton.setDisable(false);
        saveInExcelButton.setDisable(false);
        isCalculated = true;
        info = "Calculated.";
        infoText.setText(Utility.InfoBegin + info);
        onAddGraphicButton();
    }

    private List<String> getData(String type) {
        List<String> result = new ArrayList<>();
        if (type == xyDataTypeNames.get(0)) {
            for (int i = 0; i < reader.getCreditTermVal() + 1; ++i) {
                DataForTable tmp = (DataForTable) resultTable.getItems().get(i);
                result.add(Integer.toString(tmp.getN()));
            }
        } else if (type == xyDataTypeNames.get(1)) {
            for (int i = 0; i < reader.getCreditTermVal() + 1; ++i) {
                DataForTable tmp = (DataForTable) resultTable.getItems().get(i);
                result.add(Integer.toString(tmp.getDayOfUsing()));
            }

        } else if (type == xyDataTypeNames.get(2)) {
            for (int i = 0; i < reader.getCreditTermVal() + 1; ++i) {
                DataForTable tmp = (DataForTable) resultTable.getItems().get(i);
                result.add(tmp.getPaymentDateVal());
            }
        } else if (type == xyDataTypeNames.get(3)) {
            for (int i = 0; i < reader.getCreditTermVal() + 1; ++i) {
                DataForTable tmp = (DataForTable) resultTable.getItems().get(i);
                result.add(Double.toString(tmp.getGeneralPaymentSize()));
            }
        } else if (type == xyDataTypeNames.get(4)) {
            for (int i = 0; i < reader.getCreditTermVal() + 1; ++i) {
                DataForTable tmp = (DataForTable) resultTable.getItems().get(i);
                result.add(Double.toString(tmp.getPercentSum()));
            }
        } else if (type == xyDataTypeNames.get(5)) {
            for (int i = 0; i < reader.getCreditTermVal() + 1; ++i) {
                DataForTable tmp = (DataForTable) resultTable.getItems().get(i);
                result.add(Double.toString(tmp.getSumOfFee()));
            }
        } else if (type == xyDataTypeNames.get(6)) {
            for (int i = 0; i < reader.getCreditTermVal() + 1; ++i) {
                DataForTable tmp = (DataForTable) resultTable.getItems().get(i);
                result.add(Double.toString(tmp.getFeeLeft()));
            }
        } else {
            info = "Data type for " + type + " data isn't chosen. Please choose data type ";
        }
        return result;
    }

    @FXML
    protected void onAddGraphicButton() {
        var xData = getData((String) xDataType.getValue());
        var yData = getData((String) yDataType.getValue());
        try {
            XYChart.Series series = new XYChart.Series();
            for (int i = 0; i < xData.size(); ++i) {
                series.getData().add(new XYChart.Data(xData.get(i), Double.parseDouble(yData.get(i))));
            }
            series.setName("graphic_" + counterGraph);
            graphicNamesList.add(series.getName());
            graphicsContainer.put(Integer.toString(counterGraph), series); // Are we need it?! // TODO: solve it
            counterGraph++;
            graphicLineChart.getData().add(series);
            graphicLineChart.setDisable(true);
            graphicLineChart.setDisable(false);
            var tempObsList = FXCollections.observableList(graphicNamesList);
            graphicComboBox.getItems().clear();
            graphicComboBox.setItems(tempObsList);
        } catch (Exception e) {
            info = "Can't set data for this xy data types. Error:" + e.getMessage();
            infoText.setText(Utility.InfoBegin + info);
        }
    }

    @FXML
    protected void onDrawGraphicButton() {
        resultTable.setVisible(!resultTable.isVisible());
        graphicLineChart.setVisible(!graphicLineChart.isVisible());
        xDataType.setDisable(!xDataType.isDisabled());
        yDataType.setDisable(!yDataType.isDisabled());
        addGraphicButton.setDisable(!addGraphicButton.isDisabled());
        deleteGraphicButton.setDisable(!deleteGraphicButton.isDisabled());
        graphicComboBox.setDisable(!graphicComboBox.isDisabled());
    }

    @FXML
    protected void onHelpButton() {
        try {
            if (helpStage == null || !helpStage.isShowing()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("readme.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                helpStage = new Stage();
                helpStage.setTitle("Readme");
                helpStage.setScene(scene);
                helpStage.setResizable(false);
                helpStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onRuLangButton() {
        if (!isRuLang) {
            readmeTextArea.setText(Utility.ReadmeRuText);
            isRuLang = !isRuLang;
        }
    }

    @FXML
    protected void onEnLangButton() {
        if (isRuLang) {
            readmeTextArea.setText(Utility.ReadmeEnText);
            isRuLang = !isRuLang;
        }
    }

    @FXML
    protected void onSaveInExcelButton() {
        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Save File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Excel files (*.xlsx)", // TODO: ;*.xls
                "*.xlsx"); // TODO: add supporting of , "*.xls"
        saveFileChooser.getExtensionFilters().add(extFilter);
        if(!isOpenDialog) {
            isOpenDialog=true;
            File file = saveFileChooser.showSaveDialog(new Stage());
            if (file != null) {
                ResultSaver saver = new ResultSaver();
                if (!saver.createOrOpenFileStream(file, info)) {
                    infoText.setText(Utility.InfoBegin + info);
                    isOpenDialog=false;
                    return;
                }
                if (!saver.saveData(resultTable.getItems(), paymentMethod, info)) {
                    infoText.setText(Utility.InfoBegin + info);
                    isOpenDialog=false;
                    return;
                }
                infoText.setText(Utility.InfoBegin + "File saved successful.");
            }
            isOpenDialog=false;
        }
    }

    @FXML
    protected void onDeleteGraphicButton() {
        graphicLineChart.getData().clear();
    }
}