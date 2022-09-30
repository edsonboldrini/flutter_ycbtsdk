package com.example.ycblesdkdemo.ecg.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cowork16 on 2017/8/30.
 */

public class CardiographView extends View {
    Context context;
    public List<Integer> plist = new ArrayList<>();
    //画笔
    protected Paint mPaint;
    //折现的颜色
    protected int mLineColor = Color.parseColor("#FB3159");
    //自身的大小
    protected int mWidth, mHeight;
    public int WidthDots = 0; ////add by shl 2018-08-22
    public int HightDots = 0; ////add by shl 2018-08-22
    public int Is_Ver_or_Hor = 0; //竖屏还是横屏

    public static final int state = 10;//标准为10 1mv == 10mm,  翻倍为20 1mv == 20mm

    //心电图折现
    protected Path mPath;

    public CardiographView(Context context) {
        this(context, null);
    }

    public CardiographView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardiographView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initList();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPath = new Path();
    }

    public void initList() {
        plist.clear();
        mWidth = getWidth();
        mHeight = getHeight();
        System.out.println("chong----------screenWidth=" + mWidth + " screenHeight" + mHeight);
        WidthDots = (int) (mWidth / CarBgView.getMultiple(context) / 3);//3个点取一个平均值 所以点数需要除以3
        HightDots = (int) (mHeight / CarBgView.getMultiple(context));
        if (mWidth > mHeight) {
            Is_Ver_or_Hor = 1;//屏幕横置
        } else {
            Is_Ver_or_Hor = 0;//屏幕竖置
        }
        System.out.println("shl Is_Ver_or_Hor=" + Is_Ver_or_Hor + " " + WidthDots);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void initBackground(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        //设置画笔style
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(1.25f * context.getResources().getDisplayMetrics().density);
        // 重置path
        mPath.reset();
        //用path模拟一个心电图样式
        mPath.reset();
        mPath.moveTo(0, mHeight / 2);
        for (int i = 0; i < plist.size(); i++) {
            Integer py = plist.get(i);
            //每一个小格高度的值为 state * CarBgView.getSGridWidth() / 1000.0f
            mPath.lineTo(i * 3 * CarBgView.getMultiple(context), mHeight / 2 - py * state * CarBgView.getSGridWidth() / 1000.0f);
        }
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initBackground(canvas);
    }
}