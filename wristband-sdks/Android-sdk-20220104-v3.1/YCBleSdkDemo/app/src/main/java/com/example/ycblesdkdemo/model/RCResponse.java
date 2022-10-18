package com.example.ycblesdkdemo.model;

import java.util.List;

public class RCResponse {

    private int dataType;

    private List<RcModel> data;


    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public List<RcModel> getData() {
        return data;
    }

    public void setData(List<RcModel> data) {
        this.data = data;
    }

    public static class RcModel{
       private int incidentIndex;
       private long incidentTime;
       private int scheduleEnable;
       private String incidentName;
       private int scheduleIndex;
       private int incidentID;
       private int  incidentEnable;

        public int getIncidentIndex() {
            return incidentIndex;
        }

        public void setIncidentIndex(int incidentIndex) {
            this.incidentIndex = incidentIndex;
        }

        public long getIncidentTime() {
            return incidentTime;
        }

        public void setIncidentTime(long incidentTime) {
            this.incidentTime = incidentTime;
        }

        public int getScheduleEnable() {
            return scheduleEnable;
        }

        public void setScheduleEnable(int scheduleEnable) {
            this.scheduleEnable = scheduleEnable;
        }

        public String getIncidentName() {
            return incidentName;
        }

        public void setIncidentName(String incidentName) {
            this.incidentName = incidentName;
        }

        public int getScheduleIndex() {
            return scheduleIndex;
        }

        public void setScheduleIndex(int scheduleIndex) {
            this.scheduleIndex = scheduleIndex;
        }

        public int getIncidentID() {
            return incidentID;
        }

        public void setIncidentID(int incidentID) {
            this.incidentID = incidentID;
        }

        public int getIncidentEnable() {
            return incidentEnable;
        }

        public void setIncidentEnable(int incidentEnable) {
            this.incidentEnable = incidentEnable;
        }

        @Override
        public String toString() {
            return "RcModel{" +
                    "incidentIndex=" + incidentIndex +
                    ", incidentTime=" + incidentTime +
                    ", scheduleEnable=" + scheduleEnable +
                    ", incidentName='" + incidentName + '\'' +
                    ", scheduleIndex=" + scheduleIndex +
                    ", incidentID=" + incidentID +
                    ", incidentEnable=" + incidentEnable +
                    '}';
        }

    }

    @Override
    public String toString() {
        return "RCResponse{" +
                "dataType=" + dataType +
                ", data=" + data +
                '}';
    }
}
