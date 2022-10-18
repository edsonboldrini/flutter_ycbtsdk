package com.example.ycblesdkdemo.ecg.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ycblesdkdemo.R;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;
import com.yucheng.ycbtsdk.Constants;
import com.yucheng.ycbtsdk.YCBTClient;

import androidx.core.content.res.ResourcesCompat;

/**
 * @author: zhongying
 * @date: 2020/11/23
 * @Description: 自定义心电测量选左手右手Dialog
 */
public class EcgMeasurDialog extends BaseDialog<EcgMeasurDialog> {

    private RadioGroup mRadioGroup;
    private RadioButton leftRb;
    private RadioButton rightRb;
    private TextView confirm;
    private ImageView tips;

    private int isLeftRight = 0; // 0 左手 1 右手


    private OnDialogButtonClickListener buttonClickListner;

    //实现回调功能
    public interface OnDialogButtonClickListener {
        public void onLeftClick();

        public void onRightClick();
    }

    public void setOnButtonClickListener(OnDialogButtonClickListener listener) {
        this.buttonClickListner = listener;
    }

    public EcgMeasurDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {

        widthScale(0.85f);
        // 设置显示动画
        //showAnim(new Swing());
        // 设置隐藏动画
        //dismissAnim(this, new ZoomOutExit());
        View inflate = View.inflate(getContext(), R.layout.dialog_ecg_measur, null);
        mRadioGroup = inflate.findViewById(R.id.radio_group);
        leftRb = inflate.findViewById(R.id.rb_left);
        rightRb = inflate.findViewById(R.id.rb_right);
        confirm = inflate.findViewById(R.id.tv_confirm);
        // 提示图片
        tips = inflate.findViewById(R.id.iv_tips);
        // 是否有心电右电极位置配置
        if (YCBTClient.isSupportFunction(Constants.FunctionConstant.ISHASECGRIGHTELECTRODE)) {
            tips.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.mipmap.ecg_tips_pic_two, null));
        }

        inflate.setBackgroundDrawable(
                CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));
        return inflate;
    }

    @Override
    public void setUiBeforShow() {

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_left:
                        isLeftRight = 0;
                        break;
                    case R.id.rb_right:
                        isLeftRight = 1;
                        break;
                    default:
                        ;
                }
            }
        });
        // 确定点击事件
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLeftRight == 0) {
                    buttonClickListner.onLeftClick();
                    //Toast.makeText(getContext(), "你选择的是:左手", Toast.LENGTH_SHORT).show();
/*                    YCBTClient.settingHandWear(0x00, new BleDataResponse() {
                        @Override
                        public void onDataResponse(int code, float ratio, HashMap resultMap) {

                        }
                    });*/
                } else {
                    buttonClickListner.onRightClick();
                    //Toast.makeText(getContext(), "你选择的是:右手", Toast.LENGTH_SHORT).show();
/*                    YCBTClient.settingHandWear(0x01, new BleDataResponse() {
                        @Override
                        public void onDataResponse(int code, float ratio, HashMap resultMap) {

                        }
                    });*/

                }
                dismiss();
            }
        });

        return;
    }
}
