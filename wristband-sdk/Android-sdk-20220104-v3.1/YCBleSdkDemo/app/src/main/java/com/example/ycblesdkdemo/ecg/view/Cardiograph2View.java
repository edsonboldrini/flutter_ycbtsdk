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
 * Created by cowork16 on 2017/8/30.
 */

public class Cardiograph2View extends View {
    private Context context;
    private boolean isHome = false;
    private List<Integer> plist = new ArrayList<>();

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
    protected int mGridWidth;
    //小网格的宽度
    protected int mSGridWidth;
    //心电图折现
    protected Path mPath;

    public Cardiograph2View(Context context) {
        this(context, null);
    }

    public Cardiograph2View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Cardiograph2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPath = new Path();
    }

    public void setDatas(List<Integer> data, boolean isHome) {
        this.isHome = isHome;
        boolean isFlag = false;
        if (data != null) {
            plist.clear();
            plist.addAll(data);
        }
    }

    public List<Integer> getDatas() {
        return this.plist;
    }

    public void make(int w) {
        mWidth = w;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public static float getMultiple(Context context) {
        return context.getResources().getDisplayMetrics().density * MULTIPLE;
    }

    public void initBackground(Canvas canvas) {
        mSGridWidth = (int) (10 * getMultiple(context));//小网格的宽度
        mGridWidth = 5 * mSGridWidth;//网格宽度

        canvas.drawColor(Color.WHITE);
        //画小网格

        //竖线个数
        int vSNum = mWidth / mSGridWidth;
        //横线个数
        int hSNum = mHeight / mSGridWidth / 5 * 5;
        mPaint.setColor(mSGridColor);
        mPaint.setStrokeWidth(2);
        //画竖线
        for (int i = 0; i < vSNum + 1; i++) {
            canvas.drawLine(i * mSGridWidth, 0, i * mSGridWidth, hSNum * mSGridWidth, mPaint);
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
            canvas.drawLine(0, i * mGridWidth, mWidth, i * mGridWidth, mPaint);
        }
        //画竖线
        for (int i = 0; i < vNum + 1; i++) {
            mPaint.setColor(mGridColor);
            canvas.drawLine(i * mGridWidth, 0, i * mGridWidth, hNum * mGridWidth, mPaint);
            if (i != 0 && i % 5 == 0) {
                mPaint.setColor(mSecLineColor);
                canvas.drawLine(i * mGridWidth, 0, i * mGridWidth, hNum * mGridWidth, mPaint);
            }
        }

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mInitColor);
        mPaint.setStrokeWidth(3);
        // 重置path
        mPath.reset();
        //用path模拟一个心电图样式
        mPath.moveTo(0, 3 * mGridWidth);
        mPath.lineTo(2 * mSGridWidth, 3 * mGridWidth);
        mPath.lineTo(2 * mSGridWidth, mGridWidth);
        mPath.lineTo(2 * mSGridWidth + mGridWidth, mGridWidth);
        mPath.lineTo(2 * mSGridWidth + mGridWidth, 3 * mGridWidth);
        mPath.lineTo(4 * mSGridWidth + mGridWidth, 3 * mGridWidth);
        canvas.drawPath(mPath, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(12 * getResources().getDisplayMetrics().density);
        if (!isHome) {
            canvas.drawText(context.getString(R.string.ecg_header_title), 2 * mSGridWidth, (hNum - 2) * mGridWidth, mPaint);
        } else {
            canvas.drawText(context.getString(R.string.ecg_header_title), 2 * mSGridWidth, (hNum - 1) * mGridWidth, mPaint);
        }

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(1.25f * context.getResources().getDisplayMetrics().density);
        for (int i = 0; i < plist.size(); i++) {
            if (i > 0) {
                Integer py1 = plist.get(i - 1);
                Integer py2 = plist.get(i);
                canvas.drawLine((i - 1) * 3 * getMultiple(context), mHeight / 2 / mGridWidth * mGridWidth - py1 * state * mSGridWidth / 1000.0f,
                        i * 3 * getMultiple(context), mHeight / 2 / mGridWidth * mGridWidth - py2 * state * mSGridWidth / 1000.0f, mPaint);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initBackground(canvas);
    }
}