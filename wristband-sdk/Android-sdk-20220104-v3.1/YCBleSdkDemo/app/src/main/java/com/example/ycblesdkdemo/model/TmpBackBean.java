package com.example.ycblesdkdemo.model;

public class TmpBackBean {
    private String tmpVal;
    private long timeVa;

    public String getTmpVal() {
        return tmpVal;
    }

    public void setTmpVal(String tmpVal) {
        this.tmpVal = tmpVal;
    }

    public long getTimeVa() {
        return timeVa;
    }

    public void setTimeVa(long timeVa) {
        this.timeVa = timeVa;
    }

    @Override
    public String toString() {
        return "TmpBackBean{" +
                "tmpVal='" + tmpVal + '\'' +
                ", timeVa=" + timeVa +
                '}';
    }
}


