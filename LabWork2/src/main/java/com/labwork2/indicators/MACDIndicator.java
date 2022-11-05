package com.labwork2.indicators;

import java.util.ArrayList;

public class MACDIndicator extends IndicatorBase{
    //https://pro-ts.ru/indikatory-foreks/63-indikator-macd
    //usually periods MACD 12 and 26
    // EMAs - EMA small
    // EMAi - EMA big
    public static double MACDTradingViewIndicator(ArrayList<Double> data, int begin, int fastEMAWidth, int slowEMAWidth){
        var macdFMA = EMAIndicator.EMAForexIndicator(data, 2.0 / (1 + fastEMAWidth-begin), begin, fastEMAWidth);
        var macdSMA = EMAIndicator.EMAForexIndicator(data, 2.0 / (1 + slowEMAWidth-begin), begin, slowEMAWidth);
        return  macdFMA - macdSMA;
    }

    //12, 26
    public  static double MACDTradingViewIndicator(ArrayList<Double> data, int begin)
    {
        return MACDTradingViewIndicator(data, begin, 12, 26);
    }
}
