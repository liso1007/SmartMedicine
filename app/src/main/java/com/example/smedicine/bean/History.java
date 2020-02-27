package com.example.smedicine.bean;

public class History {
    /**
     * id": 2,
     * "group": "2019-06-20",
     * "name": "头孢拉定胶囊 (康良)",
     * "times": "2",
     * "num": "2",
     * "vaccineStatus": "ing"
     * */
    String time;

    String name;

    String times;

    String num;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
