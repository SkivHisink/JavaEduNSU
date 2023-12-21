package org.lab.exlabwork1.Model;

import org.lab.exlabwork1.Utility.InitialParams;
import org.lab.exlabwork1.Utility.TableData;
import org.lab.exlabwork1.Utility.Utility;

import java.time.Year;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

public abstract class PaymentBase extends InitialParams {

    protected double monthlyInterestRate;
    protected double leftToPay;

    public PaymentBase(double creditAmount_, int creditTerm_,
                       double interestRate_, int paymentDate_,
                       String dayOfTheContract_) {
        creditAmount = creditAmount_;
        creditTerm = creditTerm_;
        interestRate = interestRate_;
        paymentDate = paymentDate_;
        dayOfTheContract = dayOfTheContract_;
        // Interest rate = процентная ставка. Поскольку процентная ставка указана на год и процентах,
        // то делим на колво месяцев и делим на 100 чтобы получить месячный коэффициент
        monthlyInterestRate = interestRate / 12 / 100;
        var tempVar = Math.pow(1 + monthlyInterestRate, creditTerm);
    }

    public String getPaymentType() {
        return null;
    }

    public TableData getNotFirstMonthFee(int monthNumber, String info) {
        if (monthNumber > creditTerm) {
            info = "Номер месяца имеет некорректное значение.";
            return null;
        }
        return new TableData();
    }

    // Установить даты для определённого месяца
    protected void solveDateProblem(TableData result, int monthNumber) {
        var tempStr = dayOfTheContract.split("\\.");
        int beginMonth = Integer.parseInt(tempStr[1]) + monthNumber;
        int beginYear = Integer.parseInt(tempStr[2]);
        if (beginMonth > 12) {
            beginYear += beginMonth / 12;
            beginMonth = beginMonth % 12;
            if (beginMonth == 0) {
                beginMonth = 12;
                beginYear -= 1;
            }
        }
        result.setPaymentDateVal(paymentDate + "." + Utility.monthFormatFixer(beginMonth) + "." + beginYear);
        result.setDayOfUsing(Utility.getDaysBetweenTwoDates(dayOfTheContract,
                paymentDate + "." + Utility.monthFormatFixer(beginMonth) +
                        "." + beginYear));
    }

    // Установить проценты для определённого месяца
    protected void solvePercentProblem(TableData result, int monthNumber) {
        // Получаем начальную дату
        var splittedString = dayOfTheContract.split("\\.");
        int startMonth = Integer.parseInt(splittedString[1]) + monthNumber - 1;
        int startYear = Integer.parseInt(splittedString[2]);
        // И конечную, но в неверном формате
        int endMonth = Integer.parseInt(splittedString[1]) + monthNumber;
        int endYear = Integer.parseInt(splittedString[2]);
        // Правим начальные год/месяц
        if (startMonth > 12) {
            startYear += startMonth / 12;
            startMonth = startMonth % 12;
            if (startMonth == 0) {
                startMonth = 12;
                startYear -= 1;
            }
        }
        // Правим конечные год/месяц
        if (endMonth > 12) {
            endYear += endMonth / 12;
            endMonth = endMonth % 12;
            if (endMonth == 0) {
                endMonth = 12;
                endYear -= 1;
            }
        }
        // Получаем количество дней в последнем году
        int endNumOfDays = Year.of(endYear).length();
        // и количество дней в первом году
        int startNumOfDays = Year.of(startYear).length();
        // Получаем количество дней между началом и концом
        int daysBetween = Utility.getDaysBetweenTwoDates(
                paymentDate + "." + Utility.monthFormatFixer(startMonth) + "." + startYear,
                paymentDate + "." + Utility.monthFormatFixer(endMonth) + "." + endYear
        );
        if (startYear == endYear) {
            result.setPercentSum(Utility.
                    bankingRound(interestRate / endNumOfDays / 100 * daysBetween * leftToPay));
        } else {
            YearMonth yearMonthObject = YearMonth.of(startYear, startMonth);
            int beginMonthDays = yearMonthObject.lengthOfMonth();
            result.setPercentSum(Utility.
                    bankingRound(
                            interestRate / startNumOfDays / 100 * (beginMonthDays - paymentDate) * leftToPay +
                                    interestRate / endNumOfDays / 100 * paymentDate * leftToPay));
        }
    }

    // Получить результат платежа в первый месяц
    public TableData getFirstMonthFee() {
        TableData firstMotnthResult = new TableData();
        var tempStr = dayOfTheContract.split("\\.");
        int beginDate = Integer.parseInt(tempStr[0]);
        int endDate = paymentDate;
        int startMonth = Integer.parseInt(tempStr[1]);
        int startYear = Integer.parseInt(tempStr[2]);
        YearMonth yearMonthObject = YearMonth.of(startYear, startMonth);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        double tempPercent;
        firstMotnthResult.setSumOfFee(0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int numOfDays = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
        if (beginDate <= endDate) {
            tempPercent = interestRate / 100 / numOfDays * (endDate - beginDate);
            // Мы не используем деньги в первый день
            firstMotnthResult.setPercentSum(tempPercent * creditAmount);
            firstMotnthResult.setDayOfUsing(endDate - beginDate + 1);
        } else {
            if (startMonth == 12) {
                startMonth = 1;
                startYear += 1;
            }
            tempPercent = interestRate / 100 / numOfDays * (daysInMonth - beginDate + 1 + endDate);
            firstMotnthResult.setPercentSum(tempPercent * creditAmount);
            firstMotnthResult.setDayOfUsing(daysInMonth - beginDate + 1 + endDate);
            startMonth += 1;
        }
        firstMotnthResult.setPercentSum(Utility.bankingRound(firstMotnthResult.getPercentSum()));
        firstMotnthResult.setPaymentDateVal(paymentDate + "." +
                Utility.monthFormatFixer(startMonth) + "." +
                startYear);
        firstMotnthResult.setFeeLeft(this.creditAmount);
        firstMotnthResult.setGeneralPaymentSize(
                Utility.bankingRound(firstMotnthResult.getPercentSum() +
                        firstMotnthResult.getSumOfFee()));
        leftToPay = this.creditAmount;
        return firstMotnthResult;
    }
}
