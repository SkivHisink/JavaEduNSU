package org.lab.labwork1;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
    private String dayofTheContractVal;
    private int paymentDateVal;
    private double interestRateVal;
    private int creditTermVal;
    private double creditAmountVal;
    // Additional
    private String infoBegin = "INFO:";
    private boolean isReaded = false;
    private boolean isCalculated = false;
    private PaymentBase paymentMethod = null;
    private Stage helpStage;
    private boolean isRuLang = true;
    @FXML
    private TextArea readmeTextArea;
    String readmeRuText = "Для использования данной программы вам необходимо проделать следующие шаги:\n" +
            "1) Создать файл формата \"*.xlsx\";\n" +
            "2) На первом листе на второй строке разместить входные данные в общем формате ячеек где | - новая ячейка:\n" +
            "Сумма кредита(численное значение)|Срок кредита(целое численное значение)|Процентная ставка(численное значение)|Дата платежа(целое численное значение)| " +
            "Кредит предоставляется заемщику(дата через точку). Пример данных:\n" +
            "\"9200000|276|7.45|25|22.09.2022\". В результате должна получится строка с 5-ю заполненными ячейками;\n" +
            "3) Загрузите ваш файл с помощью кнопки \"Open init param\";\n" +
            "4) Выберите тип расчёта и нажмите кнопку \"Calculate\";\n" +
            "5) При необходимости вы можете посмотреть график или сохранить результаты при помощи кнопок \"Draw graphic\" и \"Save in Excel\" соответственно.";
    String readmeEnText = "To use this program, you need to do the following steps:\n" +
            "1) Create a file of \"*.xlsx\" format;\n" +
            "2) On the first sheet on the second line, place the input data in the general format of cells where | - new cell:\n" +
            "Credit amount(numerical value)|Credit term(integer numerical value)|Interest rate(numerical value)|Payment date(integer numerical value)| \n" +
            "The credit is provided to the borrower (date separated by a dot).Sample data:\"9200000|276|7.45|25|22.09.2022\".\n" +
            "The result should be a line with 5 filled cells;\n" +
            "3) Upload your file using the \"Open init param\" button;\n" +
            "4) Select the calculation type and click the \"Calculate\" button;\n" +
            "5) If necessary, you can view the graph or save the results using the \"Draw graphic\" and \"Save in Excel\" buttons, respectively.";

    public Controller() {
        //Generating payment types
        val = new ArrayList<String>();
        val.add("Annuity payment");
        val.add("Different payment");
        observableList = FXCollections.observableList(val);
    }
    protected boolean readWorkbook(Workbook workBook)
    {
        Sheet sheet = workBook.getSheet(workBook.getSheetName(0));
        Row row = sheet.getRow(1);
        if (row == null) {
            infoText.setText(infoBegin + "Init data is incorrect.");
            return false;
        }
        if (row.getCell(4) == null) {
            infoText.setText(infoBegin + "Init data is incorrect.");
            return false;
        }
        // getting data from file
        creditAmountVal = row.getCell(0).getNumericCellValue();
        creditTermVal = (int) row.getCell(1).getNumericCellValue();
        interestRateVal = row.getCell(2).getNumericCellValue();
        paymentDateVal = (int) row.getCell(3).getNumericCellValue();
        dayofTheContractVal = row.getCell(4).getStringCellValue();
        if (paymentDateVal > 28) {
            infoText.setText(infoBegin + "Payment date must be less or equal to 28.");
            return false;
        }
        return true;
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
                infoText.setText(infoBegin + e.getMessage());
                return;
            }
            String extension = "";
            int i = file.getPath().lastIndexOf('.');
            if (i > 0) {
                extension = file.getPath().substring(i+1);
            }
            Workbook workBook = null;
            if(extension.equals("xlsx")) {
                workBook = new XSSFWorkbook(fileInputStream);
            }
            else if(extension.equals("xls")){
                workBook = new HSSFWorkbook(fileInputStream);
            }
            else{
                infoText.setText(infoBegin + "Can't parse this type of file.");
                return;
            }
            readWorkbook(workBook);
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
            infoText.setText(infoBegin + "File read successful.");
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
        if (!isReaded) {
            infoText.setText(infoBegin + "Read init file first.");
            return;
        }
        // end of legacy code
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
        series.setName("results");
        //graphicLineChart.setTitle("results");
        isCalculated = true;
        infoText.setText(infoBegin + "Calculated.");
    }

    @FXML
    protected void onDrawGraphicButton() {
        resultTable.setVisible(!resultTable.isVisible());
        graphicLineChart.setVisible(!graphicLineChart.isVisible());
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
            readmeTextArea.setText(readmeRuText);
            isRuLang = !isRuLang;
        }
    }

    @FXML
    protected void onEnLangButton() {
        if (isRuLang) {
            readmeTextArea.setText(readmeEnText);
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
            FileOutputStream out = null;
            try {
                file.createNewFile();
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                infoText.setText(infoBegin + e.getMessage());
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
            row.getCell(0).setCellValue(creditAmountVal);
            row.getCell(1).setCellValue(creditTermVal);
            row.getCell(2).setCellValue(interestRateVal);
            row.getCell(3).setCellValue(paymentDateVal);
            row.getCell(4).setCellValue(dayofTheContractVal);
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
            infoText.setText(infoBegin + "File saved successful.");
        }
    }
}