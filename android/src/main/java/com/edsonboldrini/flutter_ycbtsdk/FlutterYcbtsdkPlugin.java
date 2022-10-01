package com.edsonboldrini.flutter_ycbtsdk;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import com.edsonboldrini.flutter_ycbtsdk.DeviceAdapter;
import com.edsonboldrini.flutter_ycbtsdk.ConnectEvent;

import static com.yucheng.ycbtsdk.Constants.BLEState.ReadWriteOK;

import com.yucheng.ycbtsdk.AITools;
import com.yucheng.ycbtsdk.Constants;
import com.yucheng.ycbtsdk.YCBTClient;
import com.yucheng.ycbtsdk.bean.AIDataBean;
import com.yucheng.ycbtsdk.bean.HRVNormBean;
import com.yucheng.ycbtsdk.bean.ScanDeviceBean;
import com.yucheng.ycbtsdk.response.BleAIDiagnosisHRVNormResponse;
import com.yucheng.ycbtsdk.response.BleAIDiagnosisResponse;
import com.yucheng.ycbtsdk.response.BleConnectResponse;
import com.yucheng.ycbtsdk.response.BleDataResponse;
import com.yucheng.ycbtsdk.response.BleDeviceToAppDataResponse;
import com.yucheng.ycbtsdk.response.BleRealDataResponse;
import com.yucheng.ycbtsdk.response.BleScanResponse;
import com.yucheng.ycbtsdk.utils.YCBTLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * FlutterYcbtsdkPlugin
 */
public class FlutterYcbtsdkPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
	/// The MethodChannel that will the communication between Flutter and native
	/// Android
	///
	/// This local reference serves to register the plugin with the Flutter Engine
	/// and unregister it
	/// when the Flutter Engine is detached from the Activity
	private MethodChannel channel;
	private Context context;
	private Activity activity;
	private String macVal;

	@Override
	public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
		channel.setMethodCallHandler(null);
	}

	@Override
	public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
		activity = binding.getActivity();
	}

	@Override
	public void onDetachedFromActivityForConfigChanges() {

	}

	@Override
	public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

	}

	@Override
	public void onDetachedFromActivity() {

	}

	@Override
	public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
		channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_ycbtsdk");
		channel.setMethodCallHandler(this);

		context = flutterPluginBinding.getApplicationContext();

		EventBus.getDefault().register(this);
		// startService(new Intent(this, MyBleService.class));
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void connectEvent(ConnectEvent connectEvent) {
		Log.e("mainOrder", "connected...");
		// baseOrderSet();
		// Intent timeIntent = new Intent(context, ChoseActivity.class);
		// timeIntent.putExtra("mac", macVal);
		// startActivity(timeIntent);

		// Toast.makeText(MainActivity.this, "connection succeeded",
		// Toast.LENGTH_SHORT).show();
	}

	BleDeviceToAppDataResponse toAppDataResponse = new BleDeviceToAppDataResponse() {

		@Override
		public void onDataResponse(int dataType, HashMap dataMap) {

			Log.e("TimeSetActivity", "被动回传数据。。。");
			Log.e("TimeSetActivity", dataMap.toString());

		}
	};

	boolean isActiveDisconnect = false;
	BleConnectResponse bleConnectResponse = new BleConnectResponse() {
		@Override
		public void onConnectResponse(int code) {
			// Toast.makeText(MyApplication.this, "i222=" + var1,
			// Toast.LENGTH_SHORT).show();

			Log.e("deviceconnect", "全局监听返回=" + code);

			if (code == com.yucheng.ycbtsdk.Constants.BLEState.Disconnect) {
				// thirdConnect = false;
				// BangleUtil.getInstance().SDK_VERSIONS = -1;
				// EventBus.getDefault().post(new BlueConnectFailEvent());
				/*
				 * if(SPUtil.getBindedDeviceMac() != null &&
				 * !"".equals(SPUtil.getBindedDeviceMac())){
				 * YCBTClient.connectBle(SPUtil.getBindedDeviceMac(), new BleConnectResponse() {
				 *
				 * @Override
				 * public void onConnectResponse(int code) {
				 *
				 * }
				 * });
				 * }
				 */
			} else if (code == com.yucheng.ycbtsdk.Constants.BLEState.Connected) {

			} else if (code == com.yucheng.ycbtsdk.Constants.BLEState.ReadWriteOK) {

				// thirdConnect = true;
				// BangleUtil.getInstance().SDK_VERSIONS = 3;
				// Log.e("deviceconnect", "蓝牙连接成功，全局监听");
				// setBaseOrder();
				EventBus.getDefault().post(new ConnectEvent());
			} else {
				// code == Constants.BLEState.Disconnect
				// thirdConnect = false;
				// BangleUtil.getInstance().SDK_VERSIONS = -1;
				// EventBus.getDefault().post(new ConnectEvent());
			}
		}
	};

	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
			Manifest.permission.BLUETOOTH_SCAN,
			Manifest.permission.BLUETOOTH_CONNECT,
			Manifest.permission.BLUETOOTH_PRIVILEGED
	};
	private static String[] PERMISSIONS_LOCATION = {
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
			Manifest.permission.BLUETOOTH_SCAN,
			Manifest.permission.BLUETOOTH_CONNECT,
			Manifest.permission.BLUETOOTH_PRIVILEGED
	};

	private void checkPermissions() {
		Log.e("debug", "checking permissions...");

		int permission1 = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int permission2 = ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN);
		if (permission1 != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(
					activity,
					PERMISSIONS_STORAGE,
					1);
		} else if (permission2 != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(
					activity,
					PERMISSIONS_LOCATION,
					1);
		}
	}

	private List<ScanDeviceBean> listModel = new ArrayList<>();
	private List<String> listVal = new ArrayList<>();
	DeviceAdapter deviceAdapter = new DeviceAdapter(listModel);

	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(@NonNull Message msg) {
			if (msg.what == 0) {
				handler.sendEmptyMessageDelayed(0, 1000);
				YCBTClient.getAllRealDataFromDevice(new BleDataResponse() {
					@Override
					public void onDataResponse(int i, float v, HashMap hashMap) {
						Log.e("debug", hashMap.toString());
					}
				});
			} else if (msg.what == 1) {
				Log.e("debug", "1");
			} else if (msg.what == 2) {
				Log.e("debug", "2");
			} else if (msg.what == 3) {
				Log.e("debug", "3");
			} else if (msg.what == 4) {
				Log.e("debug", "4");
			}
			return false;
		}
	});

	@Override
	public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
		switch (call.method) {
			case "getPlatformVersion":
				result.success("Android " + android.os.Build.VERSION.RELEASE);
				break;
			case "initPlugin": {
				Log.e("device", "initPlugin...");

				checkPermissions();

				YCBTClient.initClient(context, true);
				YCBTClient.registerBleStateChange(bleConnectResponse);
				YCBTClient.deviceToApp(toAppDataResponse);

				break;
			}
			case "startScan": {
				YCBTClient.startScanBle(new BleScanResponse() {
					@Override
					public void onScanResponse(int i, ScanDeviceBean scanDeviceBean) {

						if (scanDeviceBean != null) {
							if (!listVal.contains(scanDeviceBean.getDeviceMac())) {
								listVal.add(scanDeviceBean.getDeviceMac());
								deviceAdapter.addModel(scanDeviceBean);
							}

							Log.e("device", "mac=" + scanDeviceBean.getDeviceMac() + ";name=" + scanDeviceBean.getDeviceName()
									+ "rssi=" + scanDeviceBean.getDeviceRssi());

						}
					}
				}, 6);
				break;
			}
			case "stopScan": {
				YCBTClient.stopScanBle();
				break;
			}
			default:
				result.notImplemented();
				break;
		}
	}
}
