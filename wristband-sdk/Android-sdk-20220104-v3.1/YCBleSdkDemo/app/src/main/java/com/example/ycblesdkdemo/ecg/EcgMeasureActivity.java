package com.example.ycblesdkdemo.ecg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ycblesdkdemo.R;
import com.example.ycblesdkdemo.ecg.util.SharedPreferencesUtil;
import com.example.ycblesdkdemo.ecg.view.CardiographView;
import com.example.ycblesdkdemo.ecg.view.CommonDialog;
import com.example.ycblesdkdemo.ecg.view.EcgMeasurDialog;
import com.example.ycblesdkdemo.view.NavigationBar;
import com.google.gson.Gson;
import com.yucheng.ycbtsdk.AITools;
import com.yucheng.ycbtsdk.Constants;
import com.yucheng.ycbtsdk.YCBTClient;
import com.yucheng.ycbtsdk.bean.AIDataBean;
import com.yucheng.ycbtsdk.response.BleAIDiagnosisResponse;
import com.yucheng.ycbtsdk.response.BleDataResponse;
import com.yucheng.ycbtsdk.response.BleRealDataResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;


public class EcgMeasureActivity extends Activity {
    CardiographView mCardiographView;
    LinearLayout llElectricOn;
    LinearLayout llElectricOff;
    TextView tvBpm;
    TextView tvMmhg;
    TextView tvHrv;
    TextView mProgressBarText;
    ProgressBar mProgressBar;
    TextView tvStartFinish;
    ImageView ivStop;

    private static EcgMeasureActivity ecgMeasureActivity;
    private Gson mGson;

    private int count = 0; // ProgressBar 进度
    private boolean isStart = false;// 心电测量是否开始
    private static boolean isSpeeded = false;

    private List<Integer> mEcgMeasureList = new ArrayList<>(); // 心电测量数据
    private int index = 0; // 遍历 心电测量数据 mEcgMeasureList
    private int mLastUpDataCnt = 0; // 记录心电测量数据大小
    private static MediaPlayer mMediaPlay; // 心电测量声音
    private boolean hrv_is_from_device = false; // 手环是否有hrv

    private int mHeart; // 心率
    private int mDBP; // 高压 舒张压
    private int mSBP; // 低压 收缩压
    private int mHRV; // HRV
    private boolean isAfib; //是否房颤
    private int mDiagnoseType; //诊断类型  1正常  4 ,5 ,9异常
    private long mMeasureTime; // 测量时间

    private boolean isProgressBar = true; // 是否有数据 有 进度条开始跑


/*
     * Handler 方法，接收回调的方法
     */

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                // 开始心电测量
                openEcgMeasure();
            } else if (msg.what == 2) {
                if (index < drawLists.size()) {
                    if (mCardiographView.plist.size() > mCardiographView.WidthDots) {
                        mCardiographView.plist.remove(0);
                    }
                    mCardiographView.plist.add(drawLists.get(index));
                    index++;
                    mCardiographView.invalidate();
                    mLastUpDataCnt = drawLists.size();
                }
                if (0 == drawLists.size()) {
                    mHandler.sendEmptyMessage(4);
                } else {
                    if (isProgressBar) {
                        mHandler.post(mProRunnable);
                        isProgressBar = false;
                    }
                    mHandler.sendEmptyMessage(5);
                }
            } else if (msg.what == 3) {
                mHandler.removeCallbacks(mProRunnable);
                isProgressBar = true;
                isStart = false;
                isSpeeded = false;
                count = 0;
                index = 0;
                mProgressBar.setProgress(count);
                ivStop.setVisibility(View.GONE);
                mProgressBarText.setVisibility(View.GONE);
                tvStartFinish.setVisibility(View.VISIBLE);
                tvStartFinish.setText("开始");
            } else if (msg.what == 4) {
                llElectricOn.setVisibility(View.GONE);
                llElectricOff.setVisibility(View.VISIBLE);
            } else if (msg.what == 5) {
                llElectricOff.setVisibility(View.GONE);
                llElectricOn.setVisibility(View.VISIBLE);
            } else if (msg.what == 6) {
                goEcgAIDiagnosisActivity();
            } else if (msg.what == 21) {
                setTextViewHrv();
            } else if (msg.what == 22) {
                if (isStart) {
                    if (mMediaPlay != null) {
                        if (drawLists.size() > 200) {
                            mMediaPlay.start();
                        }
                    }
                }
            } else if (msg.what == 23) {
                tvBpm.setText(mHeart + "");
                tvMmhg.setText(mDBP + "/" + mSBP);
                setTextViewHrv();
            }
        }
    };

    private void setTextViewHrv() {
        if (mHRV != 0.0f) {
            if (mHRV > 150) {
                mHRV = 150;
            }
            tvHrv.setText(String.format("%d", mHRV));
        } else {
            if (AITools.getInstance().getHRV() != 0) {
                mHRV = AITools.getInstance().getHRV();
                tvHrv.setText(mHRV + "");
            } else {
                tvHrv.setText("-");
            }
        }
    }

    public static EcgMeasureActivity getInstance() {
        return ecgMeasureActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecgmeasure);
        initView();
        initData();
    }

    private void initView() {
        NavigationBar bar = findViewById(R.id.navigationbar);

         mCardiographView = findViewById(R.id.cardiographView);
         llElectricOn= findViewById(R.id.ll_electric_on);
         llElectricOff= findViewById(R.id.ll_electric_off);
         tvBpm= findViewById(R.id.tv_bpm);
         tvMmhg= findViewById(R.id.tv_mmhg);
         tvHrv= findViewById(R.id.tv_hrv);
         mProgressBarText= findViewById(R.id.tv_schedule);
         mProgressBar= findViewById(R.id.progress_bar);
         tvStartFinish= findViewById(R.id.tv_start_finish);
         ivStop= findViewById(R.id.iv_stop);
        // 设置Title
        bar.setTitle(getString(R.string.ecg_measure_title));
        bar.showLeftbtn(0);
        bar.setLeftOnClickListener(new NavigationBar.MyOnClickListener() {
            @Override
            public void onClick(View btn) {
                if (isStart) {
                    initDialog();
                } else {
                    finish();
                }
            }
        });
        showLeftRightDiaLog();
        mGson = new Gson();
        tvStartFinish.setOnClickListener(new OnClickListenerImpl());
        ivStop.setOnClickListener(new OnClickListenerImpl());
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        mGson = new Gson();
    }

    //定义线程  设置心电测量进度条
    Runnable mProRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (count == 0) {
                tvStartFinish.setText("开始");
            }
            //如果进度小于100,则延迟1000毫秒之后重复执行runnable
            if (count < 100 && isStart) {
                count++;
                mProgressBar.setProgress(count);
                tvStartFinish.setVisibility(View.GONE);
                mProgressBarText.setVisibility(View.VISIBLE);
                ivStop.setVisibility(View.VISIBLE);
                mProgressBarText.setText(count + "%");
                mHandler.postDelayed(mProRunnable, 600);
            } else {
                // 心电测量结束
                ecgMeasureStop();
            }
        }
    };


/**
     * @param
     * @return
     * @description 显示测量选择左右手DiaLog
     * @author zy
     * @time 2020/11/24 10:11
     */

    private void showLeftRightDiaLog() {
        EcgMeasurDialog ecgMeasurDialog = new EcgMeasurDialog(EcgMeasureActivity.this);
        ecgMeasurDialog.onCreateView();
        ecgMeasurDialog.setUiBeforShow();
        // 点击空白区域能不能退出
        ecgMeasurDialog.setCanceledOnTouchOutside(true);
        // Dialog选择事件回调
        ecgMeasurDialog.setOnButtonClickListener(new EcgMeasurDialog.OnDialogButtonClickListener() {
            @Override
            public void onLeftClick() {
                YCBTClient.settingHandWear(0x00, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        if (code == 0) {
                            Log.i("EcgMeasurDialog", "你选择的是:左手");
                        }
                    }
                });
            }

            @Override
            public void onRightClick() {
                YCBTClient.settingHandWear(0x01, new BleDataResponse() {
                    @Override
                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                        if (code == 0) {
                            Log.i("EcgMeasurDialog", "你选择的是:右手");
                        }
                    }
                });
            }
        });
        //按返回键能不能退出
        ecgMeasurDialog.setCancelable(true);
        ecgMeasurDialog.show();
    }

    private void initDialog() {
        final CommonDialog dialog = new CommonDialog(EcgMeasureActivity.this);
        String message;
        if (mEcgMeasureList.size() > 2800) {
            message = getString(R.string.ecg_measure_dialog_message);
        } else {
            message = getString(R.string.ecg_measure_dialog_message_low_time);
        }
        dialog.setMessage(message) // 是否结束心电测量
                .setTitle(getString(R.string.ecg_measure_dialog_title)) // 提示
                .setSingle(false).setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
            @Override
            public void onConfirmClick() {
                ecgMeasureStop();
                dialog.dismiss();
                Toast.makeText(EcgMeasureActivity.this, "结束", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelClick() {
                dialog.dismiss();
                Toast.makeText(EcgMeasureActivity.this, "取消", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEditTextConfirmClick(String mEditText) {
                dialog.dismiss();
            }
        }).show();
    }


    private class OnClickListenerImpl implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // 开始 完成
                case R.id.tv_start_finish:
                    if (YCBTClient.connectState() == Constants.BLEState.ReadWriteOK) {
                        ecgMeasureStart();
                    } else {
                        Toast.makeText(EcgMeasureActivity.this, "请连接手环", Toast.LENGTH_SHORT).show();
                    }
                    break;
                // 结束
                case R.id.iv_stop:
                    initDialog();
                    break;
                default:
                    ;
            }
        }
    }


/**
     * @param
     * @return
     * @description 心电测量开始
     * @author zy
     * @time 2020/12/15 9:08
     */

    private void ecgMeasureStart() {
        AITools.getInstance().init();
        tvBpm.setText("---");
        tvMmhg.setText("---");
        tvHrv.setText("---");
        mMediaPlay = MediaPlayer.create(EcgMeasureActivity.this, R.raw.vidio);
        //mProgressBar.setProgress(count);
        tvStartFinish.setVisibility(View.GONE);
        mProgressBarText.setVisibility(View.VISIBLE);
        ivStop.setVisibility(View.VISIBLE);
        mProgressBarText.setText("0%");
        if (mEcgMeasureList != null) {
            mEcgMeasureList.clear();
        }
        if(drawLists != null){
            drawLists.clear();
        }
        isStart = true;
        hrv_is_from_device = false;
        mCardiographView.plist.clear();
        mCardiographView.initList();
        mCardiographView.invalidate();
        makeStart();
    }


/**
     * @param
     * @return
     * @description 心电测量结束
     * @author zy
     * @time 2020/12/15 9:09
     */

    private void ecgMeasureStop() {
        YCBTClient.appEcgTestEnd(new BleDataResponse() {
            @Override
            public void onDataResponse(int code, float ratio, HashMap resultMap) {
                System.out.println("chong-------code==" + code);
                if (code == 0) {
                    mHandler.sendEmptyMessage(3);
                    if (mEcgMeasureList.size() > 2800) {
                        // 心率分析
                        getAiResult();
                    }
                }
            }
        });
    }

    private void getAiResult() {
        // AIDataBean mAiResult = AIData.a().getAIResult();
        AITools.getInstance().getAIDiagnosisResult(new BleAIDiagnosisResponse() {
            @Override
            public void onAIDiagnosisResponse(AIDataBean aiDataBean) {
                if (aiDataBean != null) {
                    short heart = aiDataBean.heart;//心率
                    // 诊断类型  1正常  4 ,5 ,9异常
                    mDiagnoseType = aiDataBean.qrstype;//类型 1正常心拍 5室早心拍 9房早心拍  14噪声
                    // 是否房颤
                    isAfib = aiDataBean.is_atrial_fibrillation;//是否心房颤动
                    System.out.println("chong------heart==" + heart + "--qrstype==" + mDiagnoseType + "--is_atrial_fibrillation==" + isAfib);
                    // 存储数据
                    saveData();
                }
            }
        });
    }


/**
     * @param
     * @return
     * @description 开始心电测量
     * @author zy
     * @time 2020/12/17 12:22
     */

    private void openEcgMeasure() {
        // 打开开关
        YCBTClient.appEcgTestStart(new BleDataResponse() {
            @Override
            public void onDataResponse(int i, float v, HashMap hashMap) {

            }
        }, new BleRealDataResponse() {
            @Override
            public void onRealDataResponse(int i, HashMap hashMap) {
                if (hashMap != null) {
                    int dataType = (int) hashMap.get("dataType");
                    Log.e("qob", "onRealDataResponse " + i + " dataType " + dataType);
                    if (i == Constants.DATATYPE.Real_UploadECG) {
                        List<Integer> data = (List<Integer>) hashMap.get("data");
                        person(data);
                        System.out.println("chong----------ecgData==" + mEcgMeasureList.size());
                    } else if (i == Constants.DATATYPE.Real_UploadECGHrv) {
                        if (!hrv_is_from_device && hashMap.get("data") != null && ((int) hashMap.get("data")) != 0) {
                            mHRV = (int) hashMap.get("data");
                            mHandler.sendEmptyMessage(21);
                        }
                    } else if (i == Constants.DATATYPE.Real_UploadECGRR) {
                        mHandler.sendEmptyMessage(22);
                        float param = (float) hashMap.get("data");
                        Log.e("qob", "RR invo " + param);
                    } else if (i == Constants.DATATYPE.Real_UploadBlood) {
                        mHeart = (int) hashMap.get("heartValue");//心率
                        mDBP = (int) hashMap.get("bloodDBP");//高压
                        mSBP = (int) hashMap.get("bloodSBP");//低压
                        if (hashMap.get("hrv") != null && ((int) hashMap.get("hrv")) != 0) {
                            hrv_is_from_device = true;
                            mHRV = (int) hashMap.get("hrv");
                        }
                        mHandler.sendEmptyMessage(23);
                    } else if (i == Constants.DATATYPE.AppECGPPGStatus) {
                    }
                }
            }
        });
    }

    private List<Integer> drawLists = new ArrayList<>();
    private void person(List<Integer> datas) {
        mEcgMeasureList.addAll(datas);
        int index = 0;
        for (Integer data : datas) {
            if (index % 3 == 0) {
                int value = data / 40;
                drawLists.add(value > 500 ? 500 : value);
            }
            index++;
        }
    }

    public void makeStart() {
        mHandler.sendEmptyMessage(1);
        // 开始测量时间
        mMeasureTime = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isStart) {
                    try {
                        if (mCardiographView.plist.size() > mCardiographView.WidthDots + 23) {
                            isSpeeded = true;
                        } else if (mCardiographView.plist.size() < mCardiographView.WidthDots + 3) {
                            isSpeeded = false;
                        }
                        if (isSpeeded) {
                            Thread.sleep(6);
                        } else {
                            Thread.sleep(13);
                        }
                    } catch (InterruptedException e) {
                        e.getStackTrace();
                    }
                    mHandler.sendEmptyMessage(2);// modified by shl 2018-08-22
                }
            }
        }).start();
    }

    //c调用java回调方法
    public void hrv_evt_handle(int evt_type, float params) {
        switch (evt_type) {
            case 4:
                if (params != 0.f) {
                    Message msg = new Message();
                    msg.what = 21;
                    msg.obj = params;
                    mHandler.sendMessage(msg);
                }
                break;
            case 3:
                Message msg = new Message();
                msg.what = 22;
                msg.obj = params;
                mHandler.sendMessage(msg);
                break;
            default:
                break;
        }
    }


/**
     * @param
     * @return
     * @description 保存 ECG测量 数据
     * @author zy
     * @time 2020/12/17 12:27
     */

    public void saveData() {
        // 测试不够 重新测试
        if (mEcgMeasureList.size() < 2800) {
            Log.i("saveData", "您当前测试时间过短,请重新测试");
            return;
        }
        //demo暂时保存在sharepreferences中，建议保存在数据库中  记录界面用来展示
        SharedPreferencesUtil.saveEcgList(drawLists,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                EcgMeasureActivity.this);
    }

/**
     * @param
     * @return
     * @description 跳转到 AI 诊断
     * @author zy
     * @time 2020/12/17 12:27
     */

    private void goEcgAIDiagnosisActivity() {
        /*String mBp = mDBP + "/" + mSBP;
        Intent intents = new Intent(EcgMeasureActivity.this, EcgAiDiagnoseActivity.class);
        intents.putExtra("mMeasureTime", TimeStampUtils.dateForString(TimeStampUtils.longStampForDate(mMeasureTime)));
        //intents.putExtra("mEcgMeasureList",  mGson.toJson(mEcgMeasureList));
        intents.putExtra("mEcgMeasureDbSize", mEcgMeasureDbSize.size());
        intents.putExtra("mBp", mBp);
        intents.putExtra("mHeart", mHeart);
        intents.putExtra("mHRV", mHRV);
        intents.putExtra("mAge", 22);
        intents.putExtra("mSex", 0);
        intents.putExtra("isAfib", isAfib);
        intents.putExtra("mDiagnoseType", mDiagnoseType);
        startActivity(intents);
        if (mDiagnoseType == 1 || mDiagnoseType == 5 || mDiagnoseType == 9 || isAfib) {
            Log.i("AIDiagnosisActivity", "-1-");
        } else {
            Log.i("AIDiagnosisActivity", "-2-");
            //startActivity(new Intent(EcgMeasureActivity.this, AIFeildActivity.class));
        }*/
    }


    /**
     * 返回键退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isStart) {
                initDialog();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清空消息队列 和 结束Handler生命周期
        mHandler.removeCallbacksAndMessages(null);
        if (mEcgMeasureList != null) {
            mEcgMeasureList.clear();
        }
    }
}

