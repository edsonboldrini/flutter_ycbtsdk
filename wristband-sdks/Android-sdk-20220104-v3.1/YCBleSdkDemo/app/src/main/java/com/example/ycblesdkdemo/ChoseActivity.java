package com.example.ycblesdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ycblesdkdemo.ecg.EcgActicvity;

public class ChoseActivity extends Activity {

    private TextView newBtn;
    private TextView oldBtn;
    private TextView otherView;
    TextView dfuUpdate;
    private String macVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose);

        macVal = getIntent().getStringExtra("mac");

        initView();
    }

    private void initView(){

        newBtn = (TextView)this.findViewById(R.id.chose_new_view);
        oldBtn = (TextView)this.findViewById(R.id.chose_old_view);
        otherView = (TextView)this.findViewById(R.id.chose_other_view);
        dfuUpdate = (TextView)this.findViewById(R.id.dfu_update_ss);

        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(ChoseActivity.this,TimeSetActivity.class);
                startActivity(newIntent);
            }
        });


        oldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oldIntent = new Intent(ChoseActivity.this,OldActivity.class);
                startActivity(oldIntent);
            }
        });

        otherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oldIntent = new Intent(ChoseActivity.this,OtherActivity.class);
                startActivity(oldIntent);
            }
        });

        dfuUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oldIntent = new Intent(ChoseActivity.this,DfuUpdateActivity.class);
                oldIntent.putExtra("mac",macVal);
                startActivity(oldIntent);
            }
        });

        findViewById(R.id.chose_ecg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChoseActivity.this, EcgActicvity.class));
            }
        });
    }


}
