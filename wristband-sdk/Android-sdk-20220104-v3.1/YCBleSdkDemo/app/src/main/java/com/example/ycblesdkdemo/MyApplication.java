package com.example.ycblesdkdemo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.ycblesdkdemo.model.ConnectEvent;
import com.realsil.sdk.core.RtkConfigure;
import com.realsil.sdk.core.RtkCore;
import com.realsil.sdk.dfu.RtkDfu;
import com.yucheng.ycbtsdk.YCBTClient;
import com.yucheng.ycbtsdk.response.BleConnectResponse;
import com.yucheng.ycbtsdk.response.BleDeviceToAppDataResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

public class MyApplication extends Application {

    private static MyApplication instance = null;


    public static MyApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        String currentProcessName = getCurProcessName(this);

        if ("com.example.ycblesdkdemo".equals(currentProcessName)) {

            Log.e("device", "...onCreate.....");

            instance = this;
            YCBTClient.initClient(this, true);
            YCBTClient.registerBleStateChange(bleConnectResponse);
            YCBTClient.deviceToApp(toAppDataResponse);


            //固件升级初始化
            RtkConfigure configure = new RtkConfigure.Builder()
                    .debugEnabled(true)
                    .printLog(true)
                    .logTag("OTA")
                    .build();
            RtkCore.initialize(this, configure);
            RtkDfu.initialize(this, true);


        }
    }


//    BleConnectResponse bleConnectResponse = new BleConnectResponse() {
//        @Override
//        public void onConnectResponse(int var1) {
//
//            //Toast.makeText(MyApplication.this, "i222=" + var1, Toast.LENGTH_SHORT).show();
//
//            Log.e("device","...connect..state....." + var1);
//
//            if(var1 == 0){
//                EventBus.getDefault().post(new ConnectEvent());
//            }
//        }
//    };


    BleDeviceToAppDataResponse toAppDataResponse = new BleDeviceToAppDataResponse() {

        @Override
        public void onDataResponse(int dataType, HashMap dataMap) {

            Log.e("TimeSetActivity", "被动回传数据。。。");
            Log.e("TimeSetActivity", dataMap.toString());

        }
    };


    boolean isActiveDisconnect = false;
    BleConnectResponse bleConnectResponse = new BleConnectResponse() {
        @Override
        public void onConnectResponse(int code) {
//            Toast.makeText(MyApplication.this, "i222=" + var1, Toast.LENGTH_SHORT).show();

            Log.e("deviceconnect", "全局监听返回=" + code);

            if (code == com.yucheng.ycbtsdk.Constants.BLEState.Disconnect) {
//                thirdConnect = false;
//                BangleUtil.getInstance().SDK_VERSIONS = -1;
//                EventBus.getDefault().post(new BlueConnectFailEvent());
                /*if(SPUtil.getBindedDeviceMac() != null && !"".equals(SPUtil.getBindedDeviceMac())){
                    YCBTClient.connectBle(SPUtil.getBindedDeviceMac(), new BleConnectResponse() {
                        @Override
                        public void onConnectResponse(int code) {

                        }
                    });
                }*/
            } else if (code == com.yucheng.ycbtsdk.Constants.BLEState.Connected) {

            } else if (code == com.yucheng.ycbtsdk.Constants.BLEState.ReadWriteOK) {


//                thirdConnect = true;
//                BangleUtil.getInstance().SDK_VERSIONS = 3;
//                Log.e("deviceconnect", "蓝牙连接成功，全局监听");
//                setBaseOrder();
                EventBus.getDefault().post(new ConnectEvent());
            } else {
                //code == Constants.BLEState.Disconnect
//                thirdConnect = false;
//                BangleUtil.getInstance().SDK_VERSIONS = -1;
//                EventBus.getDefault().post(new ConnectEvent());
            }
        }
    };


    /**
     * 获得当前进程名
     *
     * @param context
     * @return
     */
    private String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }


}
