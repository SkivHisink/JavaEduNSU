package org.lab.exlabwork1.Utility;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utility {
    public static String CreditAmountHeader = "Сумма кредита:";
    public static String CreditTermHeader = "Срок кредита:";
    public static String InterestRateHeader = "Процентная ставка:";
    public static  String PercentPaySumBeginHeader = "Сумма процентов з/п:";
    public static  String PaymentDateHeader = "Дата платежа:";
    public static  String DayOfTheContractHeader = "Дата кредита:";
    public static String logHeader = "LOG:";

    // Получить количество дней между двумя днями
    public static int getDaysBetweenTwoDates(String beginDate, String endDate) {
        return getDaysBetweenTwoDates(beginDate, endDate, "dd.MM.yyyy");
    }

    // Получить количество дней между двумя днями
    public static int getDaysBetweenTwoDates(String beginDate, String endDate, String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        LocalDateTime date1 = LocalDate.parse(beginDate, dtf).atStartOfDay();
        LocalDateTime date2 = LocalDate.parse(endDate, dtf).atStartOfDay();
        return (int) Duration.between(date1, date2).toDays();
    }

    // Банковское округление
    public static double bankingRound(double val) {
        return (double) (Math.round(val * 100)) / 100;
    }

    // Для выдачи месяца в формате MM
    public static String monthFormatFixer(int month) {
        return (month >= 10) ? ("" + month) : ("0" + month);
    }
}
