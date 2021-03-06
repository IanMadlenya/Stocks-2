package io.github.mattstoney.stocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.robinhood.spark.SparkAdapter;
import com.robinhood.spark.SparkView;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private StockHistoryAdapter adapter;
    private TextView stockPrice;
    private SparkView stockHistoryGraph;
    private Stock stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a spark graph using data from YahooFinanceAPI
        Thread sparkGraph = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    stockHistoryGraph = (SparkView) findViewById(R.id.stockhistorygraph);

                    // Create YahooFinance QCOM stock object as sample
                    stock = YahooFinance.get("QCOM");

                    // Set starting date for stock history
                    Calendar from = Calendar.getInstance();
                    from.add(Calendar.MONTH, -2); // two months

                    adapter = new StockHistoryAdapter(stock, from);

                    // Gets the TextView object from the activity_main...
                    stockPrice = (TextView) findViewById(R.id.stockPrice);

                    BigDecimal bdStockPrice = stock.getQuote().getPrice();
                    String strStockPrice = NumberFormat.getCurrencyInstance(Locale.US).format(bdStockPrice);

                    // Sets the TextView object
                    stockPrice.setText(strStockPrice);

                    // A listener for when the spark view is scrubbed...
                    stockHistoryGraph.setScrubListener(new SparkView.OnScrubListener() {
                        @Override
                        public void onScrubbed(Object value) {
                            // When the scrubber is not scrubbing set the value to an empty string...
                            if(value == null) {
                                BigDecimal bdStockPrice = stock.getQuote().getPrice();
                                String strStockPrice = NumberFormat.getCurrencyInstance(Locale.US).format(bdStockPrice);
                                stockPrice.setText(strStockPrice);
                            }
                            // Sets the value when the value is not null...
                            else {
                                String strStockPrice = NumberFormat.getCurrencyInstance(Locale.US).format(value);
                                stockPrice.setText(strStockPrice);
                            }
                        }
                    });

                    stockHistoryGraph.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sparkGraph.start();

    }
}

