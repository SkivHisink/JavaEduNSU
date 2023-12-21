package org.lab.exlabwork1.Model;

import org.lab.exlabwork1.Utility.TableData;
import org.lab.exlabwork1.Utility.Utility;

public final class DifferentPayment extends PaymentBase {
    private double generalFee;

    public DifferentPayment(double creditAmount_, int creditTerm_,
                            double interestRate_, int paymentDate_,
                            String dayOfTheContract_) {
        super(creditAmount_, creditTerm_, interestRate_, paymentDate_, dayOfTheContract_);
        generalFee = Utility.bankingRound(creditAmount_ / creditTerm_) ;
    }

    @Override
    public String getPaymentType() {
        return "Дифференциальный платёж";
    }


    // Рассчёт дифференциального платежа для n-го месяца
    @Override
    public TableData getNotFirstMonthFee(int monthNumber, String info){
        super.getNotFirstMonthFee(monthNumber, info);
        TableData result = new TableData();
        solveDateProblem(result, monthNumber);
        result.setSumOfFee(generalFee);
        leftToPay -= generalFee;
        result.setFeeLeft(Utility.bankingRound(leftToPay));
        solvePercentProblem(result, monthNumber);
        result.setGeneralPaymentSize(Utility.bankingRound(
                result.getSumOfFee() + result.getPercentSum()));
        return result;
    }
}

