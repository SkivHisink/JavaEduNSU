import java.util.ArrayList;
import java.util.Random;

public class Main {
    // https://pro-ts.ru/indikatory-foreks/1555-indikator-ema
    public static double EMAIndicator(double[] close, double pricePercent, int beginTime, int time) {
        if (time >= 0 && time > beginTime + 1 && time < close.length) {
            return close[time] * pricePercent + (EMAIndicator(close, pricePercent, beginTime, time - 1) * (1 - pricePercent));
        } else {
            return 0;
        }
    }

    public double calculate(double[] result, double[] highPrice, double[] lowPrice, double[] closePrice, int time, int period) {
        double value = 0;
        double[] tr = new double[period];
        double[] dmPlus = new double[period];
        double[] dmMinus = new double[period];
        double[] trN = new double[period];
        double[] dmPlusN = new double[period];
        double[] dmMinusN = new double[period];
        double[] dx = new double[period];
        double[] adx = new double[period];
        int counter = 0;
        // int periodStart = qh.size() - periodLength;
        int periodEnd = period - 1;
        double high = highPrice[time + period];
        double low = lowPrice[time + period];
        // double close = qh.getLastPriceBar().getClose();
        double high_1 = highPrice[time + period - 1];
        double low_1 = lowPrice[time + period - 1];
        double close_1 = closePrice[time + period - 1];

        for (int i = 0; i < period - 1; i++) {
            tr[i] = tr[i + 1];
            dmPlus[i] = dmPlus[i + 1];
            dmMinus[i] = dmMinus[i + 1];
            trN[i] = trN[i + 1];
            dmPlusN[i] = dmPlusN[i + 1];
            dmMinusN[i] = dmMinusN[i + 1];
            dx[i] = dx[i + 1];
            adx[i] = adx[i + 1];
        }

        // the first calculation for ADX is the true range value (TR)
        tr[period - 1] = Math.max(high - low, Math.max(Math.abs(high
                - close_1), Math.abs(low - close_1)));

        // determines the positive directional movement or returns zero if there
        // is no positive directional movement.
        dmPlus[period - 1] = high - high_1 > low_1 - low ? Math.max(high
                - high_1, 0) : 0;

        // calculates the negative directional movement or returns zero if there
        // is no negative directional movement.
        dmMinus[period - 1] = low_1 - low > high - high_1 ? Math.max(
                low_1 - low, 0) : 0;

        // The daily calculations are volatile and so the data needs to be
        // smoothed. First, sum the last N periods for TR, +DM and - DM
        double trSum = 0;
        double dmPlusSum = 0;
        double dmMinusSum = 0;
        for (int i = 0; i < period; i++) {
            trSum += tr[i];
            dmPlusSum += dmPlus[i];
            dmMinusSum += dmMinus[i];
        }

        // The smoothing formula subtracts 1/Nth of yesterday's trN from
        // yesterday's trN and then adds today's TR value
        // The truncating function is used to calculate the indicator as close
        // as possible to the developer of the ADX's original form of
        // calculation (which was done by hand).
        trN[period - 1] = ((int) (1000 * (trN[period - 2]
                - (trN[period - 2] / period) + trSum))) / 1000;
        dmPlusN[period - 1] = ((int) (1000 * (dmPlusN[period - 2]
                - (dmPlusN[period - 2] / period) + dmPlusSum))) / 1000;
        dmMinusN[period - 1] = ((int) (1000 * (dmMinusN[period - 2]
                - (dmMinusN[period - 2] / period) + dmMinusSum))) / 1000;

        // Now we have a 14-day smoothed sum of TR, +DM and -DM.
        // The next step is to calculate the ratios of +DM and -DM to TR.
        // The ratios are called the +directional indicator (+DI) and
        // -directional indicator (-DI).
        // The integer function (int) is used because the original developer
        // dropped the values after the decimal in the original work on the ADX
        // indicator.
        double diPlus = (int) (100 * dmPlusN[period - 1] / trN[period - 1]);
        double diMinus = (int) (100 * dmMinusN[period - 1] / trN[period - 1]);
        ;

        // The next step is to calculate the absolute value of the difference
        // between the +DI and the -DI and the sum of the +DI and -DI.
        double diDiff = Math.abs(diPlus - diMinus);
        double diSum = diPlus + diMinus;

        // The next step is to calculate the DX, which is the ratio of the
        // absolute value of the difference between the +DI and the -DI divided
        // by the sum of the +DI and the -DI.
        dx[period - 1] = (int) (100 * (diDiff / diSum));

        // The final step is smoothing the DX to arrive at the value of the ADX.
        // First, average the last N days of DX values
        double dxMedia = 0;
        for (int i = 0; i < period; i++) {
            dxMedia += dx[i];
        }
        dxMedia /= period;

        // The smoothing process uses yesterday's ADX value multiplied by N-1,
        // and then add today's DX value. Finally, divide this sum by N.
        if (counter == 2 * (period - 1)) {
            adx[period - 2] = dxMedia;
        }
        adx[period - 1] = (adx[period - 2] * (period - 1) + dx[period - 1])
                / period;

        counter++;

        value = adx[period - 1];
        return value;
    }

    // https://bcs-express.ru/novosti-i-analitika/indikator-adx-opredeliaem-sil-nye-trendy
    public static void ADXIndocator(ArrayList<Double> result, double[] highPrice, double[] lowPrice, double[] closePrice, int period) {
        double[] plusM = new double[period];
        double[] minusM = new double[period];
        double[] plusDM = new double[period];
        double[] minusDM = new double[period];
        double[] TR = new double[period];
        double[] plusTempDI = new double[period];
        double[] minusTempDI = new double[period];
        double[] plusDI = new double[highPrice.length];
        double[] minusDI = new double[highPrice.length];
        double[] ADXTemp = new double[highPrice.length];
        for (int i = 1; i < highPrice.length; i++) {
            int k = 0;
            for (int j = i - period; j < i; j++) {
                if (j < 0) {
                    plusM[k] = 0;
                    minusM[k] = 0;
                    plusDM[k] = 0;
                    minusDM[k] = 0;
                    TR[k] = 0;
                    plusTempDI[k] = 0;
                    minusTempDI[k] = 0;
                } else {
                    plusM[k] = highPrice[i] - highPrice[i - 1];
                    minusM[k] = lowPrice[i - 1] - lowPrice[i];
                    plusDM[k] = 0;
                    minusDM[k] = 0;
                    plusTempDI[k] = 0;
                    minusTempDI[k] = 0;
                    if (plusM[k] > minusM[k] && plusM[k] > 0) {
                        plusDM[k] = plusM[k];
                    }
                    if (minusM[k] > plusM[k] && minusM[k] > 0) {
                        minusDM[k] = minusM[k];
                    }
                    TR[k] = 0;
                    if (j > 0) {
                        TR[k] = Math.max(highPrice[j], closePrice[j - 1]) - Math.min(lowPrice[j], closePrice[j - 1]);
                    }
                    if (TR[k] != 0) {
                        plusTempDI[k] = plusDM[k] / TR[k];
                        minusTempDI[k] = minusDM[k] / TR[k];
                    }
                }
                k++;
            }
            plusDI[i] = EMAIndicator(plusTempDI, 1.0 / (2.0 + period), 0, period - 1);
            minusDI[i] = EMAIndicator(minusTempDI, 1.0 / (2.0 + period), 0, period - 1);
            ADXTemp[i] = Math.abs(plusDI[i] - minusDI[i]) / (plusDI[i] + minusDI[i]);
            if (Double.isNaN(ADXTemp[i])) {
                ADXTemp[i] = 0;
            }
            result.add(EMAIndicator(ADXTemp, 1.0 / (2.0 + period), i - 14, i - 14 + period - 1) * 100);
        }
    }

    public static double[] createAndFillPrice(int n, double min, double max) {
        if (n < 1) {
            return null;
        }
        double diff = max - min;
        final Random random = new Random();
        double[] resultArray = new double[n];
        for (int i = 0; i < n; i++) {
            resultArray[i] = min + random.nextDouble(diff + 1.0);
        }
        return resultArray;
    }

    public static void printAllValue(ArrayList<Double> indicator, double[] highPrice, double[] lowPrice, double[] closePrice) {
        System.out.println("n|ADX|high|low|close");
        for (int i = 1; i < indicator.size() + 1; i++) {
            System.out.println((i) + "|" + indicator.get(i - 1) + "|" + highPrice[i] + "|" + lowPrice[i] + "|" + closePrice[i]);
        }
    }

    public static void main(String[] args) {
        int num = 100;
        double[] highPrice = createAndFillPrice(num, 180, 200);
        double[] lowPrice = createAndFillPrice(num, 160, 180);
        double[] closePrice = createAndFillPrice(num, 160, 200);
        for (int i = 0; i < closePrice.length; i++) {
            if (closePrice[i] < lowPrice[i]) {
                closePrice[i] = lowPrice[i];
            }
            if (closePrice[i] > highPrice[i]) {
                closePrice[i] = highPrice[i];
            }
        }
        if (highPrice == null || lowPrice == null) {
            return;
        }
        ArrayList<Double> indicator = new ArrayList<Double>();
        // Обычно индикатор используют 14ти периодный
        ADXIndocator(indicator, highPrice, lowPrice, closePrice, 14);
        printAllValue(indicator, highPrice, lowPrice, closePrice);
    }
}