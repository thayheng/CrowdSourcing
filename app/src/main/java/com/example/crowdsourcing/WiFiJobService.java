package com.example.crowdsourcing;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class WiFiJobService extends JobService {
    private static final int JOB_ID = 111;

    private WifiManager wifiManager;
    private WifiReceiver receiverWifi;

    private void startReceiver() {
        receiverWifi = new WifiReceiver(wifiManager);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(receiverWifi, intentFilter);
    }

    private void unRegisterReceiver() {
        unregisterReceiver(receiverWifi);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob id=" + params.getJobId());
        if (wifiManager == null) {
            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }

        startReceiver();

        try {
            wifiManager.startScan();
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        startReceiver();
        ScheduleJobWiFi(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob id=" + params.getJobId());
        unRegisterReceiver();
        return true;
    }

//    public void JobFinished(JobParameters params, boolean needRescheduled){
//        if (needRescheduled==true){
//            Context context;
//            ScheduleJobWiFi(context);
//        }
//    }
    public static void ScheduleJobWiFi(Context context) {
        ComponentName serviceName = new ComponentName(context, WiFiJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, serviceName)
                .setPeriodic(60*1000)
                .build();
        JobScheduler scheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int result = scheduler.schedule(jobInfo);
        if (result == JobScheduler.RESULT_SUCCESS) {
            Toast.makeText(context, "Job is successfully scheduled!",
                    Toast.LENGTH_LONG).show();
        }

    }




}

