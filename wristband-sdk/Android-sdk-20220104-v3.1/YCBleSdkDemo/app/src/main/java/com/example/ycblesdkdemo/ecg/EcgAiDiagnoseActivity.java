package com.example.ycblesdkdemo.ecg;/*
package com.example.ycblesdkdemo.ecg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yucheng.smarthealthpro.R;
import com.yucheng.smarthealthpro.base.BaseActivity;
import com.yucheng.smarthealthpro.care.utils.ImageViewUtil;
import com.yucheng.smarthealthpro.framework.util.Constants;
import com.yucheng.smarthealthpro.framework.util.SharedPreferencesUtils;
import com.yucheng.smarthealthpro.greendao.bean.EcgMeasureDb;
import com.yucheng.smarthealthpro.greendao.utils.EcgMeasureDbUtils;
import com.yucheng.smarthealthpro.home.activity.ecg.adapter.EcgAiDiagnoseAdapter;
import com.yucheng.smarthealthpro.home.activity.ecg.bean.EcgAiDiagnoseItemBean;
import com.yucheng.smarthealthpro.home.view.Cardiograph3View;
import com.yucheng.smarthealthpro.utils.Constant;
import com.yucheng.smarthealthpro.utils.TimeStampUtils;
import com.yucheng.smarthealthpro.utils.Tools;
import com.yucheng.ycbtsdk.YCBTClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.jessyan.autosize.internal.CancelAdapt;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

*/
/**
 * @author: zhongying
 * @date: 2020/12/14
 * @Description: AI诊断页面
 *//*

public class EcgAiDiagnoseActivity extends BaseActivity implements CancelAdapt {

    private Unbinder mUnbinder;
    @BindView(R.id.tv_diagnosis_result)
    TextView tvDiagnosisResult;
    @BindView(R.id.tv_heart)
    TextView tvHeart;
    @BindView(R.id.tv_details)
    TextView tvDetails;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_bool)
    TextView tvBool;
    @BindView(R.id.iv_ai_diagnosis_ecg)
    ImageView ivAiDiagnosisEcg;
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ai_diagnosis_in_doubt)
    TextView aiDiagnosisInDoubt;

    private Cardiograph3View cardiographView;
    private EcgMeasureDbUtils mEcgMeasureDbUtils;
    private Gson mGson;
    private Bitmap mBitmap;
    private List<EcgAiDiagnoseItemBean> lists;
    public List<Integer> blist = new ArrayList<>();
    private List<Integer> mEcgMeasureList; // 心电测量数据
    private int mHeart; // 心率
    private String mBp; // 血压 舒张压 收缩压
    private int mHRV; // mHRV
    private boolean isAfib; //是否房颤
    private int mDiagnoseType; //诊断类型  1正常  4 ,5 ,9异常
    private String mMeasureTime; // 测量时间
    private int mAge; // 年龄
    private int mSex; // 性别
    private int mEcgMeasureDbSize; // 查询数据库大小 设置查询ID
    private String phone;

    private String ycble_bindedname;
    private String ycble_bindedmac;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_diagnosis);
        mUnbinder = ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        // 设置Title
        changeTitle(getString(R.string.ecg_ai_diagnose_title));
        // 显示返回按钮
        showBack();

        mEcgMeasureDbUtils = new EcgMeasureDbUtils(context);
        mGson = new Gson();
        lists = new ArrayList<>();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        ycble_bindedname = (String) SharedPreferencesUtils.get(context, Constant.SpConstKey.YCBLE_BINDED_NAME, "1");
        ycble_bindedmac = (String) SharedPreferencesUtils.get(context, Constant.SpConstKey.YCBLE_BINDED_MAC, "1");

        Intent intent = getIntent();
        String care = intent.getStringExtra("care");
        if (care != null && care.equals(getString(R.string.care_text))) {
            mEcgMeasureDbSize = intent.getIntExtra("mEcgMeasureDbSize", 0);
            mBp = intent.getStringExtra("mBp");
            mHeart = intent.getIntExtra("mHeart", 0);
            mHRV = intent.getIntExtra("mHRV", 0);
            mAge = intent.getIntExtra("mAge", 0);
            mSex = intent.getIntExtra("mSex", 0);
            isAfib = intent.getBooleanExtra("isAfib", false);
            mDiagnoseType = intent.getIntExtra("mDiagnoseType", 0);
            phone = intent.getStringExtra("phone");
            mEcgMeasureList = intent.getIntegerArrayListExtra("lists");
        } else {
            long time = intent.getLongExtra("time", 0);
            mMeasureTime = TimeStampUtils.dateForString(TimeStampUtils.longStampForDate(time));
            mEcgMeasureDbSize = intent.getIntExtra("mEcgMeasureDbSize", 0);
            mBp = intent.getStringExtra("mBp");
            mHeart = intent.getIntExtra("mHeart", 0);
            mHRV = intent.getIntExtra("mHRV", 0);
            mAge = intent.getIntExtra("mAge", 0);
            mSex = intent.getIntExtra("mSex", 0);
            isAfib = intent.getBooleanExtra("isAfib", false);
            mDiagnoseType = intent.getIntExtra("mDiagnoseType", 0);
            mSex = intent.getIntExtra("mSex", 0);

            // 获取数据库心电测量数据
            List<EcgMeasureDb> mEcgMeasureDb = mEcgMeasureDbUtils.queryByTime(time);
            if (mEcgMeasureDb != null && mEcgMeasureDb.size() > 0) {
                String measureData = mEcgMeasureDb.get(0).getMeasureData();
                Type type = new TypeToken<List<Integer>>() {
                }.getType();
                mEcgMeasureList = mGson.fromJson(measureData, type);
            }
        }

        tvAge.setText("年齡:" + mAge); //年龄
        //tvBool.setText(mBp + ""); // 血型
        tvHeart.setText(mHeart + ""); // 心率
        tvSex.setText("性别:" + (mSex == 0 ? getString(R.string.man_text) : getString(R.string.woman_text))); //性别
        {
            if (isAfib) {//房颤
                tvDiagnosisResult.setText(getString(R.string.ecg_ai_diagnose_suspected_atrial_fibrillation));
                tvDetails.setText(getString(R.string.ecg_ai_diagnosis_suspected_atrial_fibrillation_detail));
            } else {
                if (mDiagnoseType == 5) {
                    tvDiagnosisResult.setText(getString(R.string.ecg_ai_diagnosis_ventricular_precordial_electrocardiogram));
                    tvDetails.setText(getString(R.string.ecg_ai_diagnosis_ventricular_precordial_electrocardiogram_detail));
                } else if (mDiagnoseType == 9) {
                    tvDiagnosisResult.setText(getString(R.string.ecg_ai_diagnosis_atrial_premature_electrocardiogram));
                    tvDetails.setText(getString(R.string.ecg_ai_diagnosis_atrial_premature_electrocardiogram_detail));
                } else if (mHeart != 0 && mHeart <= 50) {//疑似心动过缓
                    tvDiagnosisResult.setText(getString(R.string.ecg_ai_diagnosis_suspected_atrial_bradycardia));
                    tvDetails.setText(getString(R.string.ecg_ai_diagnosis_suspected_atrial_bradycardia_detail));
                } else if (mHeart != 0 && mHeart >= 120) {//疑似心动过速
                    tvDiagnosisResult.setText(getString(R.string.ecg_ai_diagnosis_suspected_atrial_tachycardia));
                    tvDetails.setText(getString(R.string.ecg_ai_diagnosis_suspected_atrial_tachycardia_detail));
                } else if (!"--".equals(mHRV) && mHRV != 0 && mHRV >= 125) {//疑似窦性心律不齐
                    tvDiagnosisResult.setText(getString(R.string.ecg_ai_diagnosis_suspected_atrial_arrhythmia));
                    tvDetails.setText(getString(R.string.ecg_ai_diagnosis_suspected_atrial_arrhythmia_detail));
                } else {//正常心电   if (mDiagnoseType == 1)
                    tvDiagnosisResult.setText(getString(R.string.ecg_ai_diagnosis_normal_ecg));
                    tvDetails.setText(getString(R.string.ecg_ai_diagnosis_normal_ecg_detail));
                }
            }

            */
/*String lan = getString(R.string.lan);
            if ("en".equals(lan) || "cn".equals(lan) || "tw".equals(lan)) {
                findViewById(R.id.ai_diagnosis_in_doubt).setVisibility(View.VISIBLE);
                findViewById(R.id.ai_diagnosis_in_doubt).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        aiLogin();
                    }
                });
            }*//*

        }
        // 添加 心电Ai诊断疾病筛查数据
        addBean();
        // 添加 心电测量图
        addView(mEcgMeasureList);
        // 设置 心电Ai诊断疾病筛查 Item RecycleView
        setRecycleView();
    }

    */
/**
     * @param
     * @return
     * @description 添加 心电Ai诊断疾病筛查数据
     * @author zy
     * @time 2020/12/16 17:07
     *//*

    private void addBean() {
        String base_url;
        if (getString(R.string.lan).equals("cn")) {
            base_url = "http://www.ycaviation.com:8080/project/ECG_Diag_Web_CH/";
        } else {
            base_url = "http://www.ycaviation.com:8080/project/ECG_Diag_Web_EN/";
        }
        EcgAiDiagnoseItemBean bean = new EcgAiDiagnoseItemBean();
        if (isAfib) {
            bean.setResult(getResources().getString(R.string.ecg_ai_diagnosis_result_ok));
        } else {
            bean.setResult(getResources().getString(R.string.ecg_ai_diagnosis_result_no));
        }
        bean.setType(getResources().getString(R.string.ecg_ai_diagnosis_atrial_fibrillation));
        bean.setUrl(base_url + "fangchan.html");
        lists.add(bean);
        bean = new EcgAiDiagnoseItemBean();
        bean.setType(getResources().getString(R.string.ecg_ai_diagnosis_ventricular_flutter));
        bean.setResult(getResources().getString(R.string.ecg_ai_diagnosis_result_no));
        bean.setUrl(base_url + "xinshipudong.html");
        lists.add(bean);
        bean = new EcgAiDiagnoseItemBean();
        bean.setType(getResources().getString(R.string.ecg_ai_diagnosis_fangxing_yibo));
        bean.setResult(getResources().getString(R.string.ecg_ai_diagnosis_result_no));
        bean.setUrl(base_url + "fangxingyibo.html");
        lists.add(bean);
        bean = new EcgAiDiagnoseItemBean();
        if (mDiagnoseType == 9 && !isAfib) {
            bean.setResult(getResources().getString(R.string.ecg_ai_diagnosis_result_ok));
        } else {
            bean.setResult(getResources().getString(R.string.ecg_ai_diagnosis_result_no));
        }
        bean.setType(getResources().getString(R.string.ecg_ai_diagnosis_atrial_extrasystole));
        bean.setUrl(base_url + "fangxingzaobo.html");
        lists.add(bean);
        bean = new EcgAiDiagnoseItemBean();
        if (mDiagnoseType == 5 && !isAfib) {
            bean.setResult(getResources().getString(R.string.ecg_ai_diagnosis_result_ok));
        } else {
            bean.setResult(getResources().getString(R.string.ecg_ai_diagnosis_result_no));
        }
        bean.setUrl(base_url + "shixingzaobo.html");
        bean.setType(getResources().getString(R.string.ecg_ai_diagnosis_ventricular_extrasystole));
        lists.add(bean);
        bean = new EcgAiDiagnoseItemBean();
        bean.setType(getResources().getString(R.string.ecg_ai_diagnosis_ventricular_escape));
        bean.setResult(getResources().getString(R.string.ecg_ai_diagnosis_result_no));
        bean.setUrl(base_url + "shixingyibo.html");
        lists.add(bean);
        bean = new EcgAiDiagnoseItemBean();
        bean.setType(getResources().getString(R.string.ecg_ai_diagnosis_borderline_premature_beat));
        bean.setResult(getResources().getString(R.string.ecg_ai_diagnosis_result_no));
        bean.setUrl(base_url + "jiaojiexingzaobo.html");
        lists.add(bean);
        bean = new EcgAiDiagnoseItemBean();
        bean.setType(getResources().getString(R.string.ecg_ai_diagnosis_borderline_escape));
        bean.setResult(getResources().getString(R.string.ecg_ai_diagnosis_result_no));
        bean.setUrl(base_url + "jiaojiexingyibo.html");
        lists.add(bean);
        bean = new EcgAiDiagnoseItemBean();
        bean.setType(getResources().getString(R.string.ecg_ai_diagnosis_left_bundle_branch_block));
        bean.setResult(getResources().getString(R.string.ecg_ai_diagnosis_result_no));
        bean.setUrl(base_url + "zuoshuzhichuandaozuzhi.html");
        lists.add(bean);
        bean = new EcgAiDiagnoseItemBean();
        bean.setType(getResources().getString(R.string.ecg_ai_diagnosis_right_bundle_branch_block));
        bean.setResult(getResources().getString(R.string.ecg_ai_diagnosis_result_no));
        bean.setUrl(base_url + "youshuzhichuandaozuzhi.html");
        lists.add(bean);
    }

    private void addView(List<Integer> list) {
        RelativeLayout aaaax = findViewById(R.id.aaaax);
        cardiographView = new Cardiograph3View(this);
        cardiographView.setDatas(list, phone, mSex == 0 ? getString(R.string.perfect_userinfo_man) : getString(R.string.perfect_userinfo_woman), mAge + "", mHeart + "", mBp);
        int w = (int) (2700 * Cardiograph3View.getMultiple(EcgAiDiagnoseActivity.this));//一行2500个数据加4小格(200)
        int p = cardiographView.getDatas().size() * 3 % 2500 == 0 ? cardiographView.getDatas().size() * 3 / 2500 : cardiographView.getDatas().size() * 3 / 2500 + 1;//计算又几行
        int h = (int) (4 * 50 * (p + 1) * Cardiograph3View.getMultiple(EcgAiDiagnoseActivity.this));
        aaaax.addView(cardiographView, new RelativeLayout.LayoutParams(w, h));
        cardiographView.invalidate();
    }

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        if (h == 0 || w == 0) {
            return null;
        }
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mBitmap = loadBitmapFromView(cardiographView);
        if (mBitmap == null)
            return;
        ViewGroup.LayoutParams lp = ivAiDiagnosisEcg.getLayoutParams();
        lp.width = Tools.getwindowwidth(this) - (int) (30 * getResources().getDisplayMetrics().density);
        lp.height = (int) (1.0 * mBitmap.getHeight() * lp.width / mBitmap.getWidth());
        ivAiDiagnosisEcg.setLayoutParams(lp);
        if (mBitmap != null) {
            ivAiDiagnosisEcg.setImageBitmap(mBitmap);
            ivAiDiagnosisEcg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageViewUtil.getInstance(mBitmap);
                    startActivity(new Intent(context, AIResultImageActivity.class));
                }
            });
        }
    }

    public void aiLogin() {
*/
/*        if (progressDialog == null)
            progressDialog = ProgressDialog.show(this, getString(R.string.prompt), getString(R.string.Being_tested), true, false);
        else
            progressDialog.show();*//*

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        RequestBody requestBodyPost = new FormBody.Builder()
                .add("merchant_name", "szyc0808")
                .add("merchant_password", "c3p5YzA4MDg=")
                .build();
        Request requestPost = new Request.Builder()
                .url(Constants.AiLogin)
                .addHeader("Content-Type", "application/json")
                .post(requestBodyPost)
                .build();
        client.newCall(requestPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
*/
/*                if (progressDialog != null) {
                    progressDialog.dismiss();
                }*//*

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            int code = jsonObject.getInt("code");
                            if (code == 200) {
                                testzd();
                            } else {
                                Tools.showAlert3(context, "数据错误");
                            }
                        } catch (JSONException e) {
                            e.getStackTrace();
                        }
                    }
                });
            }
        });
    }

    */
/**
     * 上传 心电图数据 到 康源
     *//*

    public void testzd() {
        int maxb = 0;
        int minb = 0;
        if (mBp != null && !mBp.equals("0/0") && !mBp.equals("0") && mBp.split("/").length > 1) {
            maxb = Integer.parseInt(mBp.split("/")[0]);
            minb = Integer.parseInt(mBp.split("/")[1]);
        }
        String lan = Locale.getDefault().getLanguage();
        String language = "";
        if (lan.toUpperCase().equals("ZH")) {
            language = "CH";
        } else {
            language = "EN";
        }
        JSONObject object = new JSONObject();
        try {
            if (YCBTClient.connectState() == com.yucheng.ycbtsdk.Constants.BLEState.ReadWriteOK) { //蓝牙是否连接
                object.put("device_sn", ycble_bindedname +  //设备名
                        ycble_bindedmac); //设备地址
            } else {
                object.put("device_sn", "P3 f2157003");
            }
            object.put("age", mAge);
            object.put("gender", mSex);
            object.put("cellphone", phone == null ? Tools.readString("userName", context, "13812345678") : phone);
            object.put("lead_name", "1");
            object.put("lead_data", new JSONArray(mEcgMeasureList).toString());
            object.put("scale_value", 70);
            object.put("sample_base", 83);
            object.put("language", language);
            object.put("past_illness", "-");
            object.put("heart_beat", Integer.parseInt(tvHeart.getText().toString().trim().equals("--") ? "0" : tvHeart.getText().toString().trim()));
            if (YCBTClient.isSupportFunction(com.yucheng.ycbtsdk.Constants.FunctionConstant.ISHASBLOOD)) { // 是否有血压功能
                object.put("maxBP", maxb);
                object.put("minBP", minb);
            }
            if (ycble_bindedname != null && ycble_bindedmac != null
                    && (ycble_bindedname.contains("P3") || ycble_bindedname.contains("V5")
                    || ycble_bindedname.contains("P12") || ycble_bindedname.contains("P3A")
                    || ycble_bindedname.contains("P3B")
                    || ycble_bindedname.contains("XFITMATE") || ycble_bindedname.contains("P2A"))) {
                object.put("show_type", 0);
            } else {
                object.put("show_type", 1);
            }
            object.put("app_version", 77);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpClient mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        Request request = new Request.Builder()
                .url(Constants.ecgUrl)
                .post(requestBody)
                .addHeader("Content-Type", "application/json;charset:utf-8")
                .addHeader("merchantname", "c3p5YzA4MDg=")
                .addHeader("publickey", "e6411Z54MmxxzmFEaRlSuTcO3bCmE78U75QFHUedZw")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    int error = json.getInt("code");
                    if (error == 200) {
                        go2WebActivity(json);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    */
/**
     * @param
     * @return
     * @description 心电Ai诊断疾病筛查RecycleView
     * @author zy
     * @time 2020/12/16 17:08
     *//*

    private void setRecycleView() {
        // recycle设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 创建Adapter
        EcgAiDiagnoseAdapter mEcgAiDiagnoseAdapter = new EcgAiDiagnoseAdapter(R.layout.item_ecg_ai_diagnosis);
        // 添加数据
        mEcgAiDiagnoseAdapter.addData(lists);
        // recycle设置adapter
        mRecyclerView.setAdapter(mEcgAiDiagnoseAdapter);
        // RecyclerView需要内嵌到NextScrollview，为了解决卡顿问题RecyclerView.setNestedScrollingEnabled(false)
        mRecyclerView.setNestedScrollingEnabled(false);

        mEcgAiDiagnoseAdapter.setOnItemClickListener(new EcgAiDiagnoseAdapter.OnItemClickListener() {
            @Override
            public void onClick(EcgAiDiagnoseItemBean hisSearch, int position) {
                Intent intent = new Intent(EcgAiDiagnoseActivity.this, AiWebActivity.class);
                intent.putExtra("title", lists.get(position).getType().split(":")[0]);
                intent.putExtra("url", lists.get(position).getUrl());
                startActivity(intent);
            }
        });
    }

    */
/**
     * 跳转到康源查看 AI 诊断
     *
     * @param msg JSONObject
     *//*

    private void go2WebActivity(JSONObject msg) {
*/
/*        if (progressDialog != null) {
            progressDialog.dismiss();
        }*//*

        if (msg != null) {
            Intent intent = new Intent(context, AiWebActivity.class);
            intent.putExtra("title", "心电图AI辅诊报告");
            try {
                intent.putExtra("url", msg.getJSONObject("data").getString("ViewUrl"));//http://prepare.kangyuanai.com/api/yc_ecg_report/userEcgReportThirdComViewId/record_id/2715027 测试收费地址
            } catch (Exception e) {
                e.printStackTrace();
            }
            startActivity(intent);
        } else {
            Tools.showAlert3(context, "保存失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mUnbinder != null) {
            this.mUnbinder.unbind();
        }
    }
}
*/
