package org.lab.labwork1;

import java.time.YearMonth;

public class DifferentPayment extends LabTaskPaymentBase {
    private double generalFee;

    public DifferentPayment(double creditAmount_, double creditTerm_,
                            double interestRate_, int paymentDate_,
                            String dayOfTheContract_) {
        super(creditAmount_, creditTerm_, interestRate_, paymentDate_, dayOfTheContract_);
        generalFee = Utility.bankingRound(creditAmount_ / creditTerm_) ;
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
        return "different payment";
    }


    @Override
    public DataForTable getNotFirstMonthFee(int monthNumber) throws Exception {
        super.getNotFirstMonthFee(monthNumber);
        DataForTable result = new DataForTable();
        solveDateProblem(result, monthNumber);
        result.setSumOfFee(generalFee); // 5
        leftToPay -= generalFee;
        result.setFeeLeft(leftToPay); //6
        result.setPercentSum(leftToPay * interestRate / 12); //4
        result.setGeneralPaymentSize(Utility.bankingRound(
                result.getSumOfFee() + result.getPercentSum())); //3
        return result;
    }
}

