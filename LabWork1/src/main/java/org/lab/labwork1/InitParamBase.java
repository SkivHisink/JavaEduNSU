package org.lab.labwork1;

public abstract class InitParamBase {
    protected String dayOfTheContract;
    protected int paymentDate;
    protected double interestRate;
    protected int creditTerm;
    protected double creditAmount;

    public double getCreditAmountVal() {
        return creditAmount;
    }

    public double getInterestRateVal() {
        return interestRate;
    }

    public int getCreditTermVal() {
        return creditTerm;
    }

    public int getPaymentDateVal() {
        return paymentDate;
    }

    public String getDayofTheContractVal() {
        return dayOfTheContract;
    }

}
