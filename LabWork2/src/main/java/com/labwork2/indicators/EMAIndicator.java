package com.labwork2.indicators;

import java.util.ArrayList;

public class EMAIndicator extends IndicatorBase {
    // https://pro-ts.ru/indikatory-foreks/1555-indikator-ema
    public static double EMAForexIndicator(ArrayList<Double> close, double pricePercent, int beginTime, int time) {
        if (time > beginTime + 1 && time < close.size())
            return close.get(time) * pricePercent + (EMAForexIndicator(close, pricePercent, beginTime, time - 1) * (1 - pricePercent));
        else
            return 0;
    }
}
