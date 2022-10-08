package com.example.ycblesdkdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.realsil.sdk.dfu.DfuConstants;
import com.realsil.sdk.dfu.DfuException;
import com.realsil.sdk.dfu.image.BinFactory;
import com.realsil.sdk.dfu.image.LoadParams;
import com.realsil.sdk.dfu.model.BinInfo;
import com.realsil.sdk.dfu.model.DfuConfig;
import com.realsil.sdk.dfu.model.DfuProgressInfo;
import com.realsil.sdk.dfu.model.OtaDeviceInfo;
import com.realsil.sdk.dfu.model.Throughput;
import com.realsil.sdk.dfu.utils.BaseDfuAdapter;
import com.realsil.sdk.dfu.utils.ConnectParams;
import com.realsil.sdk.dfu.utils.DfuAdapter;
import com.realsil.sdk.dfu.utils.GattDfuAdapter;

import androidx.annotation.Nullable;

public class DfuUpdateActivity extends Activity {

    GattDfuAdapter mDfuAdapter;
    OtaDeviceInfo mOtaDeviceInfo;
    TextView updateView;
    private String macVal;
    DfuConfig mDfuConfig;
    ConnectParams.Builder connectParamsBuilder;
    BinInfo mBinInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dfu_update);
        macVal = getIntent().getStringExtra("mac");
        updateView = this.findViewById(R.id.dfu_update);

        updateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dfuInit();
            }
        });

    }

    private void dfuInit(){

         connectParamsBuilder = new ConnectParams.Builder()
                .address(macVal)
                .reconnectTimes(3);

         mDfuAdapter =  GattDfuAdapter.getInstance(DfuUpdateActivity.this);
        // Initialize DfuAdapter and set callback
        mDfuAdapter.initialize(mDfuHelperCallback);

        if(true)
            return;
        //配置
         mDfuConfig = new DfuConfig();
// Mandatory, bluetooth device address
        mDfuConfig.setAddress(macVal);
// Mandatory, if you know the exactly workmode, you can set it directly.
// or you can read the otaDeviceInfo first, and then call `mDfuAdapter.getPriorityWorkMode(DfuConstants.OTA_MODE_SILENT_FUNCTION)`
// to get the priority work mode
// It's recommend to read device info first, and set the right protocol type

        if (mOtaDeviceInfo != null) {
            mDfuConfig.setProtocolType(mOtaDeviceInfo.getProtocolType());
        } else {
            mDfuConfig.setProtocolType(0);
        }

        String filepathval = Environment.getExternalStorageDirectory() + "/sungotwo2/SHW.bin";

//        sdcard file
        LoadParams.Builder builder = new LoadParams.Builder()
                .with(this)//mandatory, your context
                .setFilePath(filepathval)//mandatory, format like :"/storage/emulated/0/xxx.bin"， format
                .setFileSuffix("bin")//optional
                .setOtaDeviceInfo(mOtaDeviceInfo)//recommend
                .setIcCheckEnabled(true)//optional
                .setSectionSizeCheckEnabled(false)//optional
                .setVersionCheckEnabled(true);

        try {
            mBinInfo =  BinFactory.loadImageBinInfo(builder.build());
            Log.e("TimeSetActivity","...path......"+mBinInfo.path);
        }catch (Exception e){
            e.printStackTrace();
        }

// ============= Optional  ====================
//        mDfuConfig.setSecretKey(byte[] key);
        mDfuConfig.setBreakpointResumeEnabled(true);
        mDfuConfig.setAutomaticActiveEnabled(true);
        mDfuConfig.setBatteryCheckEnabled(false);
        mDfuConfig.setLowBatteryThreshold(30);
        mDfuConfig.setVersionCheckEnabled(false);
        mDfuConfig.setIcCheckEnabled(true);
        mDfuConfig.setSectionSizeCheckEnabled(true);
        mDfuConfig.setWaitActiveCmdAckEnabled(false);
        mDfuConfig.setFileLocation(DfuConfig.FILE_LOCATION_SDCARD);
        mDfuConfig.setFileSuffix("bin");
        mDfuConfig.setSpeedControlEnabled(false);
        mDfuConfig.setControlSpeed(0);
        mDfuConfig.setFilePath(mBinInfo.path);
// ============= Optional  ====================



        //        String filepathval = Environment.getExternalStorageDirectory() + "";



    }








    //
    BaseDfuAdapter.DfuHelperCallback mDfuHelperCallback = new BaseDfuAdapter.DfuHelperCallback() {
        @Override
        public void onStateChanged(int state) {
            super.onStateChanged(state);
            if (state == DfuAdapter.STATE_INIT_OK) {
               //初始化OK
                Log.e("TimeSetActivity","...初始化OK......");
                //开始蓝牙连接
//                mDfuAdapter.connectDevice(connectParamsBuilder.build());
                mDfuAdapter.connectDevice(mDfuConfig.getAddress());
            }if (state == DfuAdapter.STATE_PREPARED) {
                // indidates connection established and sync data complete, you can get the device info like below:
                // var mOtaDeviceInfo = mDfuAdapter.otaDeviceInfo
                // TODO ...
                Log.e("TimeSetActivity","...STATE_PREPARED......");
                mDfuConfig.setOtaWorkMode(DfuConstants.OTA_MODE_NORMAL_FUNCTION);
//                mDfuConfig.setOtaWorkMode(DfuConstants.OTA_MODE_SILENT_FUNCTION);

                boolean ret = mDfuAdapter.startOtaProcedure(mDfuConfig);
                if (!ret) {
                    // TODO ...
                    //打开失败
                    Log.e("TimeSetActivity","...打开失败......");
                }else {
                    Log.e("TimeSetActivity","...打开成功......");
                }

            } else if (state == DfuAdapter.STATE_DISCONNECTED || state == DfuAdapter.STATE_CONNECT_FAILED) {
                // indicates connection disconnected
                // TODO ...
                Log.e("TimeSetActivity","...connection disconnected......");
            } else {
            }
        }

        @Override
        public void onProcessStateChanged(int state, Throughput throughput) {
            super.onProcessStateChanged(state, throughput);
            if (state == DfuConstants.PROGRESS_IMAGE_ACTIVE_SUCCESS) {
                // OTA procedure complete and success
                // TODO ...

                Log.e("TimeSetActivity","...dfu..update..success......");
            } else if (state == DfuConstants.PROGRESS_PENDING_ACTIVE_IMAGE) {
                // pending to active image
                // TODO ...

            } else if (state == DfuConstants.PROGRESS_STARTED) {
                // start to ota
                // TODO ...
                Log.e("TimeSetActivity","...progress......3");
            } else if (state == DfuConstants.PROGRESS_START_DFU_PROCESS) {
                // ota processing
                // TODO ...
                Log.e("TimeSetActivity","...progress......2");
            } else {
                Log.e("TimeSetActivity","...progress......1");
            }

        }

        @Override
        public void onProgressChanged(DfuProgressInfo dfuProgressInfo) {
            super.onProgressChanged(dfuProgressInfo);

            int mProcessState = 0;
            if (mProcessState == DfuConstants.PROGRESS_START_DFU_PROCESS && dfuProgressInfo != null) {
                // refresh UI with the dfuProgressInfo
                // TODO ...
                //
                Log.e("TimeSetActivity","...progress......"+dfuProgressInfo.getProgress());
            }else {
                Log.e("TimeSetActivity","...progress......33="+dfuProgressInfo.getProgress());
            }
//            Log.e("TimeSetActivity","...progress......44");
        }




        @Override
        public void onError(int type, int code) {
            super.onError(type, code);
            String message = DfuHelperImpl.parseError(DfuUpdateActivity.this, type, code);
            if (type == DfuException.Type.CONNECTION) {
                Log.e("TimeSetActivity","...DfuException.Type.CONNECTION......");
            } else if (type == DfuException.Type.OTA) {
                Log.e("TimeSetActivity","...DfuException.Type.OTA......");
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDfuAdapter != null) {
            mDfuAdapter.abort();
            mDfuAdapter.close();
        }
    }
}
