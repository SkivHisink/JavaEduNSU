package org.lab.labwork1;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Controller {
    // Elements in UL
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
    //Data
    String dayofTheContractVal;
    int paymentDateVal;
    double interestRateVal;
    int creditTermVal;
    double creditAmountVal;
    // Additional
    private String infoBegin = "INFO:";
    boolean isReaded = false;

    @FXML
    protected void onHelloButtonClick() {
        //test button
        welcomeText.setText("Welcome to JavaFX Application!");
        infoText.setText(infoBegin + "Test button is work successfully.");
    }

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
            // setting data to interface
            creditAmount.setText(creditAmountLabelText + creditAmountVal + " month");
            creditTerm.setText(creditTermLabelText + creditTermVal);
            interestRate.setText(interestRateLabelText + interestRateVal + "%");
            paymentDate.setText(paymentDateLabelText + paymentDateVal);
            dayOfTheContract.setText(dayOfTheContractLabelText + dayofTheContractVal);
            //
            calculationType.setItems(observableList);
            calculationType.setValue(val.get(0));
            // Finish
            isReaded = true;
        }
    }

    @FXML
    protected void onCalculateButton() throws Exception {
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
        PaymentBase paymentMethod = null;
        // Annuity payment
        if (calculationType.getValue() == val.get(0)) {
            paymentMethod = new AnnuityPayment(creditAmountVal, creditTermVal,
                    interestRateVal, paymentDateVal, dayofTheContractVal);
            for (int i = 0; i < creditTermVal + 1; ++i) {
                DataForTable tmp = null;
                if (i == 0) {
                    tmp = paymentMethod.getFirstMonthFee();
                }
                else {
                    tmp = paymentMethod.getNotFirstMonthFee(i);
                }
                tmp.setN(i);
                resultTable.getItems().add(tmp);
                System.out.println("all good");
            }
        }
        // Different payment
        else {

        }
    }
}