package com.example.ycblesdkdemo.view;


import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ycblesdkdemo.R;


public class NavigationBar extends LinearLayout {
    private TextView titletxt;
    private ImageButton nav_left_btn;
    private Button nav_left_btn2;
    private Button nav_right_btn1;
    private ImageView nav_right_btn2;
    private Button nav_right_btn3;


    private View bgview;
    private View bgline;

    private MyOnClickListener leftlistener;
    private MyOnClickListener leftlistener2;
    private MyOnClickListener rightlistener1;
    private MyOnClickListener rightlistener2;
    private MyOnClickListener rightlistener3;

    public NavigationBar(Context context) {
        super(context);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.navigationbar, this);
        bgview = findViewById(R.id.bgview);

        bgline = findViewById(R.id.bgline);
        //bgview.setBackgroundColor(Color.parseColor("#0e74f2"));
        //backColor(1);

        titletxt = (TextView) findViewById(R.id.nav_title);
        TextPaint tp = titletxt.getPaint();
        //tp.setFakeBoldText(true);

        nav_left_btn = (ImageButton) findViewById(R.id.nav_btn_left);
        nav_left_btn.setVisibility(View.INVISIBLE);
        nav_left_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickleft((ImageButton) v);
            }
        });

        nav_left_btn2 = (Button) findViewById(R.id.nav_btn_left2);
        nav_left_btn2.setVisibility(View.INVISIBLE);
        nav_left_btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                clickleft2((Button) v);
            }
        });


        nav_right_btn1 = (Button) findViewById(R.id.nav_btn_right1);
        nav_right_btn1.setVisibility(View.INVISIBLE);
        nav_right_btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                clickright1((Button) v);
            }
        });

        nav_right_btn2 = (ImageView) findViewById(R.id.nav_btn_right2);
        nav_right_btn2.setVisibility(View.INVISIBLE);
        nav_right_btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                clickright2((ImageView) v);
            }
        });

        nav_right_btn3 = (Button) findViewById(R.id.nav_btn_right3);
        nav_right_btn3.setVisibility(View.INVISIBLE);
        nav_right_btn3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                clickright3((Button) v);
            }
        });
    }

    public void isLeftBtnClickAble(boolean clickAble) {
        if (clickAble) {
            nav_left_btn.setClickable(true);
        } else {
            nav_left_btn.setClickable(false);
        }
    }

    public void setBackgroundColor(String color) {
        bgview.setBackgroundColor(Color.parseColor(color));
    }

    public void setBackgroundColor(int color) {
        bgview.setBackgroundColor(color);
    }

    public void showBarleftbtn(boolean show) {
        if (show) {
            nav_left_btn2.setVisibility(View.VISIBLE);
        } else {
            nav_left_btn2.setVisibility(View.INVISIBLE);
        }
    }

    public void setShuaxinIcon(boolean isok) {
//		if (isok) {
//			nav_left_btn2.setImageResource(R.drawable.shuaxinl);
//		}else{
//			nav_left_btn2.setImageResource(R.drawable.shuaxin);
//		}
    }

//	public void backColor(int type){
//		if (type == 1) {
//			bgview.setBackgroundColor(Color.parseColor("#46a719"));
//		}else{
//			bgview.setBackgroundColor(Color.parseColor("#9f3cae"));
//		}
//	}

    public void setTitle(String str) {
        titletxt.setText(str);
    }

    public void setTitleColor(int color) {
        titletxt.setTextColor(color);
    }

    public void showLeftBtn(Boolean show) {
        if (show) {
            nav_left_btn.setVisibility(View.VISIBLE);
        } else {
            nav_left_btn.setVisibility(View.INVISIBLE);
        }
    }

    public void showShareButtonVisualable(boolean isShow) {
        if (isShow) {
            nav_right_btn2.setVisibility(VISIBLE);
        } else {
            nav_right_btn2.setVisibility(INVISIBLE);
        }
    }

    public void showLeftbtn(int type) {
        switch (type) {
            case 0:
                nav_left_btn.setVisibility(View.VISIBLE);
                nav_left_btn2.setVisibility(View.INVISIBLE);
                break;
            case 1:
                nav_left_btn2.setVisibility(View.VISIBLE);
                nav_left_btn.setVisibility(View.INVISIBLE);

            default:
                break;
        }
    }

    public void setLeftBtnImage(int imageId) {
        nav_left_btn.setImageResource(imageId);
    }

    public void showRightbtn(int type) {
        switch (type) {
            case 0:
                nav_right_btn1.setVisibility(View.VISIBLE);
                nav_right_btn2.setVisibility(View.INVISIBLE);
                nav_right_btn3.setVisibility(View.INVISIBLE);
                break;
            case 1:
                nav_right_btn2.setVisibility(View.VISIBLE);
                nav_right_btn1.setVisibility(View.INVISIBLE);
                nav_right_btn3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                nav_right_btn2.setVisibility(View.INVISIBLE);
                nav_right_btn1.setVisibility(View.VISIBLE);
                nav_right_btn3.setVisibility(View.VISIBLE);
                break;
            case 3:
                nav_right_btn2.setVisibility(View.INVISIBLE);
                nav_right_btn1.setVisibility(View.INVISIBLE);
                nav_right_btn3.setVisibility(View.INVISIBLE);
                break;
            case 4://只显示保�?
                nav_right_btn2.setVisibility(View.INVISIBLE);
                nav_right_btn1.setVisibility(View.INVISIBLE);
                nav_right_btn3.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public void setBackgroundColor1(String color) {
        bgview.setBackgroundColor(Color.parseColor(color));
    }

    public void setRightImage(int imageid) {
        showRightbtn(1);
        nav_right_btn2.setImageResource(imageid);
    }

    public void showRightImage() {
        nav_right_btn2.setVisibility(VISIBLE);
    }

    public void closeRightImage() {
        nav_right_btn2.setVisibility(INVISIBLE);
    }

    public void setRightText(String txt) {
        showRightbtn(0);
        nav_right_btn1.setText(txt);
    }

    public void setRightText(String txt, MyOnClickListener listener) {
        showRightbtn(0);
        nav_right_btn1.setText(txt);
        setRight1OnClickListener(listener);
    }

    public void setRightEnable(boolean enable) {
        nav_right_btn2.setEnabled(enable);
        if (!enable) {
            nav_right_btn1.setTextColor(Color.LTGRAY);
        } else {
            nav_right_btn1.setTextColor(Color.BLACK);
        }
    }

    public void setLeftBtn(String str) {
        nav_left_btn2.setText(str);
    }

    public void clickleft(ImageButton btn) {
        this.leftlistener.onClick(btn);
    }

    public void setLeftOnClickListener(MyOnClickListener listener) {
        this.leftlistener = listener;
    }


    public void clickleft2(Button btn) {
        this.leftlistener2.onClick(btn);
    }

    public void setLeft2OnClickListener(MyOnClickListener listener) {
        this.leftlistener2 = listener;
    }


    public void clickright1(Button btn) {
        this.rightlistener1.onClick(btn);
    }

    public void setRight1OnClickListener(MyOnClickListener listener) {
        this.rightlistener1 = listener;
    }

    public void clickright2(ImageView btn) {
        this.rightlistener2.onClick(btn);
    }

    public void setRight2OnClickListener(MyOnClickListener listener) {
        this.rightlistener2 = listener;
    }


    public void clickright3(Button btn) {
        this.rightlistener3.onClick(btn);
    }

    public void setRight3OnClickListener(MyOnClickListener listener) {
        this.rightlistener3 = listener;
    }

    public interface MyOnClickListener {
        void onClick(View btn);
    }

    public void showBgLine(Boolean show) {
        if (show) {
            bgline.setVisibility(View.VISIBLE);
        } else {
            bgline.setVisibility(View.INVISIBLE);
        }
    }

    public void setLeftImage(int image) {
        showLeftbtn(0);
        nav_left_btn.setImageResource(image);
    }
}