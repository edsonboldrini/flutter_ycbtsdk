package com.example.ycblesdkdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yucheng.ycbtsdk.YCBTClient;
import com.yucheng.ycbtsdk.response.BleDataResponse;

import java.util.HashMap;

import static com.yucheng.ycbtsdk.Constants.BLEState.ReadWriteOK;

/**
 * @author StevenLiu
 * @date 2021/4/27
 * @desc one word for this class
 */
public class TestBoolActivity extends Activity {
    private EditText height, weight, sex, bool_high, bool_low, heart, age;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bool);
        init();
    }

    private void init() {
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        sex = findViewById(R.id.sex);
        bool_high = findViewById(R.id.bool_high);
        bool_low = findViewById(R.id.bool_low);
        heart = findViewById(R.id.heart);
        start = findViewById(R.id.start);
        age = findViewById(R.id.age);
        start.setOnClickListener(new OnClickListenerImpl());
    }

    private class OnClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.start:
                    if (YCBTClient.connectState() == ReadWriteOK) {
                        YCBTClient.appStartBloodMeasurement(0, bool_high.getEditableText().toString().trim().equals("") ? 0 : Integer.parseInt(bool_high.getEditableText().toString().trim()),
                                bool_low.getEditableText().toString().trim().equals("") ? 0 : Integer.parseInt(bool_low.getEditableText().toString().trim()),
                                heart.getEditableText().toString().trim().equals("") ? 0 : Integer.parseInt(heart.getEditableText().toString().trim()),
                                height.getEditableText().toString().trim().equals("") ? 0 : Integer.parseInt(height.getEditableText().toString().trim()),
                                weight.getEditableText().toString().trim().equals("") ? 0 : Integer.parseInt(weight.getEditableText().toString().trim()),
                                age.getEditableText().toString().trim().equals("") ? 0 : Integer.parseInt(age.getEditableText().toString().trim()),
                                sex.getEditableText().toString().trim().equals("") ? 0 : Integer.parseInt(sex.getEditableText().toString().trim()),
                                new BleDataResponse() {
                                    @Override
                                    public void onDataResponse(int code, float ratio, HashMap resultMap) {
                                        if (code == 0 && resultMap != null && (int) resultMap.get("typeResult") == 0) {
                                            Toast.makeText(TestBoolActivity.this, "开启成功", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(TestBoolActivity.this, "已断开连接,请重新连接", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
            }
        }
    }
}
