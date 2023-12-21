package com.exlabwork2;

import java.awt.Color;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;


@SuppressWarnings("serial")
public class JfreeCandlestickChart extends JPanel {

    private static final DateFormat READABLE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

    public boolean IsEma = true;
    public boolean IsMacd = true;
    private OHLCSeries ohlcSeries;
    private TimeSeries volumeSeries;
    private TimeSeries EMASeries;
    public TimeSeries MACDSeries;
    private ArrayList<Double> closeList;
    private ArrayList<FixedMillisecond> timeList;
    public int MIN = 60000;
    private int timeInterval = 1;
    private TradeItem candelChartIntervalFirstPrint = null;
    // Нужно для интервалов, но данный источник данных не даёт разных интервалов
    private double open = 0.0;
    private double close = 0.0;
    private double low = 0.0;
    private double high = 0.0;
    private long volume = 0;
    private double MACDmaxVal = Double.MIN_VALUE;
    private double MACDminVal = Double.MAX_VALUE;
    private int emaTS;
    private double emaSF;
    private int femaw;
    private int semaw;
    private int smaw;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public JfreeCandlestickChart(String title) {
        // Create new chart
        volumeSeries = new TimeSeries("Volume");
        EMASeries = new TimeSeries("EMA");
        MACDSeries = new TimeSeries("MACD");
        ohlcSeries = new OHLCSeries("Price");
        closeList = new ArrayList<>();
        timeList = new ArrayList<>();
    }

    public JFreeChart createChart(String chartTitle) {
        /**
         * Creating candlestick subplot
         */
        // Create OHLCSeriesCollection as a price dataset for candlestick chart
        OHLCSeriesCollection candlestickDataset = new OHLCSeriesCollection();
        candlestickDataset.addSeries(ohlcSeries);
        // Create candlestick chart priceAxis
        NumberAxis priceAxis = new NumberAxis("Price");
        priceAxis.setAutoRangeIncludesZero(false);
        // Create candlestick chart renderer
        CandlestickRenderer candlestickRenderer = new CandlestickRenderer(CandlestickRenderer.WIDTHMETHOD_AVERAGE,
                false, new CustomHighLowItemLabelGenerator(new SimpleDateFormat("kk:mm"),
                new DecimalFormat("0.000")));
        // Create candlestickSubplot
        XYPlot candlestickSubplot = new XYPlot(candlestickDataset, null, priceAxis, candlestickRenderer);
        candlestickSubplot.setBackgroundPaint(Color.white);
        /**
         * Creating volume subplot
         */
        // creates TimeSeriesCollection as a volume dataset for volume chart
        TimeSeriesCollection volumeDataset = new TimeSeriesCollection();
        volumeDataset.addSeries(volumeSeries);
        // Create volume chart volumeAxis
        NumberAxis volumeAxis = new NumberAxis("Volume");
        volumeAxis.setAutoRangeIncludesZero(false);
        // Set to no decimal
        volumeAxis.setNumberFormatOverride(new DecimalFormat("0"));
        // Create volume chart renderer
        XYBarRenderer timeRenderer = new XYBarRenderer();
        timeRenderer.setShadowVisible(false);
        timeRenderer.setDefaultToolTipGenerator(new StandardXYToolTipGenerator(
                "Volume--> Time={1} Size={2}",
                new SimpleDateFormat("kk:mm"), new DecimalFormat("0")));
        // Create volumeSubplot
        XYPlot volumeSubplot = new XYPlot(volumeDataset, null, volumeAxis, timeRenderer);
        volumeSubplot.setBackgroundPaint(Color.white);
        /**
         * Creating EMA subplot
         */
        // creates TimeSeriesCollection as a volume dataset for volume chart
        TimeSeriesCollection EMADataset = new TimeSeriesCollection();
        if (IsEma) {
            EMADataset.addSeries(EMASeries);
        }
        // Create volumeSubplot
        JFreeChart EMAresult = ChartFactory.createTimeSeriesChart("EMA" + "TS = " + emaTS +
                " SF = " + emaSF, "Time", "EMA", EMADataset);
        XYPlot EMASubplot = EMAresult.getXYPlot();
        EMASubplot.setBackgroundPaint(Color.white);
        /**
         * Creating MACD+SMA subplot
         */
        // creates TimeSeriesCollection as a volume dataset for volume chart
        TimeSeriesCollection MACDDataset = new TimeSeriesCollection();
        if (IsMacd) {
            MACDDataset.addSeries(MACDSeries);
        }
        TimeSeriesCollection SMADataset = new TimeSeriesCollection();

        // Create MACD and SMA Subplot
        final JFreeChart result;
        String valueAxisName = "";
        if (IsMacd) {
            valueAxisName = "MACD";
        }
        result = ChartFactory.createTimeSeriesChart("Dynamic Line And TimeSeries Chart",
                "Time", valueAxisName, MACDDataset, true, true, false);
        final XYPlot plot = result.getXYPlot();
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        // first time series
        final ValueAxis xaxis = plot.getDomainAxis();
        xaxis.setAutoRange(true);
        // Domain axis would show data of 60 seconds for a time
        xaxis.setVerticalTickLabels(true);
        final ValueAxis yaxis = plot.getRangeAxis();
        if (MACDmaxVal > MACDminVal) {
            yaxis.setRange(MACDminVal, MACDmaxVal);
        }
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        final NumberAxis yAxis1 = (NumberAxis) plot.getRangeAxis();
        yAxis1.setTickLabelPaint(Color.RED);
        // second time series
        plot.setDataset(1, SMADataset); // the second dataset (datasets are zero-based numbering)
        plot.mapDatasetToDomainAxis(1, 0); // same axis, different dataset
        plot.mapDatasetToRangeAxis(1, 0); // same axis, different dataset
        renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(1, renderer);
        /**
         * Create chart main plot with two subplots (candlestickSubplot,
         * volumeSubplot) and one common dateAxis
         */
        // Creating charts common dateAxis
        DateAxis dateAxis = new DateAxis("Time");
        dateAxis.setDateFormatOverride(new SimpleDateFormat("kk:mm"));
        // reduce the default left/right margin from 0.05 to 0.02
        dateAxis.setLowerMargin(0.02);
        dateAxis.setUpperMargin(0.02);
        // Create mainPlot
        CombinedDomainXYPlot mainPlot = new CombinedDomainXYPlot(dateAxis);
        mainPlot.setGap(10.0);
        mainPlot.add(candlestickSubplot, 3);
        mainPlot.add(volumeSubplot, 1);
        if (IsEma) {
            mainPlot.add(EMASubplot, 1);
        }
        if (IsMacd) {
            mainPlot.add(plot, 1);
        }
        mainPlot.setOrientation(PlotOrientation.VERTICAL);

        JFreeChart chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, mainPlot, true);
        chart.removeLegend();
        return chart;
    }


    public void calculateIndicator(ArrayList<TradeItem> data, int time, double price, int femaw, int semaw) {
        int i = 0;
        emaTS = time;
        emaSF = price;
        this.femaw = femaw;
        this.semaw = semaw;
        this.smaw = smaw;
        try {
            var tempList = new ArrayList<Double>();
            var sizeTimeList = timeList.size();
            if (IsEma) {
                EMASeries = new TimeSeries("EMA:" + "TS = " +
                        emaTS + " SF = " + emaSF);
                for (i = 0; i < sizeTimeList; ++i) {
                    if (i + time >= sizeTimeList)
                        break;
                    EMASeries.add(timeList.get(i), EMAForexIndicator(closeList,
                            price, i, i + time));
                }
            }
            if (IsMacd) {
                MACDSeries = new TimeSeries("MACD:" + "fastTS = " + femaw + " slowTS = " + semaw);
                for (i = 0; i < sizeTimeList; ++i) {
                    if (i + femaw >= sizeTimeList ||
                            i + semaw >= sizeTimeList)
                        break;
                    var a = MACDTradingViewIndicator(closeList,
                            i, i + femaw, i + semaw);
                    MACDSeries.add(timeList.get(i), a);
                    tempList.add(a);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double EMAForexIndicator(ArrayList<Double> close, double pricePercent, int beginTime, int time) {
        if (time > beginTime + 1 && time < close.size())
            return close.get(time) * pricePercent + (EMAForexIndicator(close,
                    pricePercent, beginTime, time - 1) * (1 - pricePercent));
        else
            return 0;
    }
    public static double MACDTradingViewIndicator(ArrayList<Double> data, int begin,
                                                  int fastEMAWidth, int slowEMAWidth){
        var macdFMA = EMAForexIndicator(data,
                2.0 / (1 + fastEMAWidth - begin), begin, fastEMAWidth);
        var macdSMA = EMAForexIndicator(data,
                2.0 / (1 + slowEMAWidth-begin), begin, slowEMAWidth);
        return  macdFMA - macdSMA;
    }

    // обычно периоды MACD 12 and 26
    public  static double MACDTradingViewIndicator(ArrayList<Double> data, int begin)
    {
        return MACDTradingViewIndicator(data, begin, 12, 26);
    }

    public static String convertToReadableTime(long millis) {
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;

        String time = String.format("%02d:%02d:%02d:%d", hour, minute, second, millis);
        return time;
    }

    public void addCandle(long time, double open, double high, double low, double close, long volume, String date) {
        try {
            FixedMillisecond t = new FixedMillisecond(
                    READABLE_TIME_FORMAT.parse(date + " " + convertToReadableTime(time)));
            ohlcSeries.add(t, open, high, low, close);
            volumeSeries.add(t, volume);
            timeList.add(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setInterval() {
        this.setTimeInterval(60 * 24 * 7);

    }

    public void onTrade(TradeItem tradeItem) {
        long time = tradeItem.getTime();
        closeList.add(tradeItem.getPrice());
        addCandle(time, tradeItem.getOpen(), tradeItem.getHigh(), tradeItem.getLow(),
                tradeItem.getPrice(), tradeItem.getVolume(), tradeItem.getDate());
    }

    public void setTimeInterval(int interval) {
        timeInterval = interval;
    }
}
