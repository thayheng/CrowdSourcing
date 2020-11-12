package com.example.crowdsourcing;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


public class ForegroundService extends Service {

    private WifiReceiver receiverWifi;
    private WifiManager wifiManager;

    private static final int HANDLE_DELAY_TIME_CHECK = 30 * 1000;
    private static final int HANDLE_WIFI_CHECK = 5;

    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            // handle message
            if (message.what == HANDLE_WIFI_CHECK) {
                // start scan wifi and and re-schedule to scan again
                wifiManager.startScan();
                mHandler.removeMessages(HANDLE_WIFI_CHECK);
                mHandler.sendEmptyMessageDelayed(HANDLE_WIFI_CHECK, HANDLE_DELAY_TIME_CHECK);
            }
            return false;
        }
    });


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startReceiver();
        new Thread(){
            @Override
            public void run() {
                wifiManager.startScan();
                //Send Message to to Handler to start scan again
                mHandler.removeMessages(HANDLE_WIFI_CHECK);
                mHandler.sendEmptyMessageDelayed(HANDLE_WIFI_CHECK, HANDLE_DELAY_TIME_CHECK);
            }
        }.start();

        return START_STICKY;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotification() {
        String NOTIFICATION_CHANNEL_ID = "com.example.andy.myapplication";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("WiFi Monitoring is running")
                .setContentText("Click to open app")
                .setContentIntent(pendingIntent).build();
        startForeground(1337, notification);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // check android version for show notification for foreground service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotification();
        } else {
            Intent notificationIntent = new
                    Intent(getApplicationContext(), MainActivity.class);
            PendingIntent contentIntent =
                    PendingIntent.getActivity(this, 0, notificationIntent, 0);
            Notification notif = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("WiFi Monitoring is running")
                    .setContentText("Click to open app")
                    .setUsesChronometer(true)
                    .setContentIntent(contentIntent)
                    .setOngoing(true)
                    .build();
            startForeground(1337, notif);
        }

        if (wifiManager == null) {
            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }
    }

    @Override
    public void onDestroy() {
        unRegisterReceiver();
        super.onDestroy();
    }

    private void startReceiver() {
        receiverWifi = new WifiReceiver(wifiManager);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(receiverWifi, intentFilter);
    }

    private void unRegisterReceiver() {
        unregisterReceiver(receiverWifi);
    }


}
