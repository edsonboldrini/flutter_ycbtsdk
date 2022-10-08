package com.example.ycblesdkdemo.model;

import java.util.List;

public class HistEcgResponse {
    private int collectType;
    private int dataType;
    private int code;
    private List<HisEcgModel> data;


    public int getCollectType() {
        return collectType;
    }

    public void setCollectType(int collectType) {
        this.collectType = collectType;
    }

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

    public List<HisEcgModel> getData() {
        return data;
    }

    public void setData(List<HisEcgModel> data) {
        this.data = data;
    }

    public static class HisEcgModel{
        private int collectType;
        private int collectBlockNum;
        private int collectSN;
        private long collectStartTime;
        private long collectTotalLen;
        private long collectSendTime;


        public int getCollectType() {
            return collectType;
        }

        public void setCollectType(int collectType) {
            this.collectType = collectType;
        }

        public int getCollectBlockNum() {
            return collectBlockNum;
        }

        public void setCollectBlockNum(int collectBlockNum) {
            this.collectBlockNum = collectBlockNum;
        }

        public int getCollectSN() {
            return collectSN;
        }

        public void setCollectSN(int collectSN) {
            this.collectSN = collectSN;
        }

        public long getCollectStartTime() {
            return collectStartTime;
        }

        public void setCollectStartTime(long collectStartTime) {
            this.collectStartTime = collectStartTime;
        }

        public long getCollectTotalLen() {
            return collectTotalLen;
        }

        public void setCollectTotalLen(long collectTotalLen) {
            this.collectTotalLen = collectTotalLen;
        }

        public long getCollectSendTime() {
            return collectSendTime;
        }

        public void setCollectSendTime(long collectSendTime) {
            this.collectSendTime = collectSendTime;
        }
    }

}


