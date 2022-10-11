package org.lab.labwork1;

public abstract class PaymentBase extends InitParamBase
{
    protected double monthlyInterestRate;
    protected double leftToPay;
    public abstract String getPaymentType();
    public abstract DataForTable getFirstMonthFee();

    public DataForTable getNotFirstMonthFee(int monthNumber, String info) {
        if (monthNumber > creditTerm) {
            info = "monthNumber has wrong value";
            return null;
        }
        return new DataForTable();
    }
}
