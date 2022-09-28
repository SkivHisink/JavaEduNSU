package org.lab.labwork1;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AnnuityPayment extends LabTaskPaymentBase {
    protected double annuityCoef;
    protected double annuityFee;

    public AnnuityPayment(double creditAmount_, double creditTerm_,
                          double interestRate_, int paymentDate_,
                          String dayOfTheContract_) {
        super(creditAmount_, creditTerm_, interestRate_, paymentDate_, dayOfTheContract_);
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
        return "Annuity payment";
    }

    @Override
    public DataForTable getNotFirstMonthFee(int monthNumber) throws Exception {
        super.getNotFirstMonthFee(monthNumber);
        DataForTable result = new DataForTable();
        solveDateProblem(result, monthNumber);
        result.setGeneralPaymentSize(annuityFee); // 3
        result.setPercentSum(monthlyInterestRate * leftToPay); // 4
        result.setSumOfFee(result.getGeneralPaymentSize() - result.getPercentSum()); // 5
        leftToPay -= result.getSumOfFee();
        result.setFeeLeft(leftToPay); // 6
        return result;
    }
}
