package org.lab.labwork1;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class initParamReader  extends  InitParamBase{
    private boolean isRead = false;
    public boolean Read(Workbook workBook, String info) {
        Sheet sheet = workBook.getSheet(workBook.getSheetName(0));
        Row row = sheet.getRow(1);
        if (row == null) {
            info = Utility.InfoBegin + "Init data is incorrect.";
            return false;
        }
        if (row.getCell(4) == null) {
            info = Utility.InfoBegin + "Init data is incorrect.";
            return false;
        }
        // getting data from file
        creditAmount = row.getCell(0).getNumericCellValue();
        creditTerm = (int) row.getCell(1).getNumericCellValue();
        interestRate = row.getCell(2).getNumericCellValue();
        paymentDate = (int) row.getCell(3).getNumericCellValue();
        dayOfTheContract = row.getCell(4).getStringCellValue();
        if (paymentDate > 28) {
            info = Utility.InfoBegin + "Payment date must be less or equal to 28.";
            return false;
        }
        info = Utility.InfoBegin + "File read successful.";
        return true;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
