package com.example.crowdsourcing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    private static final String TAG = "GraphActivity";

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, GraphActivity.class);
        context.startActivity(intent);
    }

    private XYPlot plot;
    private List<WifiCount> wifiCountList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        plot = (XYPlot) findViewById(R.id.plot);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* wifiCountList = DatabaseClient.getInstance(getApplicationContext())
                .getAppDatabase().wiFiCountDao().getALL();*/

        List<WiFi> tmpList = DatabaseClient.getInstance(getApplicationContext()).getList();
        for (WiFi wiFi : tmpList) {
            int count = DatabaseClient.getInstance(getApplicationContext()).getCountByTimeStamp(wiFi.timestamp);
            WifiCount wifiCount = new WifiCount(wiFi.timestamp, count);
            wifiCountList.add(wifiCount);
        }

        initGraph();
    }

    private void initGraph() {
        //final Number[] domainLabels = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        if (wifiCountList.size() == 0) {
            return;
        }
        List<Number> domainLabels = new ArrayList<>();
        List<Number> series1Numbers = new ArrayList<>();

        for (int i = 0; i < wifiCountList.size(); i++) {
            WifiCount wifiCount = wifiCountList.get(i);
            series1Numbers.add(wifiCount.count);
            domainLabels.add(i + 1);
        }

        XYSeries series1 = new SimpleXYSeries(
                series1Numbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Wifi Monitor");
        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels);

        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        plot.addSeries(series1, series1Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels.get(i));
            }

            @Override
            public Object parseObject(String source, @NonNull ParsePosition pos) {
                return null;
            }
        });

    }
}