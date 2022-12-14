package com.example.ycblesdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yucheng.ycbtsdk.Constants;
import com.yucheng.ycbtsdk.YCBTClient;
import com.yucheng.ycbtsdk.response.BleDataResponse;
import com.yucheng.ycbtsdk.response.BleRealDataResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeSetActivity extends Activity {

    public static String Tag = TimeSetActivity.class.getName();


    private TextView startTestView;

    private TextView jianceModeSetView;
    private TextView setWenShiView;
    private TextView setHuangjingguangView;
    private TextView wenshiView;
    private TextView huanjingguangView;
    private TextView xueyangView;

    private TextView getXueyangValView;

    private TextView tuoluojiluView;

    private TextView addRCView;
    private TextView updateRCView;
    private TextView getRCView;
    private TextView delRCView;


    private TextView rockView;
    private TextView mobileTypeView;

    private TextView weatherView;
    private TextView callPhoneView;

    private TextView findPhoneView;

    private TextView richengSetView;

    private TextView yiwaiView;

    private TextView setLiangDuView;

    private TextView xipingView;

    private TextView taiwanLiangView;

    private TextView wuraoSetView;

    private TextView guanjiView;
    private TextView chognqiView;
    private TextView huifuchuchangView;

    private TextView setModeView;

    private TextView getModeView;

    private TextView setDataView;

    private TextView huanjingGuangSetView;

    private TextView wenshiduSetView;

    private TextView tiwenView;

    private TextView setGoalView;

    private TextView baseHrView;

    private TextView sportNoticeView;

    private TextView appSendMsgView;

    private TextView sportDataView;

    private TextView sleepDataView;

    private TextView tiwenNoticeView;

    private TextView setBiaoPanView;

    private TextView getBiaoPanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        initView();
    }

    private void initView() {

        startTestView = (TextView) this.findViewById(R.id.time_start_test_view);

        setWenShiView = (TextView) this.findViewById(R.id.set_wenshi_view);
        setHuangjingguangView = (TextView) this.findViewById(R.id.set_huanjingguang_view);
        wenshiView = (TextView) this.findViewById(R.id.get_wenshi_view);
        huanjingguangView = (TextView) this.findViewById(R.id.get_huanjingguang_view);
        jianceModeSetView = (TextView) this.findViewById(R.id.set_jiance_view);
        xueyangView = (TextView) this.findViewById(R.id.get_xueyang_view);
        getXueyangValView = (TextView) this.findViewById(R.id.get_xueyangval_view);
        tuoluojiluView = (TextView) this.findViewById(R.id.get_tuoluojilu_view);

        addRCView = (TextView) this.findViewById(R.id.get_add_richeng_view);
        updateRCView = (TextView) this.findViewById(R.id.get_udpate_richeng_view);
        getRCView = (TextView) this.findViewById(R.id.get_get_richeng_view);
        delRCView = (TextView) this.findViewById(R.id.get_delete_richeng_view);

        rockView = (TextView) this.findViewById(R.id.get_qinyou_rock_view);

        mobileTypeView = (TextView) this.findViewById(R.id.get_send_mobile_type_view);

        weatherView = (TextView) this.findViewById(R.id.get_send_weather_view);

        callPhoneView = (TextView) this.findViewById(R.id.get_call_phone_view);

        findPhoneView = (TextView) this.findViewById(R.id.find_phone_view);

        richengSetView = (TextView) this.findViewById(R.id.set_richeng_order_view);

        yiwaiView = (TextView) this.findViewById(R.id.yiwai_jiance_view);


        setLiangDuView = (TextView) this.findViewById(R.id.liangdu_set_view);

        xipingView = (TextView) this.findViewById(R.id.xiping_set_view);

        taiwanLiangView = (TextView) this.findViewById(R.id.taiwan_liangping_view);

        wuraoSetView = (TextView) this.findViewById(R.id.wurao_set_view);

        guanjiView = (TextView) this.findViewById(R.id.guanji_view);
        chognqiView = (TextView) this.findViewById(R.id.reset_view);
        huifuchuchangView = (TextView) this.findViewById(R.id.huifuchuchang_view);

        setModeView = (TextView) this.findViewById(R.id.set_mode_view);
        getModeView = (TextView) this.findViewById(R.id.get_mode_view);
        setDataView = (TextView) this.findViewById(R.id.set_data_val_view);

        huanjingGuangSetView = (TextView) this.findViewById(R.id.huanjing_guang_set_view);
        wenshiduSetView = (TextView) this.findViewById(R.id.wenshidu_set_view);
        tiwenView = (TextView) this.findViewById(R.id.tiwen_set_view);
        setGoalView = (TextView) this.findViewById(R.id.set_sport_gorable);
        baseHrView = (TextView) this.findViewById(R.id.base_hr_data);
        sportNoticeView = (TextView) this.findViewById(R.id.sport_notice_view);
        appSendMsgView = (TextView) this.findViewById(R.id.app_send_msg_view);
        sportDataView = (TextView) this.findViewById(R.id.sport_data_view);

        sleepDataView = (TextView) this.findViewById(R.id.sleep_data_view);

        tiwenNoticeView = (TextView) this.findViewById(R.id.tiwen_data_view);

        setBiaoPanView = (TextView) this.findViewById(R.id.set_biaopan_view);

        getBiaoPanView = (TextView) this.findViewById(R.id.get_biaopan_view);

        //??????90?????????
        startTestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpenBloodOrder();
            }
        });


        //???????????????????????????????????????????????????????????????
        //????????????????????????
        setWenShiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingAmbientTemperatureAndHumidity(true, 10, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e(Tag, "?????????????????????......");
                    }
                });
            }
        });


        //???????????????
        setHuangjingguangView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingAmbientLight(true, 10, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e(Tag, "?????????????????????......");
                    }
                });
            }
        });


        wenshiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.healthHistoryData(0x051C, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e(Tag, "?????????????????????......" + resultMap.toString());
                    }
                });
            }
        });

        huanjingguangView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.healthHistoryData(0x0520, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e(Tag, "?????????????????????......" + resultMap.toString());
                    }
                });
            }
        });


        //??????????????????????????????????????????
        jianceModeSetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appAmbientLightMeasurementControl(0x02, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e(Tag, "??????????????????????????????......");
                    }
                });
            }
        });

        //????????????
        xueyangView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingBloodOxygenModeMonitor(true, 10, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
//                        Log.e(Tag, "??????????????????????????????......"+resultMap.toString());
                        Log.e(Tag, "??????????????????????????????......code=" + code);
                    }
                });
            }
        });


        getXueyangValView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.healthHistoryData(0x051A, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
//                        Log.e(Tag, "?????????????????????????????????......"+resultMap.toString());
                        if (resultMap != null) {
                            ArrayList lists = (ArrayList) resultMap.get("data");

                            Log.e(Tag, "?????????????????????????????????......");
//                            for (HashMap map : lists) {
//
//                            }
                        } else {
                            Log.e(Tag, "???????????????????????????......");
                        }

                    }
                });
            }
        });


        tuoluojiluView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.healthHistoryData(0x0529, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e(Tag, "??????????????????????????????......" + resultMap.toString());
                    }
                });
            }
        });


//        /**
//         * ??????????????????
//         *
//         * @param type           0x00:????????????  0x01:????????????  0x02:????????????
//         * @param scheduleIndex  ???????????? 1-20 (?????????????????????1?????????????????????20)
//         * @param scheduleEnable 0x00????????? 0x01?????????
//         * @param eventIndex     ??????????????? ?????? 1-20 (??????????????????????????????????????????????????????1???20?????????????????????????????????)[????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????]
//         * @param eventEnable    0x00????????? 0x01?????????
//         * @param time           ????????????????????? ?????????yyyy-MM-dd HH:mm:ss
//         * @param eventType      ????????????  0x00 ?????? 0x01 ?????? 0x02 ????????? 0x03 ?????? 0x04 ?????? 0x05 ?????? 0x06 ?????? 0x07 ?????? 0x08 ?????????
//         * @param content        ???????????????????????????  (????????????????????????????????????????????????(??????????????????????????????24byte)????????????null)
//         * @param dataResponse
//         */
//        public static void settingScheduleModification(int type, int scheduleIndex, int scheduleEnable, int eventIndex, int eventEnable, String time, int eventType, String content, BleDataResponse dataResponse) {
        addRCView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingScheduleModification(0x01, 2, 0x01, 7, 0x01, "2020-08-29 17:18:00", 0x00, null, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {

                    }
                });

//                YCBTClient.settingScheduleModification(0x01, 1, 0x01, 2, 0x01, "2020-07-22 09:00:00", 0x01, null, new BleDataResponse() {
//                    @Override
//                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
//
//                    }
//                });
            }
        });


        //??????????????????????????????  ???????????????????????????????????????????????????????????????
        updateRCView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingScheduleModification(0x00, 3, 0x01, 2, 0x01, "2020-09-22 06:00:00", 0x00, null, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {

                    }
                });
            }
        });

        getRCView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent rcIntent = new Intent(TimeSetActivity.this, RCActivity.class);
                startActivity(rcIntent);

            }
        });


        //???????????????????????????????????????
        delRCView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingScheduleModification(0x02, 2, 0x01, 1, 0x01, "2020-07-22 06:00:00", 0x00, null, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {

                    }
                });
            }
        });


        //??????0-4
        rockView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appEmoticonIndex(4, 9, 40, "????????????1", new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {

                    }
                });
            }
        });


        mobileTypeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appMobileModel("huawei40", new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e(Tag, "??????????????????.....");
                        Log.e(Tag, resultMap.toString());
                    }
                });
            }
        });


        //?????? ????????????
        //0-6??????????????? ????????????
        //7 ??????
        //8 ??????
        //9 ?????????
        //10 ??????
        //11 ??????
        //12 ??????
        //13 ??????
        //14 ??????
        //15 ??????
        //16 ??????
        //17 ??????
        //18 ???
        //19 ???
        //20 ???
        //0 ??????
        weatherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appTodayWeather("20", "40", "32", 8, null, "4", null, 7, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {

                    }
                });
            }
        });


        //????????????
        callPhoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appSengMessageToDevice(0x00, "??????", "1992325392", new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {

                    }
                });
            }
        });


        //????????????
        richengSetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingScheduleSwitch(true, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "code=" + code);
                    }
                });
            }
        });


        //??????????????????
        yiwaiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingAccidentMode(true, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "code=" + code);
                    }
                });
            }
        });


        //??????????????????
        setLiangDuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingDisplayBrightness(0, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "code=" + code);
                    }
                });
            }
        });

        //??????????????????
        xipingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingScreenTime(3, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "code=" + code);
                    }
                });
            }
        });

        //????????????
        taiwanLiangView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingRaiseScreen(1, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "code=" + code);
                    }
                });
            }
        });

        //????????????
        wuraoSetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingNotDisturb(1, 6, 30, 16, 30, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "code=" + code);
                    }
                });
            }
        });


//        guanjiView = (TextView)this.findViewById(R.id.guanji_view);
//        chognqiView = (TextView)this.findViewById(R.id.reset_view);
//        huifuchuchangView = (TextView)this.findViewById(R.id.huifuchuchang_view);

        //??????
        guanjiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appShutDown(1, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "code=" + code);
                    }
                });
            }
        });

        //??????
        chognqiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appShutDown(3, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "code=" + code);
                    }
                });
            }
        });

        //????????????
        huifuchuchangView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingRestoreFactory(new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "code=" + code);
                    }
                });
            }
        });


        //??????????????????   0x00?????????????????????????????? 0x01: ??????????????????????????? 0x02?????????????????????????????? 0x03: ??????????????????????????????
        setModeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingWorkingMode(3, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "????????????code=" + code);
                    }
                });
            }
        });


        //??????????????????????????????
        getModeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.getCurrentSystemWorkingMode(new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "????????????" + resultMap.toString());
                    }
                });
            }
        });


        //??????????????????
        setDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingUploadReminder(true, 80, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "??????code=" + code);
                    }
                });
            }
        });


        /***
         * ??????????????????
         * @param on 0x01: ?????? 0x00: ??????
         * @param type 0x00: PPG 0x01: ??????????????? 0x02???ECG 0x03???????????? 0x04???????????? 0x05?????????
         * @param collectLong ??????????????????(??????:???) (???????????? 0)
         * @param collectInterval ????????????(??????:??????) (???????????? 0)
         * @param dataResponse
         */
        huanjingGuangSetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingDataCollect(1, 0x04, 90, 60, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "?????????code=" + code);
                    }
                });
            }
        });

        /***
         * ??????????????????
         * @param on 0x01: ?????? 0x00: ??????
         * @param type 0x00: PPG 0x01: ??????????????? 0x02???ECG 0x03???????????? 0x04???????????? 0x05?????????
         * @param collectLong ??????????????????(??????:???) (???????????? 0)
         * @param collectInterval ????????????(??????:??????) (???????????? 0)
         * @param dataResponse
         */
        wenshiduSetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingDataCollect(1, 0x03, 90, 60, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "?????????code=" + code);
                    }
                });
            }
        });

        /***
         * ??????????????????
         * @param on 0x01: ?????? 0x00: ??????
         * @param type 0x00: PPG 0x01: ??????????????? 0x02???ECG 0x03???????????? 0x04???????????? 0x05?????????
         * @param collectLong ??????????????????(??????:???) (???????????? 0)
         * @param collectInterval ????????????(??????:??????) (???????????? 0)
         * @param dataResponse
         */
        tiwenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingDataCollect(1, 0x05, 90, 60, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "??????code=" + code);
                    }
                });
            }
        });

        //????????????
        setGoalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingGoal(0x00, 500, 0, 0, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "??????code=" + code);
                    }
                });
            }
        });


        baseHrView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appEffectiveHeart(80, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "??????code=" + code);
                    }
                });
            }
        });

        //????????????
        sportNoticeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appEarlyWarning(3, null, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "????????????code=" + code);
                    }
                });
            }
        });

        /**
         * APP ????????????
         *
         * @param type ??????    0x00	??????????????????????????????APP????????????
         *                     0x01	??????????????????????????????APP????????????
         *                     0x02	???????????????????????????APP????????????
         *                     0x03	???????????????????????????????????????
         *                     0x04	???????????????????????????
         *                     0x05	????????????????????????????????????????????????
         *                     0x06	???????????????
         * @param message      ????????????  ?????????0x06???,??????????????????
         * @param dataResponse 0x00: ?????????????????? 0x01?????????????????????
         */
        appSendMsgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appPushMessage(1, null, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "app????????????=" + code);
                    }
                });
            }
        });

        /**
         * APP ??????????????????
         * ??????????????????
         * @param step         ????????????
         * @param type         ???????????? 0x00:???????????? 0x01:???????????? 0x02:???????????? 0x03:???????????? 0x04:???????????? 0x05:?????????
         * @param dataResponse 0x00: ?????????????????? 0x01?????????????????????
         */
        sportDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appEffectiveStep(10000, 1, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "????????????=" + code);
                    }
                });
            }
        });


/**
 * APP ???????????????????????????
 *
 * @param deepSleepTimeHour  ??????(??????)
 * @param deepSleepTimeMin   ??????(??????)
 * @param lightSleepTimeHour ??????(??????)
 * @param lightSleepTimeMin  ??????(??????)
 * @param totalSleepTimeHour ?????????(??????)
 * @param totalSleepTimeMin  ?????????(??????)
 * @param dataResponse       0x00: ?????????????????? 0x01: ????????????-????????????
 */
        sleepDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appSleepWriteBack(3, 40, 2, 20, 6, 0, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "????????????=" + code);
                    }
                });
            }
        });


        /**
         * ????????????  ?????????????????????, ????????????????????????
         *????????????
         * @param on_off         ??????????????????
         * @param maxTempInteger ????????????????????????(36 -- 100)
         * @param minTempInteger ????????????????????????(-127 -- 36)
         * @param maxTempFloat   ????????????????????????(1 -- 9)
         * @param minTempFloat   ????????????????????????(1 -- 9)
         * @param dataResponse
         */
        tiwenNoticeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.newSettingTemperatureAlarm(true, 37, 33, 3, 3, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "????????????=" + code);
                    }
                });
            }
        });


        //WATCHFACE -> 1
        setBiaoPanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingMainTheme(3, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "????????????code=" + code);
                    }
                });
            }
        });

        getBiaoPanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.getThemeInfo(new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "????????????=" + resultMap.toString());
                    }
                });
            }
        });
    }


    private void isOpenBloodOrder() {
        Log.e("historyorder", "????????????,???????????????????????????");
        YCBTClient.appEcgTestStart(new BleDataResponse() {
            @Override
            public void onDataResponse(int i, float v, HashMap hashMap) {

                try {
                    if (i == Constants.CODE.Code_OK) {
                        Log.e("historyorder", "???????????? ????????????=" + i);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                beginAnnation();
//                            }
//                        });

                    } else {
                        Log.e("historyorder", "???????????? ?????????=" + i);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new BleRealDataResponse() {
            @Override
            public void onRealDataResponse(int dataType, HashMap hashMap) {
//                        Log.e("historytest", "???????????? ????????????=" + hashMap.toString());
                if (dataType == Constants.DATATYPE.Real_UploadHeart) {
                    //???????????? dataMap
                    try {
                        if (hashMap != null) {
                            String backVal = hashMap.toString();
                            JSONObject jsonObject = new JSONObject(backVal);
                            String hrVal = jsonObject.getString("heartValue");
                            Log.e("historyorhr", "???????????? ??????=" + hrVal);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (dataType == Constants.DATATYPE.Real_UploadBlood) {
                    //???????????? dataMap
                    try {
                        if (hashMap != null) {
                            String backVal = hashMap.toString();
                            JSONObject jsonObject = new JSONObject(backVal);
                            String highVal = jsonObject.getString("bloodSBP");
                            String lowVal = jsonObject.getString("bloodDBP");
                            String heartRate = jsonObject.getString("heartValue");

                            Log.e("historyorhr", "highval=" + highVal);
                            Log.e("historyorhr", "lowval=" + lowVal);
                            Log.e("historyorhr", "heartrate=" + heartRate);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (dataType == Constants.DATATYPE.Real_UploadPPG) {

                    Log.e("historyorhr", "???????????? ppg??????");

                    //??????PPG??????  dataMap
                    byte[] ppgBytes = (byte[]) hashMap.get("data");
                    for (byte ecgbyte : ppgBytes) {
                    }
                } else if (dataType == Constants.DATATYPE.Real_UploadECG) {

//                    if (beginReceiveData == 0) {
//                        beginReceiveData = 1;
//                        //??????????????????????????????????????????
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mHandlerTwo.removeMessages(MSG_CODE_TWO);
//                                openBloodTestSuccess();
//                            }
//                        });
//                    }
//                    Log.e("historyorhr", "???????????? ecg??????");
//
//                    //??????ECG??????  dataMap
//                    byte[] ecgBytes = (byte[]) hashMap.get("data");
//                    for (byte ecgbyte : ecgBytes) {
//                        ecgVals.add(ecgbyte);
//                    }
                } else if (dataType == Constants.DATATYPE.AppECGPPGStatus) {
                    int EcgStatus = (int) hashMap.get("EcgStatus");
                    int PPGStatus = (int) hashMap.get("PPGStatus");
                    if (EcgStatus == 0) {
                        Log.e("status", "ecg??????????????????");
                    } else if (EcgStatus == 1) {
                        Log.e("status", "??????????????????");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    } else if (EcgStatus == 2) {
                        Log.e("status", "????????????");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }

                    if (PPGStatus == 0) {
                        Log.e("status", "ppg??????????????????");
                    } else if (PPGStatus == 1) {
                        Log.e("status", "????????????");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    } else if (PPGStatus == 2) {
                        Log.e("status", "????????????");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }

                }
            }
        });

    }


}
