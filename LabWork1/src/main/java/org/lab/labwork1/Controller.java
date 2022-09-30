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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
    private initParamReader reader;
    // Additional
    private boolean isCalculated = false;
    private PaymentBase paymentMethod = null;
    private Stage helpStage;
    private boolean isRuLang = true;
    @FXML
    private TextArea readmeTextArea;
    @FXML
    private Button addGraphicButton;
    Map<String, XYChart.Series> graphicsContainer;
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
        reader = new initParamReader();
        graphicsContainer = new HashMap<>();
    }


    @FXML
    protected void onOpenExcelButtonClick() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Excel files (*.xlsx;*.xls)",
                "*.xlsx", "*.xls");
        chooser.getExtensionFilters().add(extFilter);
        extFilter = new FileChooser.ExtensionFilter(
                "Any files (*.*)",
                "*.*");
        chooser.getExtensionFilters().add(extFilter);
        File file = chooser.showOpenDialog(new Stage());
        if (file != null) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file.getPath());
            } catch (FileNotFoundException e) {
                infoText.setText(Utility.InfoBegin + e.getMessage());
                return;
            }
            String extension = "";
            int i = file.getPath().lastIndexOf('.');
            if (i > 0) {
                extension = file.getPath().substring(i + 1);
            }
            Workbook workBook = null;
            if (extension.equals("xlsx")) {
                workBook = new XSSFWorkbook(fileInputStream);
            } else if (extension.equals("xls")) {
                workBook = new HSSFWorkbook(fileInputStream);
            } else {
                infoText.setText(Utility.InfoBegin + "Can't parse this type of file.");
                return;
            }
            String info = "";
            if (!reader.Read(workBook, info)) {
                infoText.setText(info);
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
            yDataType.setItems(xyObservableList);
            // Finish
            reader.setRead(true);
            calculationType.setDisable(false);
            calculateButton.setDisable(false);
            resultTable.setDisable(false);
            infoText.setText(info);
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
        // Legacy code
        if (!reader.isRead()) {
            infoText.setText(Utility.InfoBegin + "Read init file first.");
            return;
        }
        // end of legacy code
        List<DataForTable> dataTemp = new ArrayList<DataForTable>();

        // Annuity payment
        if (calculationType.getValue() == val.get(0)) {
            paymentMethod = new AnnuityPayment(
                    reader.getCreditAmountVal(),
                    reader.getCreditTermVal(),
                    reader.getInterestRateVal(),
                    reader.getPaymentDateVal(),
                    reader.getDayofTheContractVal());
        }
        // Different payment
        else {
            paymentMethod = new DifferentPayment(
                    reader.getCreditAmountVal(),
                    reader.getCreditTermVal(),
                    reader.getInterestRateVal(),
                    reader.getPaymentDateVal(),
                    reader.getDayofTheContractVal());
        }
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < reader.getCreditTermVal() + 1; ++i) {
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
        series.setName("results");
        //graphicLineChart.setTitle("results");
        isCalculated = true;
        infoText.setText(Utility.InfoBegin + "Calculated.");
    }

    @FXML
    protected void onAddGraphicButton() {

    }

    @FXML
    protected void onDrawGraphicButton() {
        resultTable.setVisible(!resultTable.isVisible());
        graphicLineChart.setVisible(!graphicLineChart.isVisible());
        // xDataType.setDisable(!xDataType.isDisabled());
        //  yDataType.setDisable(!yDataType.isDisabled());
        //  addGraphicButton.setDisable(!addGraphicButton.isDisabled());
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
        File file = chooser.showSaveDialog(new Stage());
        if (file != null) {
            // TODO: add ResultSaver class
            FileOutputStream out = null;
            try {
                file.createNewFile();
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                infoText.setText(Utility.InfoBegin + e.getMessage());
                return;
            }
            XSSFWorkbook myWorkBook = new XSSFWorkbook();
            myWorkBook.createSheet("ResultSheet");
            Sheet resultSheet = myWorkBook.getSheet(myWorkBook.getSheetName(0));
            for (int i = 0; i < 4; ++i) {
                resultSheet.createRow(i);
            }
            Row row = resultSheet.getRow(0);
            // TODO: Solve this strange thing
            for (int i = 0; i < 6; ++i) {
                row.createCell(i);
            }
            row.getCell(0).setCellValue("Сумма кредита");
            row.getCell(1).setCellValue("Срок кредита");
            row.getCell(2).setCellValue("Процентная ставка");
            row.getCell(3).setCellValue("Дата платежа");
            row.getCell(4).setCellValue("Кредит предоставляется заемщику");
            row.getCell(5).setCellValue("Тип расчета");
            row = resultSheet.getRow(1);
            // TODO: Solve this strange thing
            for (int i = 0; i < 6; ++i) {
                row.createCell(i);
            }
            row.getCell(0).setCellValue(reader.getCreditAmountVal());
            row.getCell(1).setCellValue(reader.getCreditTermVal());
            row.getCell(2).setCellValue(reader.getInterestRateVal());
            row.getCell(3).setCellValue(reader.getPaymentDateVal());
            row.getCell(4).setCellValue(reader.getDayofTheContractVal());
            row.getCell(5).setCellValue(paymentMethod.getPaymentType());
            row = resultSheet.getRow(2);
            // TODO: Solve this strange thing
            for (int i = 0; i < 5; ++i) {
                row.createCell(i);
            }
            row.getCell(0).setCellValue("n");
            row.getCell(1).setCellValue("Кол-во дней пользования заемными средствами");
            row.getCell(2).setCellValue("Дата платежа");
            row.getCell(3).setCellValue("Общая сумма платежа");
            row.getCell(4).setCellValue("В том числе");
            row = resultSheet.getRow(3);
            // TODO: Solve this strange thing
            for (int i = 0; i < 7; ++i) {
                row.createCell(i);
            }
            row.getCell(4).setCellValue("сумма процентов");
            row.getCell(5).setCellValue("сумма погашаемого долга");
            row.getCell(6).setCellValue("остаток задолжности");
            for (int i = 0; i < resultTable.getItems().size(); ++i) {
                resultSheet.createRow(i + 4);
                row = resultSheet.getRow(resultSheet.getLastRowNum());
                // TODO: Solve this strange thing
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
            infoText.setText(Utility.InfoBegin + "File saved successful.");
        }
    }
}