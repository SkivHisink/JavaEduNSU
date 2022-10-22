package com.labwork2.utils;

import java.text.DecimalFormat;

/**
 * The Class MathUtils.
 *
 * 
 */
public class MathUtils {

    public static final String TWO_DEC_DOUBLE_FORMAT = "##.00";

    /**
     * Round double.
     *
     * @param value the value
     * @param format the format
     * @return the double
     */
    public static double roundDouble(double value, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return Double.valueOf(df.format(value));
    }

    public static double roundDouble(double val, int numberOfDigits) throws Exception {
        if(numberOfDigits < 0){
            throw new Exception("Number of digits after coma must be greter than or equal to 0");
        }
        int multiplier = (int)Math.pow(10, numberOfDigits);
        return (double) (Math.round(val * 100)) / 100;
    }

}
