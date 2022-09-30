package com.example.ycblesdkdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ycblesdkdemo.model.RCResponse;
import com.example.ycblesdkdemo.util.TimeUtil;
import com.google.gson.Gson;
import com.yucheng.ycbtsdk.response.BleDataResponse;
import com.yucheng.ycbtsdk.YCBTClient;

import java.util.HashMap;
import java.util.List;

import static com.example.ycblesdkdemo.TimeSetActivity.Tag;

public class RCActivity extends Activity {


    private LinearLayout listContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rc);
        initView();
    }

    private void initView(){
        listContentView = (LinearLayout)this.findViewById(R.id.rc_list_content);

        YCBTClient.getScheduleInfo(new BleDataResponse() {
            @Override
            public void onDataResponse(int code, float ratio, HashMap resultMap) {
                Log.e(Tag, resultMap.toString());
                Log.e(Tag,"code="+code);

                if(code == 0){
                    if(resultMap != null){
                        String backVal = resultMap.toString().trim();
                        Gson gson = new Gson();
                        RCResponse bandBaseInfo = gson.fromJson(backVal, RCResponse.class);

                        Log.e(Tag, bandBaseInfo.toString());

                        if(bandBaseInfo != null && bandBaseInfo.getData() !=null && bandBaseInfo.getData().size() > 0){

                            setData(bandBaseInfo.getData());




                        }
                    }

                }else {
                    Toast.makeText(RCActivity.this,"获取日程失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void setData(final List<RCResponse.RcModel> data){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<data.size();i++){
                    RCResponse.RcModel rcModel = data.get(i);
                    View itemView = LayoutInflater.from(RCActivity.this).inflate(R.layout.rc_item_layout,null);

                    TextView indexView = itemView.findViewById(R.id.rc_item_index);
                    TextView timeView = itemView.findViewById(R.id.rc_item_time);
                    indexView.setText("-"+rcModel.getScheduleIndex()+"-"+""+rcModel.getIncidentIndex());
                    timeView.setText(""+ TimeUtil.timeConverTimes(rcModel.getIncidentTime()));
                    listContentView.addView(itemView);
                }
            }
        });


    }




}
