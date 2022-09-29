package org.lab.labwork1;
public final class AnnuityPayment extends LabTaskPaymentBase {
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
