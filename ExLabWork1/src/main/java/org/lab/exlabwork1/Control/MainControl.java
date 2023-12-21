package org.lab.exlabwork1.Control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.lab.exlabwork1.Model.PaymentBase;
import org.lab.exlabwork1.Utility.InitialParamsReader;
import org.lab.exlabwork1.Utility.ExcelPaymentWriter;
import org.lab.exlabwork1.Utility.TableData;
import org.lab.exlabwork1.Application;
import org.lab.exlabwork1.Model.AnnuityPayment;
import org.lab.exlabwork1.Model.DifferentPayment;
import org.lab.exlabwork1.Utility.Utility;
import org.lab.exlabwork1.View.MainViewer;
import org.lab.exlabwork1.View.PlotViewer;
import org.lab.exlabwork1.View.TableViewer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MainControl {
    // Model
    private PaymentBase paymentMethod = null;
    // View
    private PlotViewer plotViewer = null;
    private Stage plotStage = null;
    private MainViewer mainViewer = null;
    private Stage mainStage = null;
    private TableViewer tableViewer = null;
    private Stage tableStage = null;
    // Data
    private InitialParamsReader reader;
    private List<String> paymentTypesList;
    private ObservableList<TableData> dataObservableList;
    private List<String> xyDataTypeNames;
    private boolean isCalculated = false;
    private List<Function<TableData, String>> getValueFunctionList;
    private ObservableList<String> dataTypeObservableList;

    public MainControl() {
        reader = new InitialParamsReader();
        dataObservableList = FXCollections.observableArrayList();
        getValueFunctionList = new ArrayList<>();
        getValueFunctionList.add(TableData::getStaticN);
        getValueFunctionList.add(TableData::getStaticDayOfUsing);
        getValueFunctionList.add(TableData::getStaticPaymentDateVal);
        getValueFunctionList.add(TableData::getStaticGeneralPaymentSize);
        getValueFunctionList.add(TableData::getStaticPercentSum);
        getValueFunctionList.add(TableData::getStaticSumOfFee);
        getValueFunctionList.add(TableData::getStaticFeeLeft);
        xyDataTypeNames = new ArrayList<String>();
        xyDataTypeNames.add("N");
        xyDataTypeNames.add("Кол-во дней пользования заемными средствами");
        xyDataTypeNames.add("Дата платежа");
        xyDataTypeNames.add("Общая сумма платежа");
        xyDataTypeNames.add("Сумма процентов");
        xyDataTypeNames.add("Сумма погашаемого долга");
        xyDataTypeNames.add("Остаток задолжности");
        dataTypeObservableList = FXCollections.observableList(xyDataTypeNames);
        paymentTypesList = new ArrayList<String>();
        paymentTypesList.add("Аннуитетный платеж");
        paymentTypesList.add("Дифференцированный платеж");
    }

    // stage - главное окно
    public void run(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainForm.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            mainViewer = fxmlLoader.getController();
            mainViewer.setParent(this);
            stage.setResizable(false);
            stage.setTitle("Банковский калькулятор");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Установка метода платежа
    public void setPaymentType(MainViewer viewer) throws IllegalAccessException {
        if (viewer != this.mainViewer) {
            throw new IllegalAccessException();
        }
        // установка аннуитетного метода платежа
        if (viewer.getCalculationTypeValue() == paymentTypesList.get(0)) {
            paymentMethod = new AnnuityPayment(
                    reader.getCreditAmountVal(),
                    reader.getCreditTermVal(),
                    reader.getInterestRateVal(),
                    reader.getPaymentDateVal(),
                    reader.getDayofTheContractVal());
        }
        // установка дифференциального платежа
        else if (viewer.getCalculationTypeValue() == paymentTypesList.get(1)) {
            paymentMethod = new DifferentPayment(
                    reader.getCreditAmountVal(),
                    reader.getCreditTermVal(),
                    reader.getInterestRateVal(),
                    reader.getPaymentDateVal(),
                    reader.getDayofTheContractVal());
        } else {
            paymentMethod = null;
            viewer.sendToLog("Проблема с типом оплаты.");
        }
    }

    // Рассчёт платежей
    public void calculatePayment(MainViewer viewer) throws IllegalAccessException {
        isCalculated = false;
        if (viewer != this.mainViewer) {
            throw new IllegalAccessException();
        }
        setPaymentType(viewer);
        if (paymentMethod == null) {
            isCalculated = false;
            return;
        }
        try {
            dataObservableList = FXCollections.observableArrayList();
            TableData result = null;
            result = paymentMethod.getFirstMonthFee();
            result.setN(0);
            dataObservableList.add(result);
            int calcPerSum = 0;
            String info = "";
            for (int i = 1; i < reader.getCreditTermVal() + 1; ++i) {
                result = paymentMethod.getNotFirstMonthFee(i, info);
                if (result == null) {
                    break;
                }
                result.setN(i);
                dataObservableList.add(result);
                calcPerSum += result.getPercentSum();
            }
            if (result == null) {
                viewer.sendToLog(info);
                isCalculated = false;
                return;
            }
            result = dataObservableList.get(dataObservableList.size() - 1);
            result.setGeneralPaymentSize(Utility.bankingRound(
                    result.getGeneralPaymentSize() + result.getFeeLeft()));
            result.setFeeLeft(0);
            result.setSumOfFee(Utility.bankingRound(
                    result.getGeneralPaymentSize() - result.getPercentSum()));
            viewer.setPercentPaySum(Utility.PercentPaySumBeginHeader + calcPerSum);
            dataObservableList.set(dataObservableList.size() - 1, result);
            if (tableStage != null && tableStage.isShowing()){
                tableViewer.getResultTable().setItems(dataObservableList);
            }
        } catch (Exception e) {
            sendToLog("Проблема в расчетах. Ошибка:" + e.getMessage());
            isCalculated = false;
            return;
        }
        isCalculated = true;
        return;
    }

    // Открыть окно для графиков
    public void openPlotWindow(MainViewer viewer) throws IllegalAccessException {
        if (viewer != this.mainViewer) {
            throw new IllegalAccessException();
        }
        try {
            if (plotStage == null || !plotStage.isShowing()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(Application.class.getResource("plot.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                plotViewer = fxmlLoader.getController();
                plotViewer.setParent(this);
                plotViewer.CheckSolutionState();
                var data = dataTypeObservableList;
                plotViewer.getxDataType().setItems(data);
                plotViewer.getxDataType().setValue(data.get(0));
                plotViewer.getyDataType().setItems(data);
                plotViewer.getyDataType().setValue(data.get(6));
                plotStage = new Stage();
                plotStage.setTitle("График");
                plotStage.setScene(scene);
                plotStage.setResizable(false);
                plotStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Открыть окно для данных
    public void openTableWindow(MainViewer viewer) throws IllegalAccessException {
        if (viewer != this.mainViewer) {
            throw new IllegalAccessException();
        }
        try {
            if (tableStage == null || !tableStage.isShowing()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(Application.class.getResource("table.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                tableViewer = fxmlLoader.getController();
                tableViewer.setParent(this);
                tableViewer.init();
                tableViewer.getResultTable().setItems(dataObservableList);
                tableStage = new Stage();
                tableStage.setTitle("График");
                tableStage.setScene(scene);
                tableStage.setResizable(false);
                tableStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Геттер для данных. Данные может получить только вью плот привязанный к данному контролу
    public List<String> getData(PlotViewer viewer, String type) throws IllegalAccessException {
        if (viewer != this.plotViewer) {
            throw new IllegalAccessException();
        }
        return getData(type);
    }

    // Получить флаг о том прошёл ли рассчёт или нет
    public boolean getIsCalculated() {
        return isCalculated;
    }

    // Отправить информацию в логгер
    public void sendToLog(String info) {
        mainViewer.sendToLog(info);
    }

    // Вызов кнопки добавления снаружи.
    public void insideAddSummon() {
        if (plotViewer != null) {
            plotViewer.insideAddSummon();
        }
    }

    // Получение читальщика с параметрами
    public InitialParamsReader getReader(){
        return reader;
    }

    public List<String> getData(String type) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (type == xyDataTypeNames.get(i)) {
                for (int j = 0; j < getReader().getCreditTermVal() + 1; ++j) {
                    TableData tmp = dataObservableList.get(j);
                    result.add(getValueFunctionList.get(i).apply(tmp));
                }
            }
        }
        return result;
    }

    // Сохранение в Excel
    public void saveInExcel()
    {
        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Сохранить файл");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Excel files (*.xlsx)",
                "*.xlsx");
        saveFileChooser.getExtensionFilters().add(extFilter);
        var info = "";
        if (!mainViewer.getIsOpenDialog()) {
            mainViewer.setIsOpenDialog(true);
            File file = saveFileChooser.showSaveDialog(new Stage());
            if (file != null) {
                ExcelPaymentWriter saver = new ExcelPaymentWriter();
                if (!saver.createOrOpenFileStream(file, info)) {
                    sendToLog(info);
                    mainViewer.setIsOpenDialog(false);
                    return;
                }
                if (!saver.saveData(dataObservableList, paymentMethod, info)) {
                    sendToLog(info);
                    mainViewer.setIsOpenDialog(false);
                    return;
                }
                sendToLog("Файл успешно сохранен.");
            }
            mainViewer.setIsOpenDialog(false);
        }
    }

    // Получение списка типов доступных платежей
    public List<String> getPaymentTypesList(){
        return paymentTypesList;
    }
}
