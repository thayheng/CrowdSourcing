package com.example.crowdsourcing;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {WiFi.class,WifiCount.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract WiFiDao WiFiDao();
    public abstract WiFiCountDao wiFiCountDao();
}
