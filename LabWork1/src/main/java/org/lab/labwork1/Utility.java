package org.lab.labwork1;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utility {
    public static String CreditAmountText = "Credit amount:";
    public static String CreditTermText = "Credit term:";
    public static String InterestRateText = "Interest rate:";
    public static String InfoBegin = "INFO:";
    public static String ReadmeRuText = "Для использования данной программы вам необходимо проделать следующие шаги:\n" +
            "1) Создать файл формата \"*.xlsx\";\n" +
            "2) На первом листе на второй строке разместить входные данные в общем формате ячеек где | - новая ячейка:\n" +
            "Сумма кредита(численное значение)|Срок кредита(целое численное значение)|Процентная ставка(численное значение)|Дата платежа(целое численное значение)| " +
            "Кредит предоставляется заемщику(дата через точку). Пример данных:\n" +
            "\"9200000|276|7.45|25|22.09.2022\". В результате должна получится строка с 5-ю заполненными ячейками;\n" +
            "3) Загрузите ваш файл с помощью кнопки \"Open init param\";\n" +
            "4) Выберите тип расчёта и нажмите кнопку \"Calculate\";\n" +
            "5) При необходимости вы можете посмотреть график или сохранить результаты при помощи кнопок \"Draw graphic\" и \"Save in Excel\" соответственно.";
    public static String ReadmeEnText = "To use this program, you need to do the following steps:\n" +
            "1) Create a file of \"*.xlsx\" format;\n" +
            "2) On the first sheet on the second line, place the input data in the general format of cells where | - new cell:\n" +
            "Credit amount(numerical value)|Credit term(integer numerical value)|Interest rate(numerical value)|Payment date(integer numerical value)| \n" +
            "The credit is provided to the borrower (date separated by a dot).Sample data:\"9200000|276|7.45|25|22.09.2022\".\n" +
            "The result should be a line with 5 filled cells;\n" +
            "3) Upload your file using the \"Open init param\" button;\n" +
            "4) Select the calculation type and click the \"Calculate\" button;\n" +
            "5) If necessary, you can view the graph or save the results using the \"Draw graphic\" and \"Save in Excel\" buttons, respectively.";

    // А потом люди удивляются почему джава "хороший язык"
    // параметры по умолчание не работают, используем костыль, который передаёт формат "dd.MM.yyyy"
    public static int getDaysBetweenTwoDates(String beginDate, String endDate) {
        return getDaysBetweenTwoDates(beginDate, endDate, "dd.MM.yyyy");
    }

    public static int getDaysBetweenTwoDates(String beginDate, String endDate, String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        LocalDateTime date1 = LocalDate.parse(beginDate, dtf).atStartOfDay();
        LocalDateTime date2 = LocalDate.parse(endDate, dtf).atStartOfDay();
        return (int) Duration.between(date1, date2).toDays();
    }

    public static double bankingRound(double val) {
        return (double) (Math.round(val * 100)) / 100;
    }

    public static String monthFormatFixer(int month) {
        return (month >= 10) ? ("" + month) : ("0" + month);
    }
}
