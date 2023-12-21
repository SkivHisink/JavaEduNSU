package org.lab.exlabwork1.Utility;

import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.lab.exlabwork1.Model.PaymentBase;

import java.io.File;
import java.io.FileOutputStream;

public class ExcelPaymentWriter {
    private FileOutputStream saveOutput;

    public boolean createOrOpenFileStream(File file, String info) {
        try {
            file.createNewFile();
            saveOutput = new FileOutputStream(file);
        } catch (Exception e) {
            info = e.getMessage();
            return false;
        }
        return true;
    }

    // Основной метод для сохранения в Excel
    public boolean saveData(ObservableList data, PaymentBase initData, String info) {
        XSSFWorkbook myWorkBook = new XSSFWorkbook();
        myWorkBook.createSheet("Результат");
        Sheet resultSheet = myWorkBook.getSheet(myWorkBook.getSheetName(0));
        for (int i = 0; i < 4; ++i) {
            resultSheet.createRow(i);
        }
        Row row = resultSheet.getRow(0);
        for (int i = 0; i < 6; ++i) {
            row.createCell(i);
        }
        // Первая строка сохранённого файла. Указывает что в каждой строке.
        row.getCell(0).setCellValue("Сумма кредита");
        row.getCell(1).setCellValue("Срок кредита");
        row.getCell(2).setCellValue("Процентная ставка");
        row.getCell(3).setCellValue("Дата платежа");
        row.getCell(4).setCellValue("Кредит предоставляется заемщику");
        row.getCell(5).setCellValue("Тип расчета");
        row = resultSheet.getRow(1);
        for (int i = 0; i < 6; ++i) {
            row.createCell(i);
        }
        try {
            row.getCell(0).setCellValue(initData.getCreditAmountVal());
            row.getCell(1).setCellValue(initData.getCreditTermVal());
            row.getCell(2).setCellValue(initData.getInterestRateVal());
            row.getCell(3).setCellValue(initData.getPaymentDateVal());
            row.getCell(4).setCellValue(initData.getDayofTheContractVal());
            row.getCell(5).setCellValue(initData.getPaymentType());
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
            for (int i = 0; i < data.size(); ++i) {
                resultSheet.createRow(i + 4);
                row = resultSheet.getRow(resultSheet.getLastRowNum());
                for (int j = 0; j < 7; ++j) {
                    row.createCell(j);
                }
                var item = (TableData) data.get(i);
                row.getCell(0).setCellValue(item.getN());
                row.getCell(1).setCellValue(item.getDayOfUsing());
                row.getCell(2).setCellValue(item.getPaymentDateVal());
                row.getCell(3).setCellValue(item.getGeneralPaymentSize());
                row.getCell(4).setCellValue(item.getPercentSum());
                row.getCell(5).setCellValue(item.getSumOfFee());
                row.getCell(6).setCellValue(item.getFeeLeft());
            }
            myWorkBook.write(saveOutput);
            saveOutput.close();
        } catch (Exception e) {
            info = "Проблемы с сохранением. Ошибка:" + e.getMessage();
            return false;
        }
        return true;
    }
}
