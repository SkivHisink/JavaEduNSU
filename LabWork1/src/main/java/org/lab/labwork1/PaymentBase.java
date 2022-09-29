package org.lab.labwork1;

public abstract class PaymentBase
{
    protected double creditAmount;
    protected double creditTerm;
    protected double interestRate;
    protected int paymentDate;
    protected String dayOfTheContract;
    protected double monthlyInterestRate;
    protected double leftToPay;
    public abstract String getPaymentType();
    public abstract DataForTable getFirstMonthFee();

    public DataForTable getNotFirstMonthFee(int monthNumber) throws Exception {
        if (monthNumber > creditTerm) {
            throw new Exception("monthNumber has wrong value");
        }
        return null;
    }
}
