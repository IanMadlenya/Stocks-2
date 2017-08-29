package io.github.mattstoney.stocks;

import android.widget.TextView;

import com.robinhood.spark.SparkAdapter;
import com.robinhood.spark.SparkView;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class StockHistoryAdapter extends SparkAdapter {
    private float[] yData;
    private Calendar from;
    private Calendar to;

    // Constructor with  default time range of one month
    public StockHistoryAdapter(Stock stock) {
        try {
            // Configure time range for stock history to one month
            from = Calendar.getInstance();
            to = Calendar.getInstance();
            from.add(Calendar.MONTH, -1); // from 1 month ago

            List<HistoricalQuote> history = stock.getHistory(from, to, Interval.DAILY);

            // Construct the yData array to the length of the history list
            yData = new float[history.size()];

            // Populate yData with history list
            for (int i = 0, count = yData.length; i < count; i++) {
                float data = history.get(i).getClose().floatValue();
                yData[i] = data;
            }

            notifyDataSetChanged();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Constructor with defined time range
    public StockHistoryAdapter(Stock stock, Calendar from) {
        try {
            to = Calendar.getInstance();

            // Currently the interval is daily for all time ranges, will need to change this
            List<HistoricalQuote> history = stock.getHistory(from, to, Interval.DAILY);

            // Construct the yData array to the length of the history list
            yData = new float[history.size()];

            // Populate yData with history list
            for (int i = 0, count = yData.length; i < count; i++) {
                float data = history.get(i).getClose().floatValue();
                yData[i] = data;
            }

            notifyDataSetChanged();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StockHistoryAdapter(float[] yData) {
        this.yData = yData;
    }

    @Override
    public int getCount() {
        return yData.length;
    }

    @Override
    public Object getItem(int index) {
        return yData[index];
    }

    @Override
    public float getY(int index) {
        return yData[index];
    }
}
