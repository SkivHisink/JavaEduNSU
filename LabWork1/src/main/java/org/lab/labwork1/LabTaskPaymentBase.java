package org.lab.labwork1;

import java.time.Year;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

public class LabTaskPaymentBase extends PaymentBase {

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

    protected void solveDateProblem(DataForTable result, int monthNumber) {
        var tempStr = dayOfTheContract.split("\\.");
        int beginMonth = Integer.parseInt(tempStr[1]) + monthNumber;
        int beginYear = Integer.parseInt(tempStr[2]);
        if (beginMonth > 12) {
            beginYear += beginMonth / 12;
            beginMonth = beginMonth % 12;
            if (beginMonth == 0) {
                beginMonth = 12;
                beginYear -=1;
            }
        }
        result.setPaymentDateVal(paymentDate + "." + Utility.monthFormatFixer(beginMonth) + "." + beginYear); // 2
        result.setDayOfUsing(Utility.getDaysBetweenTwoDates(dayOfTheContract,
                paymentDate + "." + Utility.monthFormatFixer(beginMonth) +
                        "." + beginYear)); // 1
    }
    protected void solvePercentProblem(DataForTable result, int monthNumber){
        var tempStr = dayOfTheContract.split("\\.");
        int beginMonth = Integer.parseInt(tempStr[1]) + monthNumber - 1;
        int beginYear = Integer.parseInt(tempStr[2]);
        if (beginMonth > 12) {
            beginYear += beginMonth / 12;
            beginMonth = beginMonth % 12;
            if (beginMonth == 0) {
                beginMonth = 12;
                beginYear-=1;
            }
        }
        int endMonth = Integer.parseInt(tempStr[1]) + monthNumber;
        int endYear = Integer.parseInt(tempStr[2]);
        if (endMonth > 12) {
            endYear += endMonth / 12;
            endMonth = endMonth % 12;
            if (endMonth == 0) {
                endMonth = 12;
                endYear -=1;
            }
        }
        int endNumOfDays = Year.of(endYear).length(); //check endYear
        int beginNumOfDays = Year.of(beginYear).length();
        int daysBetween = Utility.getDaysBetweenTwoDates(
                paymentDate + "." + Utility.monthFormatFixer(beginMonth) + "." + beginYear,
                paymentDate + "." + Utility.monthFormatFixer(endMonth) + "." + endYear
        );
        if (beginYear == endYear) {
            result.setPercentSum(Utility.
                    bankingRound(interestRate / endNumOfDays / 100 * daysBetween * leftToPay)); // 4
        } else {
            YearMonth yearMonthObject = YearMonth.of(beginYear, beginMonth);
            int beginMonthDays = yearMonthObject.lengthOfMonth();
            result.setPercentSum(Utility.
                    bankingRound(
                            interestRate / beginNumOfDays / 100 * (beginMonthDays-paymentDate) * leftToPay +
                                    interestRate / endNumOfDays / 100 * paymentDate * leftToPay));
        }
    }
    @Override
    public DataForTable getFirstMonthFee() {
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
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int numOfDays = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
        if (beginDate <= endDate) {
            tempPercent = interestRate / 100 / numOfDays * (endDate - beginDate);
            // We don't use our money in first day
            result.setPercentSum(tempPercent * creditAmount); // 4
            result.setDayOfUsing(endDate - beginDate + 1); // 1
        } else {
            if (beginMonth == 12) {
                beginMonth = 1;
                beginYear += 1;
            }
            tempPercent = interestRate / 100 / numOfDays * (daysInMonth - beginDate + 1 + endDate);
            result.setPercentSum(tempPercent * creditAmount); // 4
            result.setDayOfUsing(daysInMonth - beginDate + 1 + endDate); // 1
            beginMonth += 1;
        }
        result.setPercentSum(Utility.bankingRound(result.getPercentSum()));
        result.setPaymentDateVal(paymentDate + "." + Utility.monthFormatFixer(beginMonth) + "." + beginYear); // 2
        result.setFeeLeft(this.creditAmount); // 6
        result.setGeneralPaymentSize(Utility.bankingRound(result.getPercentSum() + result.getSumOfFee())); // 3
        leftToPay = this.creditAmount;
        return result;
    }
}
