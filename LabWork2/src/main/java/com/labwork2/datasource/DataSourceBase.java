package com.labwork2.datasource;

import java.util.ArrayList;

public abstract class DataSourceBase {
    public abstract void connect() throws InterruptedException;
    public abstract void initElements() throws Exception;
    public abstract ArrayList<String> getMarketList() throws Exception;
    public abstract ArrayList<String> getQuotesList() throws Exception;
}
