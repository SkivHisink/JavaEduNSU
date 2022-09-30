package org.lab.labwork1;

import java.time.YearMonth;

public class LabTaskPaymentBase extends  PaymentBase{

    public LabTaskPaymentBase(double creditAmount_, int creditTerm_,
                          double interestRate_, int paymentDate_,
                          String dayOfTheContract_) {
        creditAmount = creditAmount_;
        creditTerm = creditTerm_;
        interestRate = interestRate_;
        paymentDate = paymentDate_;
        dayOfTheContract = dayOfTheContract_;
        monthlyInterestRate = interestRate / 12 / 100;
        var tempVar = Math.pow(1 + monthlyInterestRate, creditTerm);
    }

    @Override
    public String getPaymentType() {
        return null;
    }
    protected void solveDateProblem(DataForTable result, int monthNumber)
    {
        var tempStr = dayOfTheContract.split("\\.");
        int beginMonth = Integer.parseInt(tempStr[1]) + monthNumber;
        int beginYear = Integer.parseInt(tempStr[2]);
        if (beginMonth > 12) {
            beginYear += beginMonth / 12;
            beginMonth = beginMonth % 12;
            if (beginMonth == 0)
                beginMonth = 12;
        }
        result.setPaymentDateVal(paymentDate + "." + Utility.monthFormatFixer(beginMonth) + "." + beginYear); // 2
        result.setDayOfUsing(Utility.getDaysBetweenTwoDates(dayOfTheContract,
                paymentDate + "." + Utility.monthFormatFixer(beginMonth) +
                        "." + beginYear)); // 1
    }
    @Override
    public DataForTable getFirstMonthFee()
    {
        DataForTable result = new DataForTable();
        var tempStr = dayOfTheContract.split("\\.");
        int beginDate = Integer.parseInt(tempStr[0]);
        int endDate = paymentDate;
        int beginMonth = Integer.parseInt(tempStr[1]);
        int beginYear = Integer.parseInt(tempStr[2]);
        YearMonth yearMonthObject = YearMonth.of(beginYear, beginMonth);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        double tempPercent;
        result.setSumOfFee(0); // 5
        if (beginDate <= endDate) {
            tempPercent = monthlyInterestRate / 30 * (endDate - beginDate + 1);
            result.setPercentSum(tempPercent * creditAmount); // 4
            result.setDayOfUsing(endDate - beginDate + 1); // 1
        } else {
            if (beginMonth == 12) {
                beginMonth = 1;
                beginYear += 1;
            }
            tempPercent = monthlyInterestRate / 30 * (daysInMonth - beginDate + 1 + endDate);
            result.setPercentSum(tempPercent * creditAmount); // 4
            result.setDayOfUsing(daysInMonth - beginDate + 1 + endDate); // 1
            beginMonth += 1;
        }
        result.setPaymentDateVal(paymentDate + "." + Utility.monthFormatFixer(beginMonth) + "." + beginYear); // 2
        result.setFeeLeft(this.creditAmount); // 6
        result.setGeneralPaymentSize(Utility.bankingRound(result.getPercentSum() + result.getSumOfFee())); // 3
        leftToPay = this.creditAmount;
        return result;
    }
}
