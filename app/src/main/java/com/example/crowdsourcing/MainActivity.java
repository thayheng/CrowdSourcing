package com.example.crowdsourcing;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE_ACCESS_COARSE_LOCATION = 120;
    private static final String TAG = "MainActivity";

    private XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Start foreground service that will schedule the various Jobs
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.plot);
        // check if user already accept permission or not
        if (checkPermissions()) {
            startService();
        }

        initGraph();
    }

    private void initGraph() {
        final Number[] domainLabels = {1, 2, 3, 6, 7, 8, 9, 10, 13, 14};
        Number[] series1Numbers = {1, 4, 2, 8, 4, 16, 8, 32, 16, 64};
        Number[] series2Numbers = {5, 2, 10, 5, 20, 10, 40, 20, 80, 40};

        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");
        XYSeries series2 = new SimpleXYSeries(
                Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels);

        LineAndPointFormatter series2Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_2);

        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        series2Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        plot.addSeries(series1,series1Format);
        //plot.addSeries(series2,series2Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels[i]);
            }
            @Override
            public Object parseObject(String source, @NonNull ParsePosition pos) {
                return null;
            }
        });

    }

    // start Start service
    private void startService() {
        Intent serviceIntent = new Intent(getApplicationContext(), ForegroundService.class);
        ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
    }

    // check require permission for application for scan wifi
    private boolean checkPermissions() {
        boolean permissionGrant = false;
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        } else {
            permissionGrant = true;
        }
        return permissionGrant;

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<WiFi> list =DatabaseClient.getInstance(getApplicationContext()).getList();
        for (WiFi wiFi: list) {
            Log.d(TAG, "onResume: " + wiFi.name);
        }
    }

    public void CreateDatabase() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "db").build();
        Toast.makeText(this, "Database is Created",
                Toast.LENGTH_LONG).show();
    }

    // handle if user have accept permission request or not
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0) {
            return;
        }

        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if(requestCode == PERMISSION_REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            startService();
        }
    }
}