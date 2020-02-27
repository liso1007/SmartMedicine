package com.example.smedicine.bean;

public class PlanDetail {
    //药名
    String medicineName;
    //小时
    int hour;
    //分钟
    int minute;
    //requestcode
    int requestcode;
    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getRequestcode() {
        return requestcode;
    }

    public void setRequestcode(int requestcode) {
        this.requestcode = requestcode;
    }
}
