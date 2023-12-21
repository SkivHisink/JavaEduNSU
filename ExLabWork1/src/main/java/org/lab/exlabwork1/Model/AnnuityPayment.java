package org.lab.exlabwork1.Model;

import org.lab.exlabwork1.Utility.TableData;
import org.lab.exlabwork1.Utility.Utility;

public final class AnnuityPayment extends PaymentBase {
    protected double annuityCoef;
    protected double annuityFee;

    public AnnuityPayment(double creditAmount_, int creditTerm_,
                          double interestRate_, int paymentDate_,
                          String dayOfTheContract_) {
        super(creditAmount_, creditTerm_, interestRate_, paymentDate_, dayOfTheContract_);
        var tempVar = Math.pow(1 + monthlyInterestRate, creditTerm);
        annuityCoef = monthlyInterestRate * tempVar / (tempVar - 1.0);
        annuityFee = annuityCoef * creditAmount;
        annuityFee = (double) (Math.round(annuityFee * 100)) / 100;
    }

    @Override
    public String getPaymentType() {
        return "Аннуитетный платёж";
    }

    // Рассчёт аннуитетного платежа для n-го месяца
    @Override
    public TableData getNotFirstMonthFee(int monthNumber, String info) {
        TableData result = super.getNotFirstMonthFee(monthNumber, info);
        if (result == null) {
            return null;
        }
        solveDateProblem(result, monthNumber);
        result.setGeneralPaymentSize(annuityFee);
        result.setPercentSum(monthlyInterestRate * leftToPay);
        result.setSumOfFee(Utility.
               bankingRound(result.getGeneralPaymentSize() -
                        result.getPercentSum()));
        leftToPay -= result.getSumOfFee();
        result.setFeeLeft(Utility.bankingRound(leftToPay));
        return result;
    }
}