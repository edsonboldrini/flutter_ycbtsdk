package com.example.ycblesdkdemo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.internal.telephony.ITelephony;
import com.example.ycblesdkdemo.util.HangUpTelephonyUtil;

import java.lang.reflect.Method;

public class BackService extends Service {

    private static final int TIMER = 999;

    private Handler mHanler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIMER:
                    //去执行定时操作逻辑
                    Log.e("incidentName","..here call hang up.....");
                    HangUpTelephonyUtil.endCall(BackService.this);
                    HangUpTelephonyUtil.killCall(BackService.this);

                    try {
                        Method method = Class.forName("android.os.ServiceManager")
                                .getMethod("getService", String.class);
                        IBinder binder = (IBinder) method.invoke(null, new Object[]{Context.TELEPHONY_SERVICE});
                        ITelephony telephony = ITelephony.Stub.asInterface(binder);
                        telephony.endCall();
                    } catch (NoSuchMethodException e) {
                        Log.d("incidentName", "", e);
                    } catch (ClassNotFoundException e) {
                        Log.d("incidentName", "", e);
                    } catch (Exception e) {
                    }

//                    mHanler.sendEmptyMessage(TIMER);
                    mHanler.sendEmptyMessageDelayed(TIMER,5000);
                    break;
                default:
                    break;
            }
        }
    };



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mHanler.sendEmptyMessage(TIMER);
    }



}
