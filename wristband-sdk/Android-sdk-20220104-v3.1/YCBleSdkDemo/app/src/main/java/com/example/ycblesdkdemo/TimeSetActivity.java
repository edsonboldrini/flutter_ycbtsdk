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

        //开始90秒测试
        startTestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpenBloodOrder();
            }
        });


        //开启温湿度，环境光，之前需要先设置检测模式
        //温湿度，模式设置
        setWenShiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingAmbientTemperatureAndHumidity(true, 10, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e(Tag, "设置温湿度回调......");
                    }
                });
            }
        });


        //设置环境光
        setHuangjingguangView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingAmbientLight(true, 10, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e(Tag, "设置环境光回调......");
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
                        Log.e(Tag, "温湿度。。。。......" + resultMap.toString());
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
                        Log.e(Tag, "环境光。。。。......" + resultMap.toString());
                    }
                });
            }
        });


        //开启检测模式，或者单次测模式
        jianceModeSetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appAmbientLightMeasurementControl(0x02, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e(Tag, "检测模式回调。。。。......");
                    }
                });
            }
        });

        //获取血氧
        xueyangView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingBloodOxygenModeMonitor(true, 10, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
//                        Log.e(Tag, "设置血氧回调。。。。......"+resultMap.toString());
                        Log.e(Tag, "设置血氧回调。。。。......code=" + code);
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
//                        Log.e(Tag, "获取血氧值回调。。。。......"+resultMap.toString());
                        if (resultMap != null) {
                            ArrayList lists = (ArrayList) resultMap.get("data");

                            Log.e(Tag, "获取血氧值回调。。。。......");
//                            for (HashMap map : lists) {
//
//                            }
                        } else {
                            Log.e(Tag, "血氧值未空。。。。......");
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
                        Log.e(Tag, "获取脱落记录。。。。......" + resultMap.toString());
                    }
                });
            }
        });


//        /**
//         * 日程修改设置
//         *
//         * @param type           0x00:修改日程  0x01:增加日程  0x02:删除日程
//         * @param scheduleIndex  日程索引 1-20 (添加一次索引加1，最大不能超过20)
//         * @param scheduleEnable 0x00：禁止 0x01：使能
//         * @param eventIndex     修改的事件 索引 1-20 (一个日程下有多个事件，索引取值范围为1到20，每个事件索引不能重复)[支持多个事件，同样的指令调用多次，只是事件类型和时间不一样，下面几个参数变动即可]
//         * @param eventEnable    0x00：禁止 0x01：使能
//         * @param time           修改事件的时间 格式为yyyy-MM-dd HH:mm:ss
//         * @param eventType      事件类型  0x00 起床 0x01 早饭 0x02 晒太阳 0x03 午饭 0x04 午休 0x05 运动 0x06 晚饭 0x07 睡觉 0x08 自定义
//         * @param content        修改事件类型的名称  (必须自定义的时候，才能传事件名称(字符串长度，不能大于24byte)，否则传null)
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


        //修改的时候，只能更改  事件类型，时间戳，（索引不能动，否则报错）
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


        //删除的时候，传两个索引即可
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


        //索引0-4
        rockView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appEmoticonIndex(4, 9, 40, "俺是大宝1", new BleDataResponse() {
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
                        Log.e(Tag, "蓝牙交互回调.....");
                        Log.e(Tag, resultMap.toString());
                    }
                });
            }
        });


        //编号 天气类型
        //0-6为公司保留 公司保留
        //7 晴天
        //8 多云
        //9 雷阵雨
        //10 小雨
        //11 中雨
        //12 大雨
        //13 雨雪
        //14 小雪
        //15 中雪
        //16 大雪
        //17 浮沉
        //18 雾
        //19 霾
        //20 风
        //0 未知
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


        //来电提醒
        callPhoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appSengMessageToDevice(0x00, "曾工", "1992325392", new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {

                    }
                });
            }
        });


        //日程开关
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


        //意外监测开关
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


        //设置屏幕亮度
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

        //息屏时间设置
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

        //抬腕亮屏
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

        //勿扰设置
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

        //关机
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

        //重启
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

        //恢复出厂
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


        //设置工作模式   0x00：设置为正常工作模式 0x01: 设置为关怀工作模式 0x02：设置为省电工作模式 0x03: 设置为自定义工作模式
        setModeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingWorkingMode(3, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "工作模式code=" + code);
                    }
                });
            }
        });


        //获取当前系统工作模式
        getModeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.getCurrentSystemWorkingMode(new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "工作模式" + resultMap.toString());
                    }
                });
            }
        });


        //设置数据阈值
        setDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingUploadReminder(true, 80, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "阈值code=" + code);
                    }
                });
            }
        });


        /***
         * 数据采集配置
         * @param on 0x01: 开启 0x00: 关闭
         * @param type 0x00: PPG 0x01: 加速度数据 0x02：ECG 0x03：温湿度 0x04：环境光 0x05：体温
         * @param collectLong 每次采集时长(单位:秒) (关闭时填 0)
         * @param collectInterval 采集间隔(单位:分钟) (关闭时填 0)
         * @param dataResponse
         */
        huanjingGuangSetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingDataCollect(1, 0x04, 90, 60, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "环境光code=" + code);
                    }
                });
            }
        });

        /***
         * 数据采集配置
         * @param on 0x01: 开启 0x00: 关闭
         * @param type 0x00: PPG 0x01: 加速度数据 0x02：ECG 0x03：温湿度 0x04：环境光 0x05：体温
         * @param collectLong 每次采集时长(单位:秒) (关闭时填 0)
         * @param collectInterval 采集间隔(单位:分钟) (关闭时填 0)
         * @param dataResponse
         */
        wenshiduSetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingDataCollect(1, 0x03, 90, 60, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "温湿度code=" + code);
                    }
                });
            }
        });

        /***
         * 数据采集配置
         * @param on 0x01: 开启 0x00: 关闭
         * @param type 0x00: PPG 0x01: 加速度数据 0x02：ECG 0x03：温湿度 0x04：环境光 0x05：体温
         * @param collectLong 每次采集时长(单位:秒) (关闭时填 0)
         * @param collectInterval 采集间隔(单位:分钟) (关闭时填 0)
         * @param dataResponse
         */
        tiwenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingDataCollect(1, 0x05, 90, 60, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "体温code=" + code);
                    }
                });
            }
        });

        //运动目标
        setGoalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.settingGoal(0x00, 500, 0, 0, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "目标code=" + code);
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
                        Log.e("TimeSetActivity", "心率code=" + code);
                    }
                });
            }
        });

        //运动风险
        sportNoticeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appEarlyWarning(3, null, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "运动风险code=" + code);
                    }
                });
            }
        });

        /**
         * APP 信息推送
         *
         * @param type 类型    0x00	有新的周报生成，请到APP上查看。
         *                     0x01	有新的月报生成，请到APP上查看。
         *                     0x02	收到亲友信息，请到APP上查看。
         *                     0x03	很久没测量了，测量一下吧。
         *                     0x04	您已成功预约咨询。
         *                     0x05	您预约的咨询，将在一小时后开始。
         *                     0x06	自定义内容
         * @param message      信息内容  类型为0x06时,才有信息内容
         * @param dataResponse 0x00: 手环同步成功 0x01：手环同步失败
         */
        appSendMsgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appPushMessage(1, null, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "app信息推送=" + code);
                    }
                });
            }
        });

        /**
         * APP 有效步数回写
         * 运动数据回写
         * @param step         有效步数
         * @param type         运动类型 0x00:休养静歇 0x01:休闲热身 0x02:心肺强化 0x03:减脂塑性 0x04:运动极限 0x05:空状态
         * @param dataResponse 0x00: 手环同步成功 0x01：手环同步失败
         */
        sportDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appEffectiveStep(10000, 1, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "运动回写=" + code);
                    }
                });
            }
        });


/**
 * APP 睡眠数据回写到手环
 *
 * @param deepSleepTimeHour  深睡(小时)
 * @param deepSleepTimeMin   深睡(分钟)
 * @param lightSleepTimeHour 浅睡(小时)
 * @param lightSleepTimeMin  浅睡(分钟)
 * @param totalSleepTimeHour 总睡眠(小时)
 * @param totalSleepTimeMin  总睡眠(分钟)
 * @param dataResponse       0x00: 手环接收成功 0x01: 设置失败-参数错误
 */
        sleepDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.appSleepWriteBack(3, 40, 2, 20, 6, 0, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "睡眠回写=" + code);
                    }
                });
            }
        });


        /**
         * 温度报警  新增了小数部分, 新手环才有这功能
         *体温报警
         * @param on_off         温度报警开关
         * @param maxTempInteger 高度报警整数部分(36 -- 100)
         * @param minTempInteger 低温报警整数部分(-127 -- 36)
         * @param maxTempFloat   高度报警小数部分(1 -- 9)
         * @param minTempFloat   低温报警小数部分(1 -- 9)
         * @param dataResponse
         */
        tiwenNoticeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YCBTClient.newSettingTemperatureAlarm(true, 37, 33, 3, 3, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        Log.e("TimeSetActivity", "体温报警=" + code);
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
                        Log.e("TimeSetActivity", "设置表盘code=" + code);
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
                        Log.e("TimeSetActivity", "获取表盘=" + resultMap.toString());
                    }
                });
            }
        });
    }


    private void isOpenBloodOrder() {
        Log.e("historyorder", "实时测试,点击开启按钮。。。");
        YCBTClient.appEcgTestStart(new BleDataResponse() {
            @Override
            public void onDataResponse(int i, float v, HashMap hashMap) {

                try {
                    if (i == Constants.CODE.Code_OK) {
                        Log.e("historyorder", "实时测试 开启成功=" + i);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                beginAnnation();
//                            }
//                        });

                    } else {
                        Log.e("historyorder", "实时测试 失败了=" + i);

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
//                        Log.e("historytest", "实时测试 返回结果=" + hashMap.toString());
                if (dataType == Constants.DATATYPE.Real_UploadHeart) {
                    //心率数据 dataMap
                    try {
                        if (hashMap != null) {
                            String backVal = hashMap.toString();
                            JSONObject jsonObject = new JSONObject(backVal);
                            String hrVal = jsonObject.getString("heartValue");
                            Log.e("historyorhr", "实时测试 心率=" + hrVal);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (dataType == Constants.DATATYPE.Real_UploadBlood) {
                    //血压数据 dataMap
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

                    Log.e("historyorhr", "实时测试 ppg数据");

                    //实时PPG数据  dataMap
                    byte[] ppgBytes = (byte[]) hashMap.get("data");
                    for (byte ecgbyte : ppgBytes) {
                    }
                } else if (dataType == Constants.DATATYPE.Real_UploadECG) {

//                    if (beginReceiveData == 0) {
//                        beginReceiveData = 1;
//                        //开始接收数据的时候，开启动画
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mHandlerTwo.removeMessages(MSG_CODE_TWO);
//                                openBloodTestSuccess();
//                            }
//                        });
//                    }
//                    Log.e("historyorhr", "实时测试 ecg数据");
//
//                    //实时ECG数据  dataMap
//                    byte[] ecgBytes = (byte[]) hashMap.get("data");
//                    for (byte ecgbyte : ecgBytes) {
//                        ecgVals.add(ecgbyte);
//                    }
                } else if (dataType == Constants.DATATYPE.AppECGPPGStatus) {
                    int EcgStatus = (int) hashMap.get("EcgStatus");
                    int PPGStatus = (int) hashMap.get("PPGStatus");
                    if (EcgStatus == 0) {
                        Log.e("status", "ecg心电接触良好");
                    } else if (EcgStatus == 1) {
                        Log.e("status", "心电电极脱落");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    } else if (EcgStatus == 2) {
                        Log.e("status", "点击脱落");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }

                    if (PPGStatus == 0) {
                        Log.e("status", "ppg心电接触良好");
                    } else if (PPGStatus == 1) {
                        Log.e("status", "点击脱落");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    } else if (PPGStatus == 2) {
                        Log.e("status", "点击脱落");
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
