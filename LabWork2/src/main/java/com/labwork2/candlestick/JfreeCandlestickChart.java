package com.labwork2.candlestick;

import java.awt.Color;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JPanel;

import com.labwork2.indicators.EMAIndicator;
import com.labwork2.indicators.MACDIndicator;
import com.labwork2.indicators.SMAIndicator;
import com.labwork2.model.TradeData;
import com.labwork2.utils.MathUtils;
import com.labwork2.utils.TimeUtils;
import javafx.scene.control.Label;
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


/**
 * The Class JfreeCandlestickChart.
 */
@SuppressWarnings("serial")
public class JfreeCandlestickChart extends JPanel {

    private static final DateFormat READABLE_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd kk:mm:ss");

    private OHLCSeries ohlcSeries;
    private TimeSeries volumeSeries;
    private TimeSeries EMASeries;
    public TimeSeries MACDSeries;
    public TimeSeries SMASeries;
    private ArrayList<Double> closeList;
    private ArrayList<FixedMillisecond> timeList;
    public int MIN = 60000;
    // Every minute
    private int timeInterval = 1;
    private TradeData candelChartIntervalFirstPrint = null;
    private double open = 0.0;
    private double close = 0.0;
    private double low = 0.0;
    private double high = 0.0;
    private long volume = 0;
    private JFreeChart candlestickChart;

    public JfreeCandlestickChart(String title) {
        // Create new chart
        volumeSeries = new TimeSeries("Volume");
        EMASeries = new TimeSeries("EMA");
        MACDSeries = new TimeSeries("MACD");
        SMASeries = new TimeSeries("SMA");
        ohlcSeries = new OHLCSeries("Price");
        closeList = new ArrayList<>();
        timeList = new ArrayList<>();
    }

    public JFreeChart getCandlestickChart() {
        return candlestickChart;
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
                false, new CustomHighLowItemLabelGenerator(new SimpleDateFormat("kk:mm"), new DecimalFormat("0.000")));
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
        timeRenderer.setDefaultToolTipGenerator(new StandardXYToolTipGenerator("Volume--> Time={1} Size={2}",
                new SimpleDateFormat("kk:mm"), new DecimalFormat("0")));
        // Create volumeSubplot
        XYPlot volumeSubplot = new XYPlot(volumeDataset, null, volumeAxis, timeRenderer);
        volumeSubplot.setBackgroundPaint(Color.white);
        /**
         * Creating EMA subplot
         */
        // creates TimeSeriesCollection as a volume dataset for volume chart
        TimeSeriesCollection EMADataset = new TimeSeriesCollection();
        EMADataset.addSeries(EMASeries);
        // Create volumeSubplot
        JFreeChart EMAresult = ChartFactory.createTimeSeriesChart("EMA", "Time", "EMA", EMADataset);
        XYPlot EMASubplot = EMAresult.getXYPlot();
        EMASubplot.setBackgroundPaint(Color.white);
        /**
         * Creating MACD+SMA subplot
         */
        // creates TimeSeriesCollection as a volume dataset for volume chart
        TimeSeriesCollection MACDDataset = new TimeSeriesCollection();
        MACDDataset.addSeries(MACDSeries);
        TimeSeriesCollection SMADataset = new TimeSeriesCollection();
        SMADataset.addSeries(SMASeries);
        // Create MACD and SMA Subplot
        final JFreeChart result = ChartFactory.createTimeSeriesChart("Dynamic Line And TimeSeries Chart",
                "Time", "MACD", MACDDataset, true, true, false);
        final XYPlot plot = result.getXYPlot();
        EMASubplot.setBackgroundPaint(Color.white);
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
        yaxis.setRange(MACDminVal, MACDmaxVal);
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
        mainPlot.add(EMASubplot, 1);
        mainPlot.add(plot, 1);
        mainPlot.setOrientation(PlotOrientation.VERTICAL);

        JFreeChart chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, mainPlot, true);
        chart.removeLegend();
        return chart;
    }

    private double MACDmaxVal = -9999999; // fix to double min and max
    private double MACDminVal = 9999999; // fix to double min and max

    public void fillIndicators(ArrayList<TradeData> data, int Time, double Price, int femaw, int semaw, int smaw) {
        int i = 0;
        try {
            var tempList = new ArrayList<Double>();
            var sizeTimeList = timeList.size();
            for (i = 0; i < sizeTimeList; ++i) {
                if(i + Time >= sizeTimeList)
                    break;
                EMASeries.add(timeList.get(i), EMAIndicator.EMAForexIndicator(closeList, Price, i, i + Time));
            }
            for (i = 0; i < sizeTimeList; ++i) {
                if (i + femaw >= sizeTimeList || i + semaw >= sizeTimeList)
                    break;
                var a = MACDIndicator.MACDTradingViewIndicator(closeList, i, i + femaw, i + semaw);
                MACDSeries.add(timeList.get(i), a);
                tempList.add(a);
            }
            for (i = 0; i < tempList.size(); ++i) {
                var b = SMAIndicator.SMATradingViewIndicator(tempList, i, smaw);
                SMASeries.add(timeList.get(i), b);
                MACDmaxVal = Double.max(Double.max(tempList.get(i), b), MACDmaxVal);
                MACDminVal = Double.min(Double.min(tempList.get(i), b), MACDminVal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    /**
     * Fill series with data.r
     *
     * @param t the t
     */
    public void addCandle(long time, double o, double h, double l, double c, long v, String date) {
        try {
            // Add bar to the data. Let's repeat the same bar
            Date inputDate = simpleDateFormat.parse(date);
            FixedMillisecond t = new FixedMillisecond(
                    READABLE_TIME_FORMAT.parse(date + " " + TimeUtils.convertToReadableTime(time)));
            ohlcSeries.add(t, o, h, l, c);
            volumeSeries.add(t, v);
            timeList.add(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public boolean setInterval(String interval, ArrayList<String> intervalList, Label infoLabel)
    {
// ticks
        if (interval.equals(intervalList.get(0))) {
            this.setTimeInterval(1);
            infoLabel.setText("INFO:" + "We don't work with ticks nowadays.");
            return false;
        }
        // mins
        else if (interval.equals(intervalList.get(1))) {
            this.setTimeInterval(1);
        }
        // 5 mins
        else if (interval.equals(intervalList.get(2))) {
            this.setTimeInterval(5);
        }
        // 10 mins
        else if (interval.equals(intervalList.get(3))) {
            this.setTimeInterval(10);
        }
        // 15 mins
        else if (interval.equals(intervalList.get(4))) {
            this.setTimeInterval(15);
        }
        // 30 min
        else if (interval.equals(intervalList.get(5))) {
            this.setTimeInterval(30);
        }
        // 1 hour
        else if (interval.equals(intervalList.get(6))) {
            this.setTimeInterval(60);
        }
        // 1 day
        else if (interval.equals(intervalList.get(7))) {
            this.setTimeInterval(60 * 24);
        }
        // 1 week
        else if (interval.equals(intervalList.get(8))) {
            this.setTimeInterval(60 * 24 * 7);
        }
        // 1 month
        else if (interval.equals(intervalList.get(9))) {
            this.setTimeInterval(60 * 24 * 7 * 4); // problemss
        } else {
            // something impossible happend
            return false;
        }
        return true;
    }
    String date = null;

    /**
     * Aggregate the (open, high, low, close, volume) based on the predefined time interval (1 minute)
     *
     * @param t the t
     */
    public void onTrade(TradeData t) {
        double price = t.getPrice();
        if (candelChartIntervalFirstPrint != null && date.equals(t.getDate())) {
            long time = t.getTime();
            if (timeInterval == (int) ((time / MIN) - (candelChartIntervalFirstPrint.getTime() / MIN))) {
                // Set the period close price
                close = MathUtils.roundDouble(price, MathUtils.FOUR_DEC_DOUBLE_FORMAT);
                // Add new candle
                closeList.add(close);
                addCandle(time, open, high, low, close, volume, t.getDate());
                // Reset the intervalFirstPrint to null
                candelChartIntervalFirstPrint = null;
            } else {
                // Set the current low price
                if (MathUtils.roundDouble(price, MathUtils.FOUR_DEC_DOUBLE_FORMAT) < low)
                    low = MathUtils.roundDouble(price, MathUtils.FOUR_DEC_DOUBLE_FORMAT);

                // Set the current high price
                if (MathUtils.roundDouble(price, MathUtils.FOUR_DEC_DOUBLE_FORMAT) > high)
                    high = MathUtils.roundDouble(price, MathUtils.FOUR_DEC_DOUBLE_FORMAT);

                volume += t.getVolume();
            }
        } else {
            // Set intervalFirstPrint
            candelChartIntervalFirstPrint = t;
            // the first trade price in the day (day open price)
            open = MathUtils.roundDouble(price, MathUtils.FOUR_DEC_DOUBLE_FORMAT);
            // the interval low
            low = MathUtils.roundDouble(price, MathUtils.FOUR_DEC_DOUBLE_FORMAT);
            // the interval high
            high = MathUtils.roundDouble(price, MathUtils.FOUR_DEC_DOUBLE_FORMAT);
            // set the initial volume
            volume = t.getVolume();
            // date
            date = t.getDate();
        }
    }

    public void setTimeInterval(int interval) {
        timeInterval = interval;
    }
}
