package org.lab.labwork1;

public abstract class PaymentBase extends InitParamBase
{
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
