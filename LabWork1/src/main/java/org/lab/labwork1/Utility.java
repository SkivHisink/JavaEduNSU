package org.lab.labwork1;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utility {
    // А потом люди удивляются почему джава "хороший язык"
    // параметры по умолчание не работают, используем костыль, который передаёт формат "dd.MM.yyyy"
    public static int getDaysBetweenTwoDates(String beginDate, String endDate)
    {
        return getDaysBetweenTwoDates(beginDate, endDate, "dd.MM.yyyy");
    }

    public static int getDaysBetweenTwoDates(String beginDate, String endDate, String format)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        LocalDateTime date1 = LocalDate.parse(beginDate, dtf).atStartOfDay();
        LocalDateTime date2 = LocalDate.parse(endDate, dtf).atStartOfDay();
        return (int) Duration.between(date1, date2).toDays();
    }

    public static double bankingRound(double val)
    {
        return (double) (Math.round(val * 100)) / 100;
    }

    public static String monthFormatFixer(int month){
        return (month>=10)?(""+month):("0"+month);
    }
}
