package org.lab.labwork1;

import java.time.Year;
import java.time.YearMonth;

public final class AnnuityPayment extends LabTaskPaymentBase {
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
        return "Annuity payment";
    }

    @Override
    public DataForTable getNotFirstMonthFee(int monthNumber, String info) {
        DataForTable result = super.getNotFirstMonthFee(monthNumber, info);
        if (result == null) {
            return null;
        }
        solveDateProblem(result, monthNumber);
        result.setGeneralPaymentSize(annuityFee); // 3
        result.setPercentSum(monthlyInterestRate * leftToPay); // 4
        //solvePercentProblem(result, monthNumber); //it doesn't work correctly
        result.setSumOfFee(Utility.
               bankingRound(result.getGeneralPaymentSize() -
                        result.getPercentSum())); // 5
        leftToPay -= result.getSumOfFee();
        result.setFeeLeft(Utility.bankingRound(leftToPay)); // 6
        return result;
    }
}

//day annuity coef
//var tempStr = dayOfTheContract.split("\\.");
//int beginMonth = Integer.parseInt(tempStr[1]);
//int beginYear = Integer.parseInt(tempStr[2]);
//int endMonth = Integer.parseInt(tempStr[1]) + creditTerm_;
// endYear = Integer.parseInt(tempStr[2]);
//if (endMonth > 12) {
//    endYear += endMonth / 12;
//    endMonth = endMonth % 12;
//    if (endMonth == 0) {
//        endMonth = 12;
//        endYear -=1;
//     }
//}
//var tempVar = Math.pow(1 + monthlyInterestRate/30, Utility.getDaysBetweenTwoDates(
//        paymentDate + "." + Utility.monthFormatFixer(beginMonth) + "." + beginYear,
//        paymentDate + "." + Utility.monthFormatFixer(endMonth) + "." + endYear
//));