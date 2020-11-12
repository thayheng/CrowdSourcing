package com.example.crowdsourcing;

public class WifiCount {
    public long timeStamp;
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
