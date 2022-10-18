package com.example.ycblesdkdemo.phone;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.yucheng.ycbtsdk.response.BleDataResponse;
import com.yucheng.ycbtsdk.YCBTClient;

import java.util.HashMap;

public class PhoneReceiver extends BroadcastReceiver {
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {//短信
        } else if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {// 如果是去电（拨出）
        } else {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            sendPhoneState(context, incomingNumber);
        }
    }

    private void sendPhoneState(Context context, String incomingNumber) {
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        switch (tManager.getCallState()) {
            case TelephonyManager.CALL_STATE_RINGING:
                //实时测试的时候，禁止操作
//                if (HealthApplication.isSyncing) {
//                    return;
//                }


                //判断是否允许通知
//                List<Integer> openList = Tools.readNoti(context);
//                if (openList.get(1) == 0) {
//                    return;
//                }
//                if (openList.get(0) == 0) {
//                    return;
//                }



                try {
                    if (incomingNumber == null)
                        return;
                    String name = PhoneUtil.getContactNameFromPhoneNum(context, incomingNumber);

                    Log.e("incidentName","phonename="+name);

                    YCBTClient.appSengMessageToDevice(0x00, name, incomingNumber, new BleDataResponse() {
                        @Override
                        public void onDataResponse(int code, float ratio, HashMap resultMap) {

                        }
                    });

                    Intent intent = new Intent("com.health.communication.SENDMSG");
                    if (name != null && !name.equals(incomingNumber)) {
                        intent.putExtra("pushname", name);
                        intent.putExtra("pushmsg", incomingNumber);
                    } else {
                        intent.putExtra("pushname", incomingNumber);
                        intent.putExtra("pushmsg", " ");
                    }
                    intent.putExtra("type", 6);

                    //如果没有ecg测试的时候，发推送
                    context.sendBroadcast(intent);

//                    if (!HealthApplication.isStartECG) {
//                        context.sendBroadcast(intent);
//                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK: // 挂电话
            case TelephonyManager.CALL_STATE_IDLE:  // 接电话
            default:
                break;
        }
    }


}
