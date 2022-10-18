package com.example.ycblesdkdemo.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
	private Context context;
	private static ToastUtil toastUtil;

	public static ToastUtil getInstance(Context context){
		if(toastUtil == null)
			toastUtil = new ToastUtil(context);
		return toastUtil;
	}

	private ToastUtil(Context context) {
		if (context != null)
			this.context = context;
	}
	
	public void toast(String str){
		Toast toast = Toast.makeText(this.context, str, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,toast.getYOffset() / 2);
		toast.show();
	}

}
