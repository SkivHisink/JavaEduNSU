package org.lab.labwork1;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class InitParamReader extends InitParamBase {
    private boolean isRead = false;

    public boolean Read(File file, String info) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file.getPath());
        } catch (FileNotFoundException e) {
            info = "File problems. Error:" + e.getMessage();
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
                info = "Wrong extension of file.";
                return false;
            }
        } catch (Exception e) {
            info = "Problems with file. Error:" + e.getMessage();
            return false;
        }
        Sheet sheet = workBook.getSheet(workBook.getSheetName(0));
        Row row = sheet.getRow(1);
        if (row == null) {
            info = "Init data is incorrect.";
            return false;
        }
        if (row.getCell(4) == null) {
            info = "Init data is incorrect.";
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
            info = "Cells with initialize parameters have wrong value. " +
                    "Please check \"Help\" to fix all problems.";
        }
        if (paymentDate > 28) {
            info = "Payment date must be less or equal to 28.";
            return false;
        }
        info = "File read successful.";
        return true;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
