package org.lab.labwork1;

public abstract class PaymentBase
{
    protected double creditAmount;
    protected double creditTerm;
    protected double interestRate;
    protected String paymentDate;
    protected String dayOfTheContract;
    protected double monthlyInterestRate;
    public abstract double getBasePayment();
    public abstract double getBalanceOwed();
    public abstract double getPercent();
    public abstract double getPayment();
    public abstract String getPaymentType();
    public abstract double getFirstMonthFee();
}
