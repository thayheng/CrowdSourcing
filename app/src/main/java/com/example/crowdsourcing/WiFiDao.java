package com.example.crowdsourcing;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WiFiDao {
    @Query("SELECT * FROM WIFI")
    List<WiFi> getALL();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WiFi wiFi);
}
