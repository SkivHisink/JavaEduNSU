package org.lab.labwork1;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AnnuityPayment extends PaymentBase {
    protected double annuityCoef;
    protected double annuityFee;

    public AnnuityPayment(double creditAmount_, double creditTerm_,
                          double interestRate_, int paymentDate_,
                          String dayOfTheContract_) {
        creditAmount = creditAmount_;
        creditTerm = creditTerm_;
        interestRate = interestRate_;
        paymentDate = paymentDate_;
        dayOfTheContract = dayOfTheContract_;
        monthlyInterestRate = interestRate / 12 / 100;
        var tempVar = Math.pow(1 + monthlyInterestRate, creditTerm);
        annuityCoef = monthlyInterestRate * tempVar / (tempVar - 1);
        annuityFee = annuityCoef * creditAmount;
        annuityFee = (double)(Math.round( annuityFee * 100))/100;
    }

    @Override
    public double getBasePayment() {
        return 0;
    }

    @Override
    public double getBalanceOwed() {
        return 0;
    }

    @Override
    public double getPercent() {
        return 0;
    }

    @Override
    public double getPayment() {
        return 0;
    }

    @Override
    public String getPaymentType() {
        return "AnnuityPayment";
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
        result.setPaymentDateVal(paymentDate + "." + (beginMonth) + "." + beginYear); // 2
        result.setFeeLeft(this.creditAmount); // 6
        result.setGeneralPaymentSize(result.getPercentSum() + result.getSumOfFee()); // 3
        leftToPay = this.creditAmount;
        return result;
    }

    public DataForTable getNotFirstMonthFee(int monthNumber) throws Exception {
        super.getNotFirstMonthFee(monthNumber);
        DataForTable result = new DataForTable();
        var tempStr = dayOfTheContract.split("\\.");
        int beginMonth = Integer.parseInt(tempStr[1]) + monthNumber + 1;
        int beginYear = Integer.parseInt(tempStr[2]);
        if (beginMonth > 12) {
            beginYear += beginMonth / 12;
            beginMonth = beginMonth % 12;
            if (beginMonth == 0)
                beginMonth = 12;
        }
        result.setPaymentDateVal(paymentDate + "." + (beginMonth) + "." + beginYear); // 2
        result.setDayOfUsing(Utility.getDaysBetweenTwoDates(dayOfTheContract,
                paymentDate + "." + ((beginMonth>=10)?beginMonth:("0"+beginMonth)) +
                        "." + beginYear)); // 1
        result.setPercentSum(monthlyInterestRate * leftToPay); // 4
        result.setGeneralPaymentSize(annuityFee); // 3
        result.setSumOfFee(result.getGeneralPaymentSize() - result.getPercentSum()); // 5
        leftToPay -= result.getSumOfFee();
        result.setFeeLeft(leftToPay); // 6
        return result;
    }

    private AnnuityPayment() {
    }
}
