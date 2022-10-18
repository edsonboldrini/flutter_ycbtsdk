package com.example.ycblesdkdemo.ecg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ycblesdkdemo.R;
import com.example.ycblesdkdemo.ecg.adapter.EcgHisListAdapter;
import com.example.ycblesdkdemo.ecg.bean.EcgSyncListResponse;
import com.example.ycblesdkdemo.ecg.util.SharedPreferencesUtil;
import com.example.ycblesdkdemo.ecg.view.Cardiograph2View;
import com.example.ycblesdkdemo.util.ToastUtil;
import com.example.ycblesdkdemo.view.NavigationBar;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.yucheng.ycbtsdk.AITools;
import com.yucheng.ycbtsdk.YCBTClient;
import com.yucheng.ycbtsdk.bean.AIDataBean;
import com.yucheng.ycbtsdk.response.BleAIDiagnosisResponse;
import com.yucheng.ycbtsdk.response.BleDataResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author StevenLiu
 * @date 2021/12/29
 * @desc one word for this class
 */
public class EcgActicvity extends Activity {
    private SmartRefreshLayout refreshLayout;
    private TextView tvStartEcg;//启动ecg测量  （start ecg）
    private Cardiograph2View cardiographView;//显示最近的一段
    private ListView listView;//历史记录
    private List<String> strs;
    private List<String> lists = new ArrayList<>();
    private NavigationBar bar;
    private List<EcgSyncListResponse.DataBean> datas = new ArrayList<>();
    private ProgressDialog progressDialog;
    private EcgHisListAdapter adapter;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    dismisDialog(1);
                    break;
                case 2:
                    dismisDialog(2);
                    break;
                case 3:
                    upDateSyncData();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg);
        init();
        initListener();
    }

    private void init() {
        bar = findViewById(R.id.navigationbar);
        tvStartEcg = findViewById(R.id.tv_start_button);
        cardiographView = findViewById(R.id.cardiograph2View);
        listView = findViewById(R.id.ls_view);
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(true); //是否启用下拉刷新功能
        refreshLayout.setEnableLoadMore(false); //是否启用上拉加载功能
        adapter = new EcgHisListAdapter(EcgActicvity.this, lists);
        listView.setAdapter(adapter);
    }

    private void initListener() {
        bar.showLeftbtn(0);
        bar.setLeftOnClickListener(new NavigationBar.MyOnClickListener() {
            @Override
            public void onClick(View btn) {
                finish();
            }
        });
        bar.setTitle("ECG");
        tvStartEcg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EcgActicvity.this, EcgMeasureActivity.class));
            }
        });
        adapter.setOnCall(new EcgHisListAdapter.OnCall() {
            @Override
            public void setInfo(View v, int position) {
                switch (v.getId()) {
                    case R.id.tv_jilu:
                        //startActivity(new Intent(EcgActicvity.this, HeartMsgActivity.class).putExtra("timeStr", lists.get(position));
                        break;
                    case R.id.tv_jiance:
                        //startActivity(new Intent(EcgActicvity.this, HeartMsgActivity.class).putExtra("timeStr", lists.get(position));
                        break;
                    case R.id.btn_sycn:
                        syncEcgListData(datas.get(position).collectSN, datas.get(position).collectStartTime, datas.get(position).collectSendTime);
                        break;
                }
            }
        });
    }

    private void initData() {
        List<Integer> blist = new ArrayList<>();
        List<Integer> blist_change = new ArrayList<>();
        strs = SharedPreferencesUtil.readEcgList(this);
        if (strs != null && strs.size() > 0) {
            blist.addAll(SharedPreferencesUtil.readEcgListMsg(strs.get(0), this));
            if (blist.size() > 280) {
                for (int i = 280; i < blist.size(); i++) {
                    blist_change.add(blist.get(i));
                }
            } else {
                blist_change.addAll(blist);
            }
            cardiographView.setDatas(blist_change, true);
            cardiographView.invalidate();
        }
        datas.clear();
        YCBTClient.collectEcgList(new BleDataResponse() {
            @Override
            public void onDataResponse(int code, float v, HashMap resultMap) {
                if (code == 0) {
                    if (resultMap == null) {
                        return;
                    }
                    EcgSyncListResponse ecgSyncListResponse = new Gson().fromJson(String.valueOf(resultMap), EcgSyncListResponse.class);
                    if (ecgSyncListResponse == null || ecgSyncListResponse.data == null) {
                        return;
                    }
                    List<EcgSyncListResponse.DataBean> dataBeans = ecgSyncListResponse.data;
                    for (EcgSyncListResponse.DataBean dataBean : dataBeans) {
                        List<Integer> lists = SharedPreferencesUtil.readEcgListMsg(
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dataBean.collectStartTime)),
                                EcgActicvity.this);
                        if (lists != null && lists.size() > 0) {
                            deleteEcgInfo(dataBean.collectSendTime);
                        } else {
                            datas.add(dataBean);
                        }
                    }
                    handler.sendEmptyMessage(3);
                }
            }
        });
    }

    private void upDateSyncData() {
        lists.clear();
        for (EcgSyncListResponse.DataBean dataBean : datas) {
            lists.add(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(dataBean.collectStartTime)) + "");
        }
        if (strs != null) {
            lists.addAll(strs);
        }
        if (adapter != null) {
            adapter.setDataChanged(lists);
        } else {
            adapter = new EcgHisListAdapter(EcgActicvity.this, lists);
            listView.setAdapter(adapter);
        }
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
        }
    }

    private void syncEcgListData(int index, long startTime, long sendTime) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, getString(R.string.prompt), getString(R.string.ecg_sync_data), true, false);
        } else {
            progressDialog.show();
        }
        AITools.getInstance().init();
        YCBTClient.collectEcgDataWithIndex(index, new BleDataResponse() {
            @Override
            public void onDataResponse(int code, float ratio, HashMap resultMap) {
                if (code == 0 && resultMap.get("data") != null) {
                    byte[] ints = (byte[]) resultMap.get("data");
                    List<Integer> mEcgMeasureList = AITools.getInstance().ecgRealWaveFiltering(ints);
                    //demo暂时保存在sharepreferences中，建议保存在数据库中  记录界面用来展示
                    SharedPreferencesUtil.saveEcgList(person(mEcgMeasureList),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(startTime)),
                                    EcgActicvity.this);
                    getAIDiagnosisResult(startTime, sendTime);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private List<Integer> person(List<Integer> datas) {
        List<Integer> lists = new ArrayList<>();
        int index = 0;
        int value = 0;
        for (Integer data : datas) {
            value += data;
            index++;
            if (index % 3 == 0) {
                value = value / 40 / 3;
                value = (value > 500 ? 500 : value);
                value = (value < -500 ? -500 : value);
                lists.add(value);
            }
        }
        return lists;
    }

    /*
    * 诊断结果  诊断界面用来展示
    * */
    private void getAIDiagnosisResult(long startTime, long sendTime) {
        AITools.getInstance().getAIDiagnosisResult(new BleAIDiagnosisResponse() {
            @Override
            public void onAIDiagnosisResponse(AIDataBean aiDataBean) {
                if (aiDataBean != null) {
                    short heart = aiDataBean.heart;//心率
                    int mDiagnoseType = aiDataBean.qrstype;//类型 1正常心拍 5室早心拍 9房早心拍  14噪声
                    boolean isAfib = aiDataBean.is_atrial_fibrillation;//是否心房颤动
                    System.out.println("chong------heart==" + heart + "--qrstype==" + mDiagnoseType + "--is_atrial_fibrillation==" + isAfib);
                    deleteEcgInfo(sendTime);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private void deleteEcgInfo(long sendTime) {
        // 数据库存储成功后 删除手环数据
        YCBTClient.deleteHistoryListData(0, sendTime, new BleDataResponse() {
            @Override
            public void onDataResponse(int code, float ratio, HashMap resultMap) {
                if (code == 0) {
                    handler.sendEmptyMessage(2);
                    initData();
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void dismisDialog(int type) {
        if (!EcgActicvity.this.isFinishing() && progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            if (type == 1) {
                ToastUtil.getInstance(EcgActicvity.this).toast(getString(R.string.ecg_sync_data_failed));
            } else if (type == 2) {
                ToastUtil.getInstance(EcgActicvity.this).toast(getString(R.string.ecg_sync_data_success));
            }
        }
    }
}
