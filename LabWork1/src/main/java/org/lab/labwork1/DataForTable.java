package org.lab.labwork1;

public class DataForTable {
    private int N; // 0
    private int dayOfUsing; // 1
    private  String paymentDateVal; // 2
    private double generalPaymentSize; // 3
    private   double percentSum; // 4
    private double sumOfFee; // 5
    private  double feeLeft; // 6

    public double getFeeLeft() {
        return feeLeft;
    }

    public double getGeneralPaymentSize() {
        return generalPaymentSize;
    }

    public double getPercentSum() {
        return percentSum;
    }

    public double getSumOfFee() {
        return sumOfFee;
    }

    public int getN() {
        return N;
    }

    public int getDayOfUsing() {
        return dayOfUsing;
    }

    public String getPaymentDateVal() {
        return paymentDateVal;
    }

    public void setDayOfUsing(int dayOfUsing) {
        this.dayOfUsing = dayOfUsing;
    }

    public void setFeeLeft(double feeLeft) {
        this.feeLeft = feeLeft;
    }

    public void setN(int n) {
        N = n;
    }

    public void setGeneralPaymentSize(double generalPaymentSize) {
        this.generalPaymentSize = generalPaymentSize;
    }

    public void setPaymentDateVal(String paymentDateVal) {
        this.paymentDateVal = paymentDateVal;
    }

    public void setPercentSum(double percentSum) {
        this.percentSum = percentSum;
    }

    public void setSumOfFee(double sumOfFee) {
        this.sumOfFee = sumOfFee;
    }
}
