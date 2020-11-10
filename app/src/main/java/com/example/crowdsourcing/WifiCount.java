package com.example.crowdsourcing;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WifiCount {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    @ColumnInfo(name = "timeStamp")
    public long timeStamp;
    @ColumnInfo(name = "count")
    public int count;

    public WifiCount(long timeStamp, int count) {
        this.timeStamp = timeStamp;
        this.count = count;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
