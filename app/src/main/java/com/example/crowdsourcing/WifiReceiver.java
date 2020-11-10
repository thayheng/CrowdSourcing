package com.example.crowdsourcing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class WifiReceiver extends BroadcastReceiver {
    private static final String TAG = "WifiReceiver";
    WifiManager wifiManager;
    StringBuilder sb;

    //ListView wifiDeviceList;
    public WifiReceiver(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
        //this.wifiDeviceList = wifiDeviceList;

    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive: " + action);
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
            sb = new StringBuilder();
            List<ScanResult> wifiList = wifiManager.getScanResults();
            ArrayList<String> deviceList = new ArrayList<>();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Long currentTime = System.currentTimeMillis();
            for (ScanResult scanResult : wifiList) {
                sb.append("\n").append(scanResult.SSID).append(" - ").append(scanResult.capabilities);
                deviceList.add(scanResult.SSID + " - " + scanResult.capabilities);
                Log.d(TAG, "onReceive: " + scanResult.BSSID + " :::: " + scanResult.timestamp);
                WiFi wiFi = new WiFi(currentTime, scanResult.SSID, scanResult.BSSID);
                DatabaseClient.getInstance(context).insert(wiFi);
            }

            if (wifiList.size() > 0) {
                WifiCount wifiCount = new WifiCount(currentTime, wifiList.size());
                DatabaseClient.getInstance(context).getAppDatabase().wiFiCountDao().insert(wifiCount);
            }

            //Toast.makeText(context, sb, Toast.LENGTH_SHORT).show();
            //ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, deviceList.toArray());
            //wifiDeviceList.setAdapter(arrayAdapter);
        }
    }
}
