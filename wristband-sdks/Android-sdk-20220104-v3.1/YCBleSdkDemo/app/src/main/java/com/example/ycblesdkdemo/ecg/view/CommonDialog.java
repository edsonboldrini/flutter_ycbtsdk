package com.example.ycblesdkdemo.ecg.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ycblesdkdemo.R;


/**
 * @author: zhongying
 * @date: 2020/12/28
 * @Description: 自定义Dialog
 */
public class CommonDialog extends Dialog {

    private ImageView mImage; // 显示的图片
    private EditText mEditText; // EditText
    private TextView mTitle; // 显示的标题
    private TextView mMessage; // 显示的消息
    private Button mCancel, mConfirm; // 取消和确认按钮
    private View mColumnLineView; // 按钮之间的分割线

    public CommonDialog(Context context) { // 样式
        super(context, R.style.CustomDialog);
    }

    private boolean isSingle = false; // 底部是否只有一个按钮
    // 都是内容数据
    private String message;
    private String title;
    private String cancel, confirm, cancelColor, confirmColor;
    private int imageResId = -1;
    private int editTextResId = -1;
    private int mMessageResId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_dialog_layout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        refreshView();
        //初始化界面控件的事件
        initEvent();
    }

    /**
     * @param
     * @return
     * @description 初始化界面的确定和取消监听器
     * @author zy
     * @time 2020/12/28 10:44
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    if (editTextResId != -1) {
                        onClickBottomListener.onEditTextConfirmClick(mEditText.getText().toString());
                    } else {
                        onClickBottomListener.onConfirmClick();
                    }
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onCancelClick();
                }
            }
        });
    }

    /**
     * @param
     * @return
     * @description 初始化界面控件的显示数据
     * @author zy
     * @time 2020/12/28 10:45
     */
    private void refreshView() {

        if (!TextUtils.isEmpty(title)) { //如果用户自定了title和message
            mTitle.setText(title);
            mTitle.setVisibility(View.VISIBLE);
        } else {
            mTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(message)) {
            mMessage.setText(message);
        }

        if (!TextUtils.isEmpty(confirm)) { //如果设置按钮的文字
            mConfirm.setText(confirm);
        } else {
            mConfirm.setText("确定");
        }
        if (!TextUtils.isEmpty(cancel)) {
            mCancel.setText(cancel);
        } else {
            mCancel.setText("取消");
        }
        // 确定按钮文字颜色
        if (!TextUtils.isEmpty(confirmColor)) {
            mConfirm.setTextColor(Color.parseColor(confirmColor));
        } else {

        }

        // 取消按钮文字颜色
        if (!TextUtils.isEmpty(cancelColor)) {
            mCancel.setTextColor(Color.parseColor(cancelColor));
        } else {

        }

        if (imageResId != -1) {
            mImage.setImageResource(imageResId);
            mImage.setVisibility(View.VISIBLE);
        } else {
            mImage.setVisibility(View.GONE);
        }

        if (editTextResId != -1) {
            mEditText.setVisibility(View.VISIBLE);
        } else {
            mEditText.setVisibility(View.GONE);
        }


        if (mMessageResId != -1) {
            mMessage.setVisibility(View.GONE);
        } else {
            mMessage.setVisibility(View.VISIBLE);
        }

        if (isSingle) { // 只显示一个按钮的时候隐藏取消按钮，回掉只执行确定的事件
            mColumnLineView.setVisibility(View.GONE);
            mCancel.setVisibility(View.GONE);
        } else {
            mCancel.setVisibility(View.VISIBLE);
            mColumnLineView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void show() {
        super.show();
        refreshView();
    }

    /**
     * @param
     * @return
     * @description 初始化界面控件
     * @author zy
     * @time 2020/12/28 11:00
     */
    private void initView() {
        mCancel = (Button) findViewById(R.id.cancel);
        mConfirm = (Button) findViewById(R.id.confirm);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mMessage = (TextView) findViewById(R.id.tv_message);
        mImage = (ImageView) findViewById(R.id.iv_image);
        mEditText = (EditText) findViewById(R.id.ed_text);
        mColumnLineView = findViewById(R.id.column_line);
    }

    /**
     * @description 设置确定取消按钮的回调
     * @param
     * @return
     * @author zy
     * @time 2020/12/28 10:45
     */
    public OnClickBottomListener onClickBottomListener;

    public CommonDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    public interface OnClickBottomListener {

        public void onConfirmClick(); // 点击确定按钮事件

        public void onCancelClick(); //点击取消按钮事件

        public void onEditTextConfirmClick(String mEditText); // 点击EditText确定按钮事件

    }

    public String getMessage() {
        return message;
    }

    public CommonDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CommonDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getConfirm() {
        return confirm;
    }

    public CommonDialog setConfirm(String confirm) {
        this.confirm = confirm;
        return this;
    }

    /*设置确认按钮文字颜色*/
    public CommonDialog setConfirmColor(String confirmColor) {
        this.confirmColor = confirmColor;
        return this;
    }

    /*设置取消按钮文字颜色*/
    public CommonDialog setCancelColor(String cancelColor) {
        this.cancelColor = cancelColor;
        return this;
    }

    public String getCancel() {
        return cancel;
    }

    public CommonDialog setCancel(String cancel) {
        this.cancel = cancel;
        return this;
    }


    public boolean isSingle() {
        return isSingle;
    }

    public CommonDialog setSingle(boolean single) {
        isSingle = single;
        return this;
    }

    public int getImageResId() {
        return imageResId;
    }

    public CommonDialog setImageResId(int imageResId) {
        this.imageResId = imageResId;
        return this;
    }

    public int getEditTextResId() {
        return editTextResId;
    }

    public CommonDialog setEditTextResId(int editTextResId) {
        this.editTextResId = editTextResId;
        return this;
    }

    public int getMessageResId() {
        return mMessageResId;
    }

    public CommonDialog setMessageResId(int mMessageResId) {
        this.mMessageResId = mMessageResId;
        return this;
    }


}
