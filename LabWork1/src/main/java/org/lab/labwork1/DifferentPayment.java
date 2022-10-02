package org.lab.labwork1;

import java.time.Year;
import java.time.YearMonth;

public final class DifferentPayment extends LabTaskPaymentBase {
    private double generalFee;

    public DifferentPayment(double creditAmount_, int creditTerm_,
                            double interestRate_, int paymentDate_,
                            String dayOfTheContract_) {
        super(creditAmount_, creditTerm_, interestRate_, paymentDate_, dayOfTheContract_);
        generalFee = Utility.bankingRound(creditAmount_ / creditTerm_) ;
    }

    @Override
    public String getPaymentType() {
        return "different payment";
    }


    @Override
    public DataForTable getNotFirstMonthFee(int monthNumber, String info){
        super.getNotFirstMonthFee(monthNumber, info);
        DataForTable result = new DataForTable();
        solveDateProblem(result, monthNumber); //1, 2
        result.setSumOfFee(generalFee); // 5
        leftToPay -= generalFee;
        result.setFeeLeft(Utility.bankingRound(leftToPay)); //6 // rounding
        solvePercentProblem(result, monthNumber); //4
        result.setGeneralPaymentSize(Utility.bankingRound(
                result.getSumOfFee() + result.getPercentSum())); //3
        return result;
    }
}

