package com.example.crowdsourcing;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WiFiDao {
    @Query("SELECT * FROM WIFI GROUP BY timestamp")
    List<WiFi> getALL();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WiFi wiFi);

    @Query("SELECT COUNT(macAddress) FROM WIFI GROUP BY timestamp")
    int getCount();
}
