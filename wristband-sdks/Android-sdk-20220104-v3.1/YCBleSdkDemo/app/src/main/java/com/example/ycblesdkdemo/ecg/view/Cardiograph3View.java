package com.example.ycblesdkdemo.ecg.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.example.ycblesdkdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengchong on 2020/4/25.
 */

public class Cardiograph3View extends View {
    private Context context;
    private List<Integer> plist = new ArrayList<>();
    private String userName, sex, age, heart, bp;

    //画笔
    protected Paint mPaint;
    //示例颜色
    protected int mInitColor = Color.parseColor("#000000");
    //折现的颜色
    protected int mLineColor = Color.parseColor("#FB3159");
    //网格颜色
    protected int mGridColor = Color.parseColor("#D9D9D9");
    //小网格颜色
    protected int mSGridColor = Color.parseColor("#F0F0F0");
    //每隔一秒画一条竖线
    private int mSecLineColor = Color.parseColor("#8C8C8C");
    private static final float MULTIPLE = 0.5f;//网格倍数
    public static final int state = 10;//标准为10 1mv == 10mm,  翻倍为20 1mv == 20mm
    //自身的大小
    protected int mWidth, mHeight;

    //网格宽度
    protected float mGridWidth;
    //小网格的宽度
    protected float mSGridWidth;
    //心电图折现
    protected Path mPath;

    public Cardiograph3View(Context context) {
        this(context, null);
    }

    public Cardiograph3View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Cardiograph3View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPath = new Path();
    }

    public void setDatas(List<Integer> native_list, String userName, String sex, String age, String heart, String bp) {
        this.userName = userName;
        this.sex = sex;
        this.age = age;
        this.heart = heart;
        this.bp = bp;
        boolean isFlag = false;
        if (native_list != null) {
            plist.clear();
            plist.addAll(native_list);
        }
    }

    public List<Integer> getDatas() {
        return this.plist;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;//(5 * 10 + 4) * mGridWidth;//w; 1秒5大格, 一行10秒 左右空出2大格
        mHeight = h;//(4 * 6 + 4) * mGridWidth;//h; 一行高度4大格, 60秒共6行 上下各2大格
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void initBackground(Canvas canvas) {
        mSGridWidth = (10 * getMultiple(context));//小网格的宽度
        mGridWidth = 5 * mSGridWidth;//网格宽度
        canvas.drawColor(Color.WHITE);
        //画小网格

        //竖线个数
        int vSNum = (int) (mWidth / mSGridWidth);
        //横线个数
        int hSNum = (int) (mHeight / mSGridWidth);
        mPaint.setColor(mSGridColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        //画竖线
        for (int i = 0; i < vSNum + 1; i++) {
            canvas.drawLine(i * mSGridWidth, 0, i * mSGridWidth, mHeight, mPaint);
        }
        //画横线
        for (int i = 0; i < hSNum + 1; i++) {
            canvas.drawLine(0, i * mSGridWidth, mWidth, i * mSGridWidth, mPaint);
        }

        //竖线个数
        int vNum = (int) (mWidth / mGridWidth);
        //横线个数
        int hNum = (int) (mHeight / mGridWidth);
        mPaint.setColor(mGridColor);
        mPaint.setStrokeWidth(3);
        //画横线
        for (int i = 0; i < hNum + 1; i++) {
            if (i == 0 || i == hNum) {
                mPaint.setColor(mInitColor);
            } else {
                mPaint.setColor(mGridColor);
            }
            canvas.drawLine(0, i * mGridWidth, mWidth, i * mGridWidth, mPaint);
        }
        //画竖线
        for (int i = 0; i < vNum + 1; i++) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(3);
            if (i == 0 || i == vNum) {
                mPaint.setColor(mInitColor);
            } else {
                mPaint.setColor(mGridColor);
            }
            canvas.drawLine(i * mGridWidth, 0, i * mGridWidth, mHeight, mPaint);
            if ((i - 2) % 5 == 0) {
                mPaint.setColor(mSecLineColor);
                canvas.drawLine(i * mGridWidth, 2 * mGridWidth, i * mGridWidth, mHeight - 2 * mGridWidth, mPaint);

                mPaint.setColor(mInitColor);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setStrokeWidth(1);
                mPaint.setTextSize(10 * getResources().getDisplayMetrics().density);
                canvas.drawText((i - 2) / 5 + "s", i * mGridWidth + 0.5f * mSGridWidth, mHeight - 2 * mGridWidth, mPaint);
            }
        }

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mInitColor);
        mPaint.setStrokeWidth(3);
        // 重置path
        mPath.reset();
        //用path模拟一个心电图样式
        mPath.moveTo(0, 4 * mGridWidth);
        mPath.lineTo(2 * mSGridWidth, 4 * mGridWidth);
        mPath.lineTo(2 * mSGridWidth, 2 * mGridWidth);
        mPath.lineTo(2 * mSGridWidth + mGridWidth, 2 * mGridWidth);
        mPath.lineTo(2 * mSGridWidth + mGridWidth, 4 * mGridWidth);
        mPath.lineTo(4 * mSGridWidth + mGridWidth, 4 * mGridWidth);
        canvas.drawPath(mPath, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(12 * getResources().getDisplayMetrics().density);
        canvas.drawText(context.getString(R.string.ecg_header_title), 2 * mGridWidth, mHeight - mGridWidth, mPaint);
        canvas.drawText(context.getString(R.string.login_user_name) + ":" + userName + "    "//userName, sex, age, heart, bp
                        + context.getString(R.string.sex_text) + ":" + sex + "    "
                        + context.getString(R.string.age_text) + ":" + age + "    "
                        + context.getString(R.string.function_heart) + ":" + (("0".equals(heart) || heart == null || "-1".equals(heart)) ? "--" : heart) + "    "
                        + context.getString(R.string.function_bp) + ":" + ((bp == null || bp.equals("0/0") || bp.equals("0")) ? "--" : bp),
                2 * mGridWidth, mGridWidth, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(1.25f * context.getResources().getDisplayMetrics().density);

        float startX = 0;
        float startY = 2 * mGridWidth;
        for (int i = 1; i < plist.size(); i++) {
            //一行的数据量为2500/3  采样率250  一行10秒  3个数据取平均值为一个
            switch (i) {
                case 2500 / 3:
                case 2500 * 2 / 3:
                case 2500 * 3 / 3:
                case 2500 * 4 / 3:
                case 2500 * 5 / 3:
                    startX = (i - 1) * 3 * getMultiple(context);
                    startY += 4 * mGridWidth;
                    break;
                case 2500 * 6 / 3:
                    return;
                default:
                    Integer py1 = plist.get(i - 1);
                    Integer py2 = plist.get(i);
                    canvas.drawLine(2 * mGridWidth + (i - 1) * 3 * getMultiple(context) - startX, 4 * mGridWidth / 2 - py1 * state * mSGridWidth / 1000.0f + startY,
                            2 * mGridWidth + i * 3 * getMultiple(context) - startX, 4 * mGridWidth / 2 - py2 * state * mSGridWidth / 1000.0f + startY, mPaint);
                    break;
            }
        }
    }

    public static float getMultiple(Context context) {
        return context.getResources().getDisplayMetrics().density * MULTIPLE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initBackground(canvas);
    }
}