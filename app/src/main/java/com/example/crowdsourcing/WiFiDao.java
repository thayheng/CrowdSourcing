package com.example.crowdsourcing;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WiFiDao {
    @Query("SELECT * FROM WIFI")
    List<WiFi> getALL();

}
