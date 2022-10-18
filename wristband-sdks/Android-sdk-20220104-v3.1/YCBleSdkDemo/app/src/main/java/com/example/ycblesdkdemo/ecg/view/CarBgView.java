package com.example.ycblesdkdemo.ecg.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.example.ycblesdkdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cowork16 on 2017/8/30.
 */

public class CarBgView extends View {
    Context context;
    public List<Integer> plist = new ArrayList<>();
    //画笔
    protected Paint mPaint;
    //网格颜色
    protected int mGridColor = Color.parseColor("#D9D9D9");
    //小网格颜色
    protected int mSGridColor = Color.parseColor("#F0F0F0");
    //示例颜色
    protected int mInitColor = Color.parseColor("#000000");
    //每隔一秒画一条竖线
    private int mSecLineColor = Color.parseColor("#8C8C8C");

    //背景颜色
    protected int mBackgroundColor = Color.WHITE;
    //自身的大小
    protected int mWidth, mHeight;

    private static int mGridWidth;//网格宽度
    private static int mSGridWidth;//小网格的宽度
    private static final float MULTIPLE = 0.5f;//网格倍数

    int screenHeight = 0;
    //心电图折现
    private Path mPath;

    public CarBgView(Context context) {
        this(context, null);
    }

    public CarBgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarBgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        // 方法1 Android获得屏幕的宽和高
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        float scale = context.getResources().getDisplayMetrics().density;
        for (int i = 0; i < (int) (screenWidth / scale); i++) {
            plist.add(0);
        }
        mPaint = new Paint();
        mPath = new Path();
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

    public static int getSGridWidth() {
        return mSGridWidth;
    }

    private void initBackground(Canvas canvas) {
        mSGridWidth = (int) (10 * getMultiple(context));//小网格的宽度
        mGridWidth = 5 * mSGridWidth;//网格宽度

        canvas.drawColor(mBackgroundColor);
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
        int vNum = mWidth / mGridWidth;
        //横线个数
        int hNum = mHeight / mGridWidth;
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
        canvas.drawText(context.getString(R.string.ecg_header_title), 2 * mSGridWidth, (hNum - 1) * mGridWidth, mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initBackground(canvas);
    }

}