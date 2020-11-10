package com.example.crowdsourcing;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class WiFi {
    @PrimaryKey(autoGenerate = true)
    public Long WID;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "macAddress")
    public String macAddress;
    @ColumnInfo(name = "timestamp")
    public long timestamp;

    public WiFi(@NonNull Long timestamp, String name, String macAddress) {
        this.timestamp = timestamp;
        this.name = name;
        this.macAddress = macAddress;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
