package com.labwork2.datasource;

import java.util.ArrayList;

public abstract class DataSourceBase {
    public abstract void connect() throws InterruptedException;
    public abstract void initElements() throws Exception;
    public abstract ArrayList<String> getMarketList() throws Exception;
    public abstract ArrayList<String> getQuotesList() throws Exception;
    public abstract ArrayList<String> getIntervalList() throws Exception ;
    public abstract void setMarket(String marketName, int marketNumber) throws Exception;
    public abstract void setQuote(String quoteName, int quoteNumber) throws Exception;
    public abstract void setInterval(String intervalName, int intervalNumber) throws Exception;
    public abstract void setBeginDate();
    public abstract void setEndDate();
}
