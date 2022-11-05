package com.labwork2.datasource;

import java.util.ArrayList;

public class DownloadedDataBuffef extends DataSourceBase {
    @Override
    public void connect() {

    }

    @Override
    public void initElements() throws Exception {

    }

    @Override
    public ArrayList<String> getMarketList() throws Exception {
        return null;
    }

    @Override
    public ArrayList<String> getQuotesList() throws Exception {
        return null;
    }

    @Override
    public ArrayList<String> getIntervalList() throws Exception {
        return null;
    }

    @Override
    public void setMarket(String marketName, int marketNumber, int marketPos) throws Exception {

    }

    @Override
    public void setQuote(String quoteName, int quoteNumber, int quotePos) throws Exception {

    }

    @Override
    public void setInterval(String intervalName, int intervalNumber) throws Exception {

    }

    @Override
    public void setBeginDate() {

    }

    @Override
    public void setEndDate() {

    }

    @Override
    public String getMinDate() {
        return null;
    }

    @Override
    public void getData() {

    }

    @Override
    public void setBeginData(int day, int month, int year) {

    }

    @Override
    public void setEndData(int day, int month, int year) {

    }
}
