package com.edsonboldrini.flutter_ycbtsdk;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.EventChannel.EventSink;
import io.flutter.plugin.common.EventChannel.StreamHandler;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import com.yucheng.ycbtsdk.YCBTClient;
import com.yucheng.ycbtsdk.bean.ScanDeviceBean;
import com.yucheng.ycbtsdk.response.BleConnectResponse;
import com.yucheng.ycbtsdk.response.BleDataResponse;
import com.yucheng.ycbtsdk.response.BleDeviceToAppDataResponse;
import com.yucheng.ycbtsdk.response.BleScanResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	private static final String TAG = "FlutterYCBTSDK";
	private static final String NAMESPACE = "flutter_ycbtsdk";
	private MethodChannel channel;
	private EventChannel stateChannel;
	private Context context;
	private Activity activity;
	private String macVal;

	private final Object initializationLock = new Object();
	private FlutterPluginBinding pluginBinding;
	private ActivityPluginBinding activityBinding;

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
		Log.d(TAG, "onAttachedToEngine");
		pluginBinding = flutterPluginBinding;
		setup(pluginBinding.getBinaryMessenger(), (Application) pluginBinding.getApplicationContext());
	}

	private void setup(final BinaryMessenger messenger, final Application application) {
		synchronized (initializationLock) {
			Log.d(TAG, "setup");
			context = application;
			channel = new MethodChannel(messenger, NAMESPACE + "/methods");
			channel.setMethodCallHandler(this);
			stateChannel = new EventChannel(messenger, NAMESPACE + "/state");
			stateChannel.setStreamHandler((StreamHandler) stateHandler);

			EventBus.getDefault().register(this);
			// startService(new Intent(this, MyBleService.class));
		}
	}

	private final StreamHandler stateHandler = new EventChannel.StreamHandler() {
		private EventSink sink;

		private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				final String action = intent.getAction();
			}
		};

		@Override
		public void onListen(Object o, EventSink eventSink) {
			sink = eventSink;
			IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
			context.registerReceiver(mReceiver, filter);
		}

		@Override
		public void onCancel(Object o) {
			sink = null;
			context.unregisterReceiver(mReceiver);
		}
	};

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void connectEvent(ConnectEvent connectEvent) {
		Log.e(TAG, "connected...");
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

			Log.e(TAG, "被动回传数据。。。");
			Log.e(TAG, dataMap.toString());

		}
	};

	boolean isActiveDisconnect = false;
	BleConnectResponse bleConnectResponse = new BleConnectResponse() {
		@Override
		public void onConnectResponse(int code) {
			// Toast.makeText(MyApplication.this, "i222=" + var1,
			// Toast.LENGTH_SHORT).show();

			Log.e(TAG, "全局监听返回=" + code);

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
				// Log.e(TAG, "蓝牙连接成功，全局监听");
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

	private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_PRIVILEGED};
	private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_PRIVILEGED};

	private void checkPermissions() {
		Log.e(TAG, "checking permissions...");

		int permission1 = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int permission2 = ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN);
		if (permission1 != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 1);
		} else if (permission2 != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(activity, PERMISSIONS_LOCATION, 1);
		}
	}

	private List<ScanDeviceBean> scanDevicesList = new ArrayList<>();
	private List<String> macAddressList = new ArrayList<>();
	DeviceAdapter deviceAdapter = new DeviceAdapter(scanDevicesList);

	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(@NonNull Message msg) {
			if (msg.what == 0) {
				handler.sendEmptyMessageDelayed(0, 1000);
				YCBTClient.getAllRealDataFromDevice(new BleDataResponse() {
					@Override
					public void onDataResponse(int i, float v, HashMap hashMap) {
						Log.e(TAG, hashMap.toString());
					}
				});
			} else if (msg.what == 1) {
				Log.e(TAG, "1");
			} else if (msg.what == 2) {
				Log.e(TAG, "2");
			} else if (msg.what == 3) {
				Log.e(TAG, "3");
			} else if (msg.what == 4) {
				Log.e(TAG, "4");
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
				Log.e(TAG, "initPlugin...");
				checkPermissions();

				YCBTClient.initClient(context, true);
				YCBTClient.registerBleStateChange(bleConnectResponse);
				YCBTClient.deviceToApp(toAppDataResponse);

				break;
			}
			case "startScan": {
				Log.e(TAG, "startScan...");
				macAddressList = new ArrayList<>();
				deviceAdapter.setScanDevicesList(new ArrayList<>());
				YCBTClient.startScanBle(new BleScanResponse() {
					@Override
					public void onScanResponse(int i, ScanDeviceBean scanDeviceBean) {

						if (scanDeviceBean != null) {
							if (!macAddressList.contains(scanDeviceBean.getDeviceMac())) {
								macAddressList.add(scanDeviceBean.getDeviceMac());
								deviceAdapter.addModel(scanDeviceBean);
							}

							Log.e(TAG, "mac = " + scanDeviceBean.getDeviceMac() + "; name = " + scanDeviceBean.getDeviceName() + "; rssi = " + scanDeviceBean.getDeviceRssi());
						}
					}
				}, 15);
				Log.e(TAG, "finishStartScan...");
				result.success(deviceAdapter.getScanDevicesList());
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
