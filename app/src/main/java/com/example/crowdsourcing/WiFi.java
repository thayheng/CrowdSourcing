package com.example.crowdsourcing;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class WiFi {
    @PrimaryKey @NonNull
    public String WID;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "datetime")
    public String datetime;
}
