package com.example.ycblesdkdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ycblesdkdemo.model.BandBaseInfo;
import com.example.ycblesdkdemo.model.HistEcgResponse;
import com.google.gson.Gson;
import com.yucheng.ycbtsdk.Constants;
import com.yucheng.ycbtsdk.response.BleDataResponse;
import com.yucheng.ycbtsdk.YCBTClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class OldActivity extends Activity {

    public static String Tag = OldActivity.class.getName();

    private TextView timeSetView;
    private TextView batteryView;
    private TextView tiwenView;

    private TextView historyHrView;
    private TextView historySleepView;
    private TextView historySportView;

    private TextView hisEcgPpgView;

    private TextView xinLvNoticeView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old);
        initView();
    }

    private void initView(){
        timeSetView = (TextView) this.findViewById(R.id.time_set_view);
        batteryView = (TextView) this.findViewById(R.id.time_battery_view);
        tiwenView = (TextView) this.findViewById(R.id.time_tmpturedata_view);

        historyHrView = (TextView) this.findViewById(R.id.get_history_hr_view);
        historySleepView = (TextView) this.findViewById(R.id.get_history_sleep_view);
        historySportView = (TextView) this.findViewById(R.id.get_history_sport_view);

        hisEcgPpgView = (TextView)this.findViewById(R.id.get_history_ecgppg_view);
        xinLvNoticeView = (TextView)this.findViewById(R.id.xinlv_notice_view);

        //设置时间模式0x01 12小时，0x00 24小时
        timeSetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingUnit(0, 0x00, 0x00, 0x01, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        if(code == 0){
                            Log.e("TimeSetActivity", "时间模式设置成功=");
                        }else {
                            Log.e("TimeSetActivity", "时间模式设置失败=");
                        }
                    }
                });
            }
        });


        //获取电量  获取设备基本信息;设备ID, 固件版本号,电池状态,电池电量,绑定状态
        batteryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.getDeviceInfo(new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        if(code == 0){
                            if (resultMap != null) {

                                String backVal = resultMap.toString();
                                Gson gson = new Gson();
                                BandBaseInfo bandBaseInfo = gson.fromJson(backVal, BandBaseInfo.class);
                                Log.e(Tag, "resultMap=" + resultMap.toString());
                                Toast.makeText(OldActivity.this,"电量获取="+ bandBaseInfo.getData().getDeviceBatteryValue() + ";deviceVersion=" + bandBaseInfo.getData().getDeviceVersion(),Toast.LENGTH_SHORT).show();
//                                Log.e(Tag, "。。。电量获取" + ";deviceBatteryValue=" + bandBaseInfo.getData().getDeviceBatteryValue() + ";deviceVersion=" + bandBaseInfo.getData().getDeviceVersion());
                            }
                        }else {
                            Toast.makeText(OldActivity.this,"获取电量失败"+code,Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });


        //体温数据获取
        tiwenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.healthHistoryData(0x051E, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        if (resultMap != null) {
                            Log.e(Tag, "获取体温......" + resultMap.toString());
//                            try {
//                                ArrayList<HashMap> lists = (ArrayList<HashMap>) resultMap.get("data");
//                                if (lists != null) {
//                                    String temp = "";
//                                    List<TmpBackBean> listTmpBeans = new ArrayList<>();
//                                    for (HashMap map : lists) {
//                                        if(map != null){
//                                            int tempIntValue = (int) map.get("tempIntValue");
//                                            int tempFloatValue = (int) map.get("tempFloatValue");
//                                            long startTime = (long) map.get("startTime");
//
//                                            if (tempFloatValue != 15) {
//                                                temp = tempIntValue + "." + tempFloatValue;
//
//                                                TmpBackBean tmpBackModel = new TmpBackBean();
//                                                tmpBackModel.setTimeVa(startTime);
//                                                tmpBackModel.setTmpVal(temp);
//                                                listTmpBeans.add(tmpBackModel);
//                                            }
//                                            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(startTime));
//                                            Log.e(Tag, "temp=" + temp + ";time=" + time);
//                                        }
//                                    }
//                                }
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
                        } else {
                            Log.e(Tag, "没有获取到体温数据......");
                        }
                    }
                });
            }
        });


        historyHrView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                YCBTClient.healthHistoryData(Constants.DATATYPE.Health_HistoryHeart, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int i, float v, HashMap hashMap) {

                        Log.e(Tag, "同步心率。。。。" + hashMap.toString());
                    }
                });

            }
        });

        historySleepView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.healthHistoryData(0x0504, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int i, float v, HashMap hashMap) {

                        Log.e(Tag, "sleep hashMap=" + hashMap.toString());

                    }
                });
            }
        });

        historySportView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.healthHistoryData(Constants.DATATYPE.Health_HistorySport, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int i, float v, HashMap hashMap) {
                        Log.e(Tag, "同步步数。。。。" + hashMap.toString());
                    }
                });
            }
        });


        hisEcgPpgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncHistoryEcgTime();
            }
        });


        xinLvNoticeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingHeartAlarm(1, 10, 50, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e(Tag, "心率报警。。。。code=" + code);
                    }
                });
            }
        });

    }




    private List<Long> ecgUseTimeVals = new ArrayList<>();
    private List<Long> ppgUseTimeVals = new ArrayList<>();

    private List<Long> ecgTimeVals = new ArrayList<>();
    private List<Long> ppgTimeVals = new ArrayList<>();

    int doubleType = 0;

    //获取ecg时间戳
    private void syncHistoryEcgTime() {

        Log.e("yc-ble", "同步历史ecg。。。。");

        //获取ecg时间戳
        YCBTClient.collectEcgList(new BleDataResponse() {
            @Override
            public void onDataResponse(int i, float v, HashMap hashMap) {
                if (hashMap != null) {
                    ecgTimeVals.clear();
                    Log.e("historydata", "his ecg hashMap=" + hashMap.toString());
                    String backVal = hashMap.toString();
                    Gson gson = new Gson();
                    HistEcgResponse response = gson.fromJson(backVal, HistEcgResponse.class);
                    for (int k = 0; k < response.getData().size(); k++) {
                        List<HistEcgResponse.HisEcgModel> dataVal = response.getData();
                        ecgTimeVals.add(dataVal.get(k).getCollectSendTime());
                    }
                    syncHistoryPpgTime();
                } else {
                    Log.e("historydata", "同步历史ecg no his ecg");
                    syncHistoryPpgTime();
                }
            }
        });
    }


    //获取ppg时间戳
    private void syncHistoryPpgTime() {

        Log.e("yc-ble", "同步历史ppg。。。。");

        YCBTClient.collectPpgList(new BleDataResponse() {
            @Override
            public void onDataResponse(int i, float v, HashMap hashMap) {
                if (hashMap != null) {
                    ppgTimeVals.clear();
                    Log.e("historydata", "his ecg hashMap=" + hashMap.toString());
                    String backVal = hashMap.toString();

                    Gson gson = new Gson();
                    HistEcgResponse response = gson.fromJson(backVal, HistEcgResponse.class);

                    for (int k = 0; k < response.getData().size(); k++) {
                        List<HistEcgResponse.HisEcgModel> dataVal = response.getData();
                        ppgTimeVals.add(dataVal.get(k).getCollectSendTime());
                    }
                    timeHandle();
                } else {

                    Log.e("historydata", "同步历史ppg no his ecg，停止历史Ecg,ppg同步");

//                    timeHandle();
                }
            }
        });
    }


    //处理时间
    private void timeHandle() {

        Log.e("historydata", "....timeHandle.....");

        if (ppgTimeVals.size() == 0 && ecgTimeVals.size() == 0) {
            //没有时间戳，退出下面逻辑判断
            uploadDoubleFile();
//            hideHeaderData();
            return;
        }

        if (ecgTimeVals.size() > 0 && ppgTimeVals.size() == 0) {
            //只有ecg,没有对应的ppg,开启删除无用的ecg
            deleteNoUseEcg(ecgTimeVals);
//            hideHeaderData();
            return;
        }

        for (int i = 0; i < ppgTimeVals.size(); i++) {
            Long ecgTimecurrent = ppgTimeVals.get(i);
            if (ecgTimeVals.contains(ecgTimecurrent)) {
                ecgUseTimeVals.add(ecgTimecurrent);
            } else {
                ppgUseTimeVals.add(ecgTimecurrent);
            }
        }

        Log.e("dataRes2", "ecgusetime=" + ecgUseTimeVals.toString());
        Log.e("dataRes2", "ppgusetime=" + ppgUseTimeVals.toString());

//        uploadTotalNum = ecgUseTimeVals.size() + ppgUseTimeVals.size();
//        handleUploadData(ecgUseTimeVals.size() + ppgUseTimeVals.size());

        //开始根据时间点上传数据
        handleDataBytime();
    }


    //开始成对数据上传
    private void handleDataBytime() {

        if (ecgUseTimeVals.size() == 0) {
//            Log.e("dataRes2", "成对的数据同步结束");
            //开始单个测试
            handleSinglePPgBytime();
            return;
        }

        //表示现在开始同步成对数据
        for (int i = 0; i < ecgUseTimeVals.size(); i++) {
            //开始同步数据
            doubleType = 0;
            beginUploadEcgData(ecgUseTimeVals.get(i));
            break;
        }
    }


    //开始同步单个的ppg文件
    private void handleSinglePPgBytime() {

        if (ppgUseTimeVals.size() == 0) {
//            Log.e("dataRes2", "所有数据同步结束");

            ecgUseTimeVals.clear();
            ppgUseTimeVals.clear();

            //开始本地文件上传操作
            uploadDoubleFile();
            return;
        }


        for (int i = 0; i < ppgUseTimeVals.size(); i++) {
//            Log.e("dataRes2", "开始单个的ppg同步");
            beginUploadPpgData(ppgUseTimeVals.get(i));

            break;
        }
    }


    //上传成对时间
    public void uploadDoubleFile() {
        List<String> doubleFileName = getDoubleFile();
        for (int i = 0; i < doubleFileName.size(); i++) {
//            Log.e("dataRes", " 开始上传 ： " + doubleFileName.get(i));
            handleDoubleFileName(doubleFileName.get(i));
            try {

                Random rand = new Random();
                int timecount = rand.nextInt(200) + 300;
                Thread.sleep(timecount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        Log.e("dataRes", "成对上传结束");
        //开始上传单个
        uploadSingleFile();
    }

    //删除无用的ecg ,这里有bug
    private void deleteNoUseEcg(List<Long> ecgNoUseTimeVals) {

        for (int i = 0; i < ecgNoUseTimeVals.size(); i++) {
            long nousertime = ecgNoUseTimeVals.get(i);
            YCBTClient.collectDeleteEcgPpg((int) nousertime, new BleDataResponse() {
                @Override
                public void onDataResponse(int i, float v, HashMap hashMap) {
//                    handleUploadData(ecgUseTimeVals.size() + ppgUseTimeVals.size());
                    handleSinglePPgBytime();
                }
            });
        }
    }


    //获取单个的ecg文件内容
    private void beginUploadEcgData(final long ecgTimeVal) {

        Log.e("dataRes2", "开始同步成对 ecg time=" + ecgTimeVal);

        YCBTClient.collectEcgDataWithTimestamp(ecgTimeVal, new BleDataResponse() {
            @Override
            public void onDataResponse(int i, float v, HashMap hashMap) {

                if (hashMap != null) {
                    byte[] tEcgData = (byte[]) hashMap.get("data");
                    save2(String.valueOf(sendTime((int) ecgTimeVal)), tEcgData);

//                    Log.e("dataRes2", TimeUtil.timeConverTimes(sendTime((int) ecgTimeVal)));

                    beginUploadDoublePpgData(ecgTimeVal);
                } else {
                    Log.e("historyEcg", "no his ecg data");
                    //没有对应的ecg,获取对应的ppg
                    beginUploadDoublePpgData(ecgTimeVal);
                }
            }
        });
    }


    private void save2(final String fileNameVal,final byte[] historyEcgDatas) {

//        ecgTempDataContain.clear();
//        ecgTempDataContain.addAll(historyEcgDatas);

        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(Environment.getExternalStorageDirectory(), "sungotwo2/double");
                if (!file.exists()) {
                    file.mkdirs();
                }
                String ecgName = fileNameVal + ".ecg200";
                File ecgFile = new File(file, ecgName);
                try {
                    ecgFile.createNewFile();
                    FileOutputStream ecgFos = new FileOutputStream(ecgFile);
                    ecgFos.write(historyEcgDatas);
                    ecgFos.flush();
                    ecgFos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
//                    uploadDoubleFile();
                }
            }
        }).start();
    }



    private void beginUploadPpgData(final long ppgTimeval) {
//        myBleService.write(ProtocolWriter.getHistoryDataWithTime(1, currentSingleFileName));
//        Log.e("dataRes", "单个ppg time=" + ppgTimeval);

        try {
            YCBTClient.collectPpgDataWithTimestamp( ppgTimeval, new BleDataResponse() {
                @Override
                public void onDataResponse(int i, float v, HashMap hashMap) {

                    if (hashMap != null) {
                        byte[] tEcgData = (byte[]) hashMap.get("data");
                        ppgUseTimeVals.remove(ppgTimeval);


                        save(String.valueOf(sendTime((int) ppgTimeval)), 1, tEcgData);

                        //等待1秒，开始删除
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                deletePPgDataByTime(ppgTimeval);
//                            }
//                        }, 500);

                        deletePPgDataByTime(ppgTimeval);

                        Log.e("historyEcg", "ppgg data" + Arrays.toString(tEcgData));
                    } else {
                        Log.e("historyEcg", "no his ppg data");
                    }
                }
            });

        } catch (Exception e) {
            Log.e("dataRes", ".....同步单个ppg数据报错....." + e.toString());
            ppgUseTimeVals.remove(ppgTimeval);
            //等待1秒，开始删除
            deletePPgDataByTime(ppgTimeval);
        }
    }


    private void deletePPgDataByTime(long deleteTime) {
        Log.e("dataRes", "单个ppg deleteTime=" + deleteTime);
        try {

            YCBTClient.collectDeleteEcgPpg((int) deleteTime, new BleDataResponse() {
                @Override
                public void onDataResponse(int i, float v, HashMap hashMap) {
//                    handleUploadData(ecgUseTimeVals.size() + ppgUseTimeVals.size());
                    handleSinglePPgBytime();
                }
            });
        } catch (Exception e) {
//            Log.e("dataRes", ".....删除单个ppg....." + e.toString());
//            handleUploadData(ecgUseTimeVals.size() + ppgUseTimeVals.size());
            handleSinglePPgBytime();
        }
    }

    //开始成对中的Ppg上传
    private void beginUploadDoublePpgData(final long ppgTimeVal) {

        Log.e("dataRes2", "开始同步成对 PPG time=" + ppgTimeVal);
        //根据时间戳同步指定时间点的数据
        try {
            YCBTClient.collectPpgDataWithTimestamp( ppgTimeVal, new BleDataResponse() {
                @Override
                public void onDataResponse(int i, float v, HashMap hashMap) {

                    if (hashMap != null) {
                        byte[] tEcgData = (byte[]) hashMap.get("data");

                        ecgUseTimeVals.remove(ppgTimeVal);
//                                    Log.e("dataRes", ".开始同步成对 PPG...size=" + listECG.size());
                        save(String.valueOf(sendTime((int) ppgTimeVal)), 0, tEcgData);

//                        Log.e("dataRes2", TimeUtil.timeConverTimes(sendTime((int) ppgTimeVal)));


                        //等待1秒，开始删除
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        }, 1000);

                        deleteEcgDataByTime(ppgTimeVal);

                        Log.e("historyEcg", "ppgg data" + Arrays.toString(tEcgData));
                    } else {
                        Log.e("historyEcg", "no his ppg data");
                    }
                }
            });
        } catch (Exception e) {
//            e.printStackTrace();
            Log.e("dataRes", ".....同步成对ppg数据报错....." + e.toString());
//            deleteEcgDataByTime(ppgTimeVal);
            ecgUseTimeVals.remove(ppgTimeVal);
            //等待1秒，开始删除
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    deleteEcgDataByTime(ppgTimeVal);
//                }
//            }, 1000);

            deleteEcgDataByTime(ppgTimeVal);
        }
    }



    private long sendTime(long stime) {
//        long stime = 5591791;
//        int millisFromGMT = TimeZone.getDefault().getOffset(System.currentTimeMillis());
//        long tStartTime2 = (stime + YCBTClient.SecFrom30Year) * 1000;
        long tStartTime2 = (stime + YCBTClient.SecFrom30Year);
        return tStartTime2;
    }


    private void deleteEcgDataByTime(long deleteTime) {

        try {
            Log.e("dataRes", "删除成对的时间点=" + deleteTime);
            YCBTClient.collectDeleteEcgPpg((int) deleteTime, new BleDataResponse() {
                @Override
                public void onDataResponse(int i, float v, HashMap hashMap) {
//                    handleUploadData(ecgUseTimeVals.size() + ppgUseTimeVals.size());
                    handleDataBytime();
                }
            });
        } catch (Exception e) {
//            Log.e("dataRes", ".....删除ecg报错....." + e.toString());
//            handleUploadData(ecgUseTimeVals.size() + ppgUseTimeVals.size());
            handleDataBytime();
        }

    }


    private List<String> getDoubleFile() {
        List<String> fileLists = new ArrayList<>();
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "sungotwo2/double");
            if (file.exists()) {
                File[] subFile = file.listFiles();

                for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                    // 判断是否为文件夹
                    if (!subFile[iFileLength].isDirectory()) {
                        String filename = subFile[iFileLength].getName();
                        if (!TextUtils.isEmpty(filename)) {
                            String aftername = filename.substring(0, filename.lastIndexOf("."));
                            fileLists.add(aftername);
                        }
                    }
                }
                HashSet hashSet = new HashSet(fileLists);
                fileLists.clear();
                fileLists.addAll(hashSet);
//                Log.e("fileeee", " doulbe 文件名 ： " + fileLists.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileLists;
    }

    private void handleDoubleFileName(String fileName) {

        File file = new File(Environment.getExternalStorageDirectory(), "sungotwo2/double");

//        Log.e("dataRes2", "fileName=" + TimeUtil.timeConverTimes(Long.parseLong(fileName)));

        String ecgFileVal = fileName + ".ecg200";
        String ppgFileVal = fileName + ".ppg200";

        File ecgFile = new File(file, ecgFileVal);
        File ppgFile = new File(file, ppgFileVal);

        if (ecgFile.exists() && ppgFile.exists()) {
//            uploadDoubleFIle(ecgFile, ppgFile);
        }
    }


    //处理单个时间
    private void handleSingleFileName(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory(), "sungotwo2/single");
        String ppgFileVal = fileName + ".ppg200";
        File ppgFile = new File(file, ppgFileVal);
        if (ppgFile.exists()) {
//            uploadSingleFIle(ppgFile);
        }
    }



    //上传单个的ppg
//    private void uploadSingleFIle(File ppgNew) {
//
////        String userid = ACache.get(MyBleServiceFive.this).getAsString(MsgNum.AC_USER_ID);
////        String tokenVal = ACache.get(MyBleServiceFive.this).getAsString(MsgNum.AC_TOKEN_OLD);
//
//        int userid = (Integer) UserSPHelper.get(MyBleServiceFive.this, "userid", 0);
//        String tokenVal = (String) UserSPHelper.get(MyBleServiceFive.this, "token", "no");
//
//        HttpParams paramsPost = new HttpParams();
//        paramsPost.put("userId", userid);
//        paramsPost.put("filePpg200", ppgNew);
//        paramsPost.put("token", tokenVal);
//        paramsPost.put("version", "2");
//        //上传无感的数据ppg
//        paramsPost.put("uploadType", "noninductive");
//        paramsPost.put("appVersion", "3.0");
//
//
//        OkGo.<String>post(Urls.BASE_URL_OLD + "/" + UrlPath.PATH_UPLOAD_PPG_FILE_URL).headers("Authorization", tokenVal)
//                .tag(MyBleServiceFive.this)
//                .params(paramsPost)
//                .isMultipart(true)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> backresponse) {
//                        if (backresponse != null) {
//                            String backVal = backresponse.body();
//                            if (backVal != null) {
//                                Gson gson = new Gson();
//                                UploadResponse response = gson.fromJson(backVal, UploadResponse.class);
//                                if (response != null) {
//                                    if (response.getCode() == 200) {
////                                        Toast.makeText(MyBleService.this, "上传成功", Toast.LENGTH_SHORT).show();
//
//                                        deleteSingleFileByTime(ppgNew);
//
//                                        //结束时，震动下
//                                    } else if (response.getStatus() == 1 && response.getMsg() != null && response.getMsg().equals("token匹配错误")) {
//                                        Toast.makeText(MyBleServiceFive.this, response.getMsg(), Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(MyBleServiceFive.this, response.getMsg(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        super.onFinish();
//                    }
//                });
//    }




    //上传单个时间
    public void uploadSingleFile() {
        List<String> singleFile = getSingleFile();
        for (int i = 0; i < singleFile.size(); i++) {
//            Log.e("dataRes", " 开始单个上传 ： " + singleFile.get(i));
            handleSingleFileName(singleFile.get(i));
            try {
                Random rand = new Random();
                int timecount = rand.nextInt(200) + 300;
                Thread.sleep(timecount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        EventBus.getDefault().post(new HistoryUploadEvent());
//        Log.e("dataRes", "单个上传结束");
    }


    private List<String> getSingleFile() {
        List<String> fileLists = new ArrayList<>();
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "sungotwo2/single");
            if (file.exists()) {
                File[] subFile = file.listFiles();

                for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                    // 判断是否为文件夹
                    if (!subFile[iFileLength].isDirectory()) {
                        String filename = subFile[iFileLength].getName();
                        if (!TextUtils.isEmpty(filename)) {
                            String aftername = filename.substring(0, filename.lastIndexOf("."));
                            fileLists.add(aftername);
                        }
//                        Log.e("fileeee", "single 文件名 ： " + filename);
                    }
                }
                HashSet hashSet = new HashSet(fileLists);
                fileLists.clear();
                fileLists.addAll(hashSet);
//                Log.e("fileeee", "single 文件名 ： " + fileLists.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileLists;
    }


    //新接口，一次上传完
//    private void uploadDoubleFIle(File ecgNew, File ppgNew) {
//
////        String userid = ACache.get(MyBleServiceFive.this).getAsString(MsgNum.AC_USER_ID);
////        String tokenVal = ACache.get(MyBleServiceFive.this).getAsString(MsgNum.AC_TOKEN_OLD);
//
//        int userid = (Integer) UserSPHelper.get(MyBleServiceFive.this, "userid", 0);
//        String tokenVal = (String) UserSPHelper.get(MyBleServiceFive.this, "token", "no");
//
//        HttpParams paramsPost = new HttpParams();
//        paramsPost.put("userId", userid);
//        paramsPost.put("filePpg200", ppgNew);
//        paramsPost.put("fileEcg200", ecgNew);
//        paramsPost.put("token", tokenVal);
//        paramsPost.put("version", "2");
//        //上传离线的数据ecg,ppg
//        paramsPost.put("uploadType", "off-line");
//        paramsPost.put("appVersion", "3.0");
//
//        OkGo.<String>post(Urls.BASE_URL_OLD + "/" + UrlPath.PATH_UPLOAD_PPG_FILE_URL).headers("Authorization", tokenVal)
//                .tag(MyBleServiceFive.this)
//                .params(paramsPost)
//                .isMultipart(true)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> backresponse) {
//                        if (backresponse != null) {
//                            String backVal = backresponse.body();
//                            if (backVal != null) {
//                                Gson gson = new Gson();
//                                UploadResponse response = gson.fromJson(backVal, UploadResponse.class);
//                                if (response != null) {
//                                    if (response.getCode() == 200) {
////                                        Toast.makeText(MyBleService.this, "上传成功", Toast.LENGTH_SHORT).show();
//
//                                        deleteDoubleFileByTime(ecgNew, ppgNew);
//
//                                        //结束时，震动下
//                                    } else if (response.getStatus() == 1 && response.getMsg() != null && response.getMsg().equals("token匹配错误")) {
//                                        Toast.makeText(MyBleServiceFive.this, response.getMsg(), Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(MyBleServiceFive.this, response.getMsg(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        super.onFinish();
//                    }
//                });
//    }



    private void save(final String fileNameVal,final int currentType,final byte[] historyPpgDatas) {

//        ppgTempDataContain.clear();
//        ppgTempDataContain.addAll(historyPpgDatas);
//        Log.e("dataRes2", "保存文件名 ppg：" + fileNameVal + ":size=" + ppgTempDataContain.size());

        new Thread(new Runnable() {
            @Override
            public void run() {

                File file;

                if (currentType == 0) {
                    file = new File(Environment.getExternalStorageDirectory(), "sungotwo2/double");
                } else {
                    file = new File(Environment.getExternalStorageDirectory(), "sungotwo2/single");
                }

                if (!file.exists()) {
                    file.mkdirs();
                }

                String ppgName = fileNameVal + ".ppg200";
                File ppgFile = new File(file, ppgName);
                try {

                    ppgFile.createNewFile();
                    FileOutputStream ppgFos = new FileOutputStream(ppgFile);

//                    for (Byte t : ppgTempDataContain) {
//                        ppgFos.write(t);
//                    }

                    ppgFos.write(historyPpgDatas);

                    ppgFos.flush();
                    ppgFos.close();

//                    ZqhLogUtils.e(TAG, "....保存单个ppg.....");

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //数据写入本地结束，调用上传
                    uploadDoubleFile();
                }
//                if (currentUpload == 0) {
//                    //0代表成对的数据同步，
//                    handleDoubleFileName(fileNameVal);
//                } else {
//                    //1代表单个的Ppg同步
//                    handleSingleFileName(fileNameVal);
//                }

            }
        }).start();
    }

}
