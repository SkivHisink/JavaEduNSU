package org.lab.labwork1;

import java.time.Month;
import java.time.YearMonth;

public class AnnuityPayment extends PaymentBase {
    protected double annuityCoef;
    protected double annuityFee;

    public AnnuityPayment(double creditAmount_, double creditTerm_,
                          double interestRate_, String paymentDate_,
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
    public double getFirstMonthFee() {
        var tempStr = dayOfTheContract.split(".");
        int beginDate = Integer.parseInt(tempStr[0]);
        int endDate = Integer.parseInt(paymentDate);
        if (beginDate <= endDate) {
            return monthlyInterestRate / 30 * (endDate - beginDate + 1) * creditAmount;
        }
        int beginMonth = Integer.parseInt(tempStr[1]);
        int beginYear = Integer.parseInt(tempStr[2]);
        if (beginMonth == 12) {
            beginMonth = 1;
            beginYear += 1;
        }
        YearMonth yearMonthObject = YearMonth.of(beginYear, beginMonth);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        return monthlyInterestRate / 30 * (daysInMonth - beginDate + 1 + endDate) * creditAmount;
    }

    public double getNotFirstMonthFee(int monthNumber) throws Exception {
        if (monthNumber > creditTerm) {
            throw new Exception("monthNumber has wrong value");
        }
        return annuityFee;
    }

    private AnnuityPayment() {
    }
}
