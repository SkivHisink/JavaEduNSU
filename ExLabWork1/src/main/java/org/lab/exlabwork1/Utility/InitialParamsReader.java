package org.lab.exlabwork1.Utility;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class InitialParamsReader extends InitialParams {

    public boolean Read(File file, String info) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file.getPath());
        } catch (FileNotFoundException e) {
            info = "Проблемы с файлом. Ошибка:" + e.getMessage();
            return false;
        }
        String extension = "";
        int i = file.getPath().lastIndexOf('.');
        if (i > 0) {
            extension = file.getPath().substring(i + 1);
        }
        Workbook workBook = null;
        try {

            if (extension.equals("xlsx")) {
                workBook = new XSSFWorkbook(fileInputStream);
            } else if (extension.equals("xls")) {
                workBook = new HSSFWorkbook(fileInputStream);
            } else {
                info = "Некорректное расширение файла.";
                return false;
            }
        } catch (Exception e) {
            info = "Проблема с файлом. Ошибка:" + e.getMessage();
            return false;
        }
        Sheet sheet = workBook.getSheet(workBook.getSheetName(0));
        Row row = sheet.getRow(1);
        if (row == null) {
            info = "Входящий файл некорректный.";
            return false;
        }
        if (row.getCell(4) == null) {
            info = "Входящий файл некорректный.";
            return false;
        }
        // getting data from file
        try {
            creditAmount = row.getCell(0).getNumericCellValue();
            creditTerm = (int) row.getCell(1).getNumericCellValue();
            interestRate = row.getCell(2).getNumericCellValue();
            paymentDate = (int) row.getCell(3).getNumericCellValue();
            dayOfTheContract = row.getCell(4).getStringCellValue();
            fileInputStream.close();
        } catch (Exception e) {
            info = "Одна из ячеек с параметрами инициализации имеет некоректноее значение.";
            return false;
        }
        if (paymentDate > 28) {
            info = "Дата платежа должна быть меньше равна 28 числу.";
            return false;
        }
        info = "Файл успешно прочитан.";
        if(creditAmount<=0){
            info = "Некорректное значение суммы кредита! Исправьте значение на положительное";
            return false;
        }
        if(creditTerm <=0){
            info = "Некорректное значение срока кредита! Исправьте значение на положительное";
            return false;
        }
        if(paymentDate <1 || paymentDate >31){
            info = "Некорректное значение даты платежа! Исправьте значение на корректное";
            return false;
        }
        return true;
    }

}
