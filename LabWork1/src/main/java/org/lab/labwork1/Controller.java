package org.lab.labwork1;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class Controller {
    // Elements in UI
    @FXML
    private Label welcomeText;
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
    private Button saveInExcelButton;
    @FXML
    private Button calculateButton;
    @FXML
    private Button drawGraphicButton;
    @FXML
    private Label infoText; // like a log for user
    private List<String> val;
    private ObservableList<String> observableList;
    private String creditAmountLabelText = "Credit amount:";
    @FXML
    private Label creditAmount;
    private String creditTermLabelText = "Credit term:";
    @FXML
    private Label creditTerm;
    private String interestRateLabelText = "Interest rate:";
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
    String dayofTheContractVal;
    int paymentDateVal;
    double interestRateVal;
    int creditTermVal;
    double creditAmountVal;
    // Additional
    private String infoBegin = "INFO:";
    boolean isReaded = false;
    boolean isCalculated = false;
    PaymentBase paymentMethod = null;

    public Controller() {
        //Generating payment types
        val = new ArrayList<String>();
        val.add("Annuity payment");
        val.add("Different payment");
        observableList = FXCollections.observableList(val);
    }

    @FXML
    protected void onOpenExcelButtonClick() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Excel files (*.xlsx)", // TODO: ;*.xls
                "*.xlsx"); // TODO: add supporting of , "*.xls"
        chooser.getExtensionFilters().add(extFilter);
        extFilter = new FileChooser.ExtensionFilter(
                "Any files (*.*)",
                "*.*");
        chooser.getExtensionFilters().add(extFilter);
        File file = chooser.showOpenDialog(new Stage());
        if (file != null) {
            FileInputStream fileInputStream = new FileInputStream(file.getPath());
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = xssfWorkbook.getSheet(xssfWorkbook.getSheetName(0));
            Row row = sheet.getRow(1);
            if (row == null) {
                // TODO: write something in INFO
                return;
            }
            if (row.getCell(4) == null) {
                // TODO: write something in INFO
                return;
            }
            // getting data from file
            creditAmountVal = row.getCell(0).getNumericCellValue();
            creditTermVal = (int) row.getCell(1).getNumericCellValue();
            interestRateVal = row.getCell(2).getNumericCellValue();
            paymentDateVal = (int) row.getCell(3).getNumericCellValue();
            dayofTheContractVal = row.getCell(4).getStringCellValue();
            if(paymentDateVal>28){
                infoText.setText(infoBegin + "Payment date must be less or equal to 28.");
                return;
            }
            // setting data to interface
            creditAmount.setText(creditAmountLabelText + creditAmountVal + "₽");
            creditTerm.setText(creditTermLabelText + creditTermVal + " month");
            interestRate.setText(interestRateLabelText + interestRateVal + "%");
            paymentDate.setText(paymentDateLabelText + paymentDateVal);
            dayOfTheContract.setText(dayOfTheContractLabelText + dayofTheContractVal);
            //
            calculationType.setItems(observableList);
            calculationType.setValue(val.get(0));
            // Finish
            isReaded = true;
            calculationType.setDisable(false);
            calculateButton.setDisable(false);
            resultTable.setDisable(false);
        }
    }

    @FXML
    protected void onCalculateButton() throws Exception {
        isCalculated = false;
        for (int i = 0; i < resultTable.getItems().size(); i++) {
            resultTable.getItems().clear();
        }
        col1.setCellValueFactory(new PropertyValueFactory<>("N"));
        col2.setCellValueFactory(new PropertyValueFactory<>("dayOfUsing"));
        col3.setCellValueFactory(new PropertyValueFactory<>("paymentDateVal"));
        col4.setCellValueFactory(new PropertyValueFactory<>("generalPaymentSize"));
        col5.setCellValueFactory(new PropertyValueFactory<>("percentSum"));
        col6.setCellValueFactory(new PropertyValueFactory<>("sumOfFee"));
        col7.setCellValueFactory(new PropertyValueFactory<>("feeLeft"));
        if (!isReaded) {
            // TODO: send signal to info
            return;
        }
        List<DataForTable> dataTemp = new ArrayList<DataForTable>();

        // Annuity payment
        if (calculationType.getValue() == val.get(0)) {
            paymentMethod = new AnnuityPayment(creditAmountVal, creditTermVal,
                    interestRateVal, paymentDateVal, dayofTheContractVal);
        }
        // Different payment
        else {
            paymentMethod = new DifferentPayment(creditAmountVal, creditTermVal,
                    interestRateVal, paymentDateVal, dayofTheContractVal);
        }
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < creditTermVal + 1; ++i) {
            DataForTable tmp = null;
            if (i == 0) {
                tmp = paymentMethod.getFirstMonthFee();
            } else {
                tmp = paymentMethod.getNotFirstMonthFee(i);
            }
            tmp.setN(i);
            series.getData().add(new XYChart.Data(Integer.toString(tmp.getN()),
                    tmp.getFeeLeft()));
            resultTable.getItems().add(tmp);
        }
        drawGraphicButton.setDisable(false);
        saveInExcelButton.setDisable(false);
        graphicLineChart.getData().clear();
        graphicLineChart.getData().add(series);
        isCalculated = true;
    }

    @FXML
    protected void onDrawGraphicButton() {
        resultTable.setVisible(!resultTable.isVisible());
        graphicLineChart.setVisible(!graphicLineChart.isVisible());
    }

    @FXML
    protected void onSaveInExcelButton() throws IOException {
        if (!isCalculated) {
            // TODO: send signal to info
            return;
        }
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Excel files (*.xlsx)", // TODO: ;*.xls
                "*.xlsx"); // TODO: add supporting of , "*.xls"
        chooser.getExtensionFilters().add(extFilter);
        extFilter = new FileChooser.ExtensionFilter(
                "Any files (*.*)",
                "*.*");
        chooser.getExtensionFilters().add(extFilter);
        File file = chooser.showSaveDialog(new Stage());
        if (file != null) {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            XSSFWorkbook myWorkBook = new XSSFWorkbook();
            myWorkBook.createSheet("ResultSheet");
            Sheet resultSheet = myWorkBook.getSheet(myWorkBook.getSheetName(0));
            for (int i = 0; i < 4; ++i) {
                resultSheet.createRow(i);
            }
            Row row = resultSheet.getRow(0);
            for (int i = 0; i < 6; ++i) {
                row.createCell(i);
            }
            row.getCell(0).setCellValue("Сумма кредита");
            row.getCell(1).setCellValue("Срок кредита");
            row.getCell(2).setCellValue("Процентная ставка");
            row.getCell(3).setCellValue("Дата платежа");
            row.getCell(4).setCellValue("Кредит предоставляется заемщику");
            row.getCell(5).setCellValue("Тип рассчёта");
            row = resultSheet.getRow(1);

            for (int i = 0; i < 6; ++i) {
                row.createCell(i);
            }
            row.getCell(0).setCellValue(creditAmountVal);
            row.getCell(1).setCellValue(creditTermVal);
            row.getCell(2).setCellValue(interestRateVal);
            row.getCell(3).setCellValue(paymentDateVal);
            row.getCell(4).setCellValue(dayofTheContractVal);
            row.getCell(5).setCellValue(paymentMethod.getPaymentType());
            row = resultSheet.getRow(2);
            for (int i = 0; i < 5; ++i) {
                row.createCell(i);
            }
            row.getCell(0).setCellValue("n");
            row.getCell(1).setCellValue("Кол-во дней пользования заемными средствами");
            row.getCell(2).setCellValue("Дата платежа");
            row.getCell(3).setCellValue("Общая сумма платежа");
            row.getCell(4).setCellValue("В том числе");
            row = resultSheet.getRow(3);
            for (int i = 0; i < 7; ++i) {
                row.createCell(i);
            }
            row.getCell(4).setCellValue("сумма процентов");
            row.getCell(5).setCellValue("сумма погашаемого долга");
            row.getCell(6).setCellValue("остаток задолжности");
            for (int i = 0; i < resultTable.getItems().size(); ++i) {
                resultSheet.createRow(i + 4);
                row = resultSheet.getRow(resultSheet.getLastRowNum());
                for (int j = 0; j < 7; ++j) {
                    row.createCell(j);
                }
                var item = (DataForTable) resultTable.getItems().get(i);
                row.getCell(0).setCellValue(item.getN());
                row.getCell(1).setCellValue(item.getDayOfUsing());
                row.getCell(2).setCellValue(item.getPaymentDateVal());
                row.getCell(3).setCellValue(item.getGeneralPaymentSize());
                row.getCell(4).setCellValue(item.getPercentSum());
                row.getCell(5).setCellValue(item.getSumOfFee());
                row.getCell(6).setCellValue(item.getFeeLeft());
            }
            myWorkBook.write(out);
            out.close();
        }
    }
}