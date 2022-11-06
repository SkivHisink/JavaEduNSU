package com.labwork2.common;

import com.labwork2.datasource.FinamSelenium;
import com.labwork2.model.TradeData;
import com.labwork2.utils.TimeUtils;

import java.util.ArrayList;

public class FinamDataParser {
    public static ArrayList<TradeData> parse(String data)
    {
        ArrayList<TradeData> result = new ArrayList<>();
        if(data == null){
            return null;
        }
        var splittedData = data.split("\\r\\n");
        if(splittedData.length<2 || (!splittedData[0].contains("HIGH") ||
                !splittedData[0].contains("OPEN") || !splittedData[0].contains("CLOSE") ||
                !splittedData[0].contains("LOW")) ){
            return null;
        }
        for(int i = 1;i<splittedData.length;++i)
        {
            var temp = splittedData[i].split(",");
            TradeData tempData = new TradeData(temp[FinamConstants.STOCK_IDX],
                    TimeUtils.convertToMillisTime(temp[FinamConstants.TIME_IDX]),
                    Double.parseDouble(temp[FinamConstants.OPEN_IDX]),
                    Double.parseDouble(temp[FinamConstants.PRICE_IDX]),
                    Long.parseLong(temp[FinamConstants.VOLUME_IDX]),
                    temp[FinamConstants.DATE_IDX],
                    Double.parseDouble(temp[FinamConstants.HIGH_IDX]),
                            Double.parseDouble(temp[FinamConstants.LOW_IDX]));
            result.add(tempData);
        }
        return result;
    }
}
