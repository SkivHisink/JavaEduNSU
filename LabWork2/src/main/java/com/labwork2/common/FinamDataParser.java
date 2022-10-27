package com.labwork2.common;

import com.labwork2.datasource.FinamSelenium;
import com.labwork2.model.TradeData;

import java.util.ArrayList;

public class FinamDataParser {
    public static ArrayList<TradeData> parse(String data)
    {
        ArrayList<TradeData> result = new ArrayList<>();
        if(data == null){
            return null;
        }
        var splittedData = data.split("\\\\r?\\\\n");
        if(splittedData.length<2 || (!splittedData[0].contains("HIGH") ||
                !splittedData[0].contains("OPEN") || !splittedData[0].contains("CLOSE") ||
                !splittedData[0].contains("LOW")) ){
            return null;
        }
        for(int i=0;i<splittedData.length;++i)
        {
            var temp = splittedData[i].split(",");
            TradeData tempData = new TradeData(temp[FinamConstants.STOCK_IDX],
                    temp[FinamConstants.STOCK_IDX],
                    temp[FinamConstants.STOCK_IDX],
                    temp[FinamConstants.STOCK_IDX],
                    temp[FinamConstants.STOCK_IDX],
                    temp[FinamConstants.STOCK_IDX],
                    temp[FinamConstants.STOCK_IDX]);
        }
        return result;
    }
}
