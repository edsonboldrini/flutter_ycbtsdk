package com.example.ycblesdkdemo.ecg.bean;

import java.util.List;

/**
 * @author: zhongying
 * @date: 2020/12/23
 * @Description: 心电手环本地数据同步
 */
public class EcgSyncListResponse {
    public int collectType;
    public int code;
    public List<DataBean> data;


    public class DataBean {
        public int collectDigits;
        public int collectType;
        public int collectSN; // 手环数据条数
        public int collectTotalLen;
        public long collectSendTime;
        public long collectStartTime;
        public int collectBlockNum;
    }
}
