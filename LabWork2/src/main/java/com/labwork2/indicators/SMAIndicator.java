package com.labwork2.indicators;

import java.util.ArrayList;

public class SMAIndicator extends IndicatorBase {
    public static double SMATradingViewIndicator(ArrayList<Double> data, int begin, int step) {
        Double result = 0.0;
        int i = begin;
        for (; i < begin + step && i < data.size(); ++i) {
            result += data.get(i);
        }
        result /= (i - begin);
        return result;
    }
}
