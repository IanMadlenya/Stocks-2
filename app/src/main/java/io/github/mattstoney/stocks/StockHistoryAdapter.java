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

    public StockHistoryAdapter() {
        try {
            // Create YahooFinance QCOM stock object as sample
            Stock stock = YahooFinance.get("QCOM");

            // Configure time range for stock history
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            from.add(Calendar.MONTH, -1); // from 1 month ago

            List<HistoricalQuote> history = stock.getHistory(from, to, Interval.DAILY);

            // Construct the yData array to the length of the history list
            yData = new float[history.size()];

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