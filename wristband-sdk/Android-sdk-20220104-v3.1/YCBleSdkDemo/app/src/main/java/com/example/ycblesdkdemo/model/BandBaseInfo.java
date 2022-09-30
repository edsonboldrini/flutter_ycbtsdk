package com.example.ycblesdkdemo.model;

public class BandBaseInfo {
    private int dataType;
    private int code;
    private BandBaseInfoModel data;

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public BandBaseInfoModel getData() {
        return data;
    }

    public void setData(BandBaseInfoModel data) {
        this.data = data;
    }

    public static class BandBaseInfoModel{
        private String deviceBatteryValue;
        private String deviceVersion;

        public String getDeviceBatteryValue() {
            return deviceBatteryValue;
        }

        public void setDeviceBatteryValue(String deviceBatteryValue) {
            this.deviceBatteryValue = deviceBatteryValue;
        }

        public String getDeviceVersion() {
            return deviceVersion;
        }

        public void setDeviceVersion(String deviceVersion) {
            this.deviceVersion = deviceVersion;
        }
    }
}
