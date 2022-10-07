package com.edsonboldrini.flutter_ycbtsdk;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
// import com.yucheng.ycbtsdk.AITools;
// import com.yucheng.ycbtsdk.Constants;
import com.yucheng.ycbtsdk.Constants;
import com.yucheng.ycbtsdk.YCBTClient;
// import com.yucheng.ycbtsdk.bean.AIDataBean;
// import com.yucheng.ycbtsdk.bean.HRVNormBean;
import com.yucheng.ycbtsdk.bean.ScanDeviceBean;
// import com.yucheng.ycbtsdk.response.BleAIDiagnosisHRVNormResponse;
// import com.yucheng.ycbtsdk.response.BleAIDiagnosisResponse;
import com.yucheng.ycbtsdk.response.BleConnectResponse;
import com.yucheng.ycbtsdk.response.BleDataResponse;
import com.yucheng.ycbtsdk.response.BleDeviceToAppDataResponse;
import com.yucheng.ycbtsdk.response.BleRealDataResponse;
import com.yucheng.ycbtsdk.response.BleScanResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
// import java.util.Arrays;
import java.util.Date;
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
	private MethodChannel methodChannel;
	private EventChannel eventChannel;
	private Context context;
	private Activity activity;
	private String deviceMacAddress;

	private final Object initializationLock = new Object();
	private final Object tearDownLock = new Object();
	private FlutterPluginBinding pluginBinding;
	private ActivityPluginBinding activityBinding;

	private ObjectMapper mapper = new ObjectMapper();

	private List<ScanDeviceBean> scanDevicesList = new ArrayList<>();
	private List<String> macAddressList = new ArrayList<>();
	DeviceAdapter deviceAdapter = new DeviceAdapter(scanDevicesList);

	@Override
	public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
		Log.d(TAG, "onDetachedFromEngine");
		tearDown();
	}

	@Override
	public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
		Log.d(TAG, "onAttachedToActivity");
		activityBinding = binding;
		activity = binding.getActivity();
	}

	@Override
	public void onDetachedFromActivityForConfigChanges() {
		Log.d(TAG, "onDetachedFromActivityForConfigChanges");
	}

	@Override
	public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
		Log.d(TAG, "onReattachedToActivityForConfigChanges");
	}

	@Override
	public void onDetachedFromActivity() {
		Log.d(TAG, "onDetachedFromActivity");
	}

	@Override
	public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
		Log.d(TAG, "onAttachedToEngine");
		pluginBinding = flutterPluginBinding;
//		PendingIntent pendingIntent;
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//			pendingIntent = PendingIntent.getActivity(pluginBinding.getApplicationContext(),
//							0, new Intent(pluginBinding.getApplicationContext(), getClass()).addFlags(
//											Intent.FLAG_ACTIVITY_SINGLE_TOP),
//							PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//		} else {
//			pendingIntent = PendingIntent.getActivity(pluginBinding.getApplicationContext(),
//							0, new Intent(pluginBinding.getApplicationContext(), getClass()).addFlags(
//											Intent.FLAG_ACTIVITY_SINGLE_TOP),
//							PendingIntent.FLAG_ONE_SHOT);
//		}
		setup(pluginBinding.getBinaryMessenger(), (Application) pluginBinding.getApplicationContext());
	}

	@Override
	public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
		switch (call.method) {
			case "getPlatformVersion": {
				result.success("Android " + android.os.Build.VERSION.RELEASE);
				break;
			}
			case "checkPermissions": {
				checkPermissions();
				result.success(null);
				break;
			}
			/*
			case "initPlugin": {
				Log.e(TAG, "initPlugin...");

				YCBTClient.initClient(context, true);
				YCBTClient.registerBleStateChange(bleConnectResponse);
				YCBTClient.deviceToApp(toAppDataResponse);

				result.success(null);
				break;
			}
			*/
			case "startScan": {
				Log.e(TAG, "startScan...");
				macAddressList = new ArrayList<>();
				deviceAdapter.setScanDevicesList(new ArrayList<>());
				int timeoutInSeconds = (int) call.arguments;
				YCBTClient.startScanBle(bleScanResponse, timeoutInSeconds);
				result.success(null);
				break;
			}
			case "stopScan": {
				Log.e(TAG, "stopScan...");
				try {
					YCBTClient.stopScanBle();
					result.success(null);
				} catch (Exception e) {
					e.printStackTrace();
					result.error("stopScan error", e.getMessage(), e);
				}
				break;
			}
			case "connectDevice": {
				Log.e(TAG, "connectDevice...");
				Log.e(TAG, call.arguments.toString());
				deviceMacAddress = call.arguments.toString();

				YCBTClient.connectBle(deviceMacAddress, new BleConnectResponse() {
					@Override
					public void onConnectResponse(final int i) {
						// Log.e(TAG, "onConnectResponse... " + i);
						if (i == 0) {
							HashMap map = new HashMap<String, String>() {{
								put(deviceMacAddress, "connected");
							}};

							String mapString = hashMapToStringJson(map);
							result.success(mapString);
						} else {
							HashMap map = new HashMap<String, String>() {{
								put(deviceMacAddress, "error");
							}};

							String mapString = hashMapToStringJson(map);
							result.error("unableConnectDevice error", "error", null);
						}
					}
				});
			}
			break;
			case "disconnectDevice": {
				Log.e(TAG, "disconnectDevice...");
				try {
					YCBTClient.disconnectBle();
					HashMap map = null;
					if (deviceMacAddress != null) {
						map = new HashMap<String, String>() {{
							put(deviceMacAddress, "disconnected");
						}};
					} else {
						map = new HashMap<String, String>() {{
							put("all", "disconnected");
						}};
					}

					String mapString = hashMapToStringJson(map);
					deviceMacAddress = null;
					result.success(mapString);
				} catch (Exception e) {
					e.printStackTrace();
					result.error("disconnectDevice error", e.getMessage(), e);
				}
				break;
			}
			case "startEcgTest": {
				Log.e(TAG, "startEcgTest...");
				YCBTClient.appEcgTestStart(bleDataResponse, bleRealDataResponse);
				result.success(null);
				break;
			}
			case "stopEcgTest": {
				Log.e(TAG, "stopEcgTest...");
				YCBTClient.appEcgTestEnd(bleDataResponse);
				result.success(null);
				break;
			}
			case "healthHistoryData": {
				YCBTClient.healthHistoryData(Constants.DATATYPE.Health_HistoryAll, bleDataResponse);
				result.success(null);
				break;
			}
			case "deleteHealthHistoryData": {
				YCBTClient.deleteHealthHistoryData(Constants.DATATYPE.Health_DeleteAll, bleDataResponse);
				result.success(null);
				break;
			}
			case "test": {
				Log.e(TAG, "test...");
				result.success(null);
				break;
			}
			default: {
				result.notImplemented();
				break;
			}
		}
	}

	BleScanResponse bleScanResponse = new BleScanResponse() {
		@Override
		public void onScanResponse(int i, ScanDeviceBean scanDeviceBean) {
			if (scanDeviceBean != null) {
				if (!macAddressList.contains(scanDeviceBean.getDeviceMac())) {
					macAddressList.add(scanDeviceBean.getDeviceMac());
					deviceAdapter.addModel(scanDeviceBean);
				}

				Log.e(TAG, "mac = " + scanDeviceBean.getDeviceMac() + "; name = " + scanDeviceBean.getDeviceName() + "; rssi = " + scanDeviceBean.getDeviceRssi());
				HashMap map = new HashMap<String, Object>() {{
					put("mac", scanDeviceBean.getDeviceMac());
					put("name", scanDeviceBean.getDeviceName());
					put("rssi", scanDeviceBean.getDeviceRssi());
				}};

				String mapString = hashMapToStringJson(map);
				invokeMethodUIThread("onScanResult", mapString);
			}
		}
	};

	BleConnectResponse bleConnectResponse = new BleConnectResponse() {
		@Override
		public void onConnectResponse(int code) {
			String status = "unknown";

			if (code <= Constants.BLEState.Disconnecting) {
				status = "disconnected";
			} else if (code == Constants.BLEState.Disconnecting) {
				status = "disconnecting";
			} else if (code == Constants.BLEState.Connecting) {
				status = "connecting";
			} else if (code >= com.yucheng.ycbtsdk.Constants.BLEState.Connected) {
				Log.e(TAG, deviceMacAddress + " - Connected! " + code);
				status = "connected";
			} else {
				status = "unknown";
			}

			String finalStatus = status;
			Log.e(TAG, "onConnectResponse - " + deviceMacAddress + " " + finalStatus + " " + code);

			HashMap map = null;
			if (deviceMacAddress != null) {
				map = new HashMap<String, String>() {{
					put(deviceMacAddress, finalStatus);
				}};
			} else {
				map = new HashMap<String, String>() {{
					put("all", finalStatus);
				}};
			}

			String mapString = hashMapToStringJson(map);
			invokeMethodUIThread("onConnectResponse", mapString);
		}
	};

	BleDataResponse bleDataResponse = new BleDataResponse() {
		@Override
		public void onDataResponse(int code, float value, HashMap hashMap) {
			Log.e("qob", "onDataResponse - code: " + code + " value: " + value + " data: " + hashMap);

			if (hashMap != null) {
				if (hashMap.containsKey("data")) {
					Object data = hashMap.get("data");
					if (data instanceof ArrayList<?>) {
						ArrayList<HashMap> list = (ArrayList<HashMap>) hashMap.get("data");
						for (HashMap map : list) {
							String mapString = hashMapToStringJson(map);
							invokeMethodUIThread("onDataResponse", mapString);
						}
					} else {
						String mapString = hashMapToStringJson(hashMap);
						invokeMethodUIThread("onDataResponse", mapString);
					}
				} else {
					String mapString = hashMapToStringJson(hashMap);
					invokeMethodUIThread("onDataResponse", mapString);
				}

			}
		}
	};

	BleRealDataResponse bleRealDataResponse = new BleRealDataResponse() {
		@Override
		public void onRealDataResponse(int dataType, HashMap map) {
			Log.e("qob", "onRealDataResponse dataType: " + dataType + " data: " + map);
			String mapString = hashMapToStringJson(map);
			invokeMethodUIThread("onDataResponse", mapString);
		}
	};

	private String hashMapToStringJson(HashMap data) {
		String resultString = null;
		try {
			resultString = mapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return resultString;
	}

	private void setup(final BinaryMessenger messenger, final Application application) {
		synchronized (initializationLock) {
			Log.d(TAG, "setup...");
			context = application;
			methodChannel = new MethodChannel(messenger, NAMESPACE + "/methods");
			methodChannel.setMethodCallHandler(this);
			eventChannel = new EventChannel(messenger, NAMESPACE + "/events");
			eventChannel.setStreamHandler(new BatteryStreamHandler(context));

			YCBTClient.initClient(context, true);
			YCBTClient.registerBleStateChange(bleConnectResponse);
			YCBTClient.deviceToApp(toAppDataResponse);

			EventBus.getDefault().register(this);
			// startService(new Intent(this, MyBleService.class));
		}
	}

//  SDK >= 31
//	private static String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_PRIVILEGED};
//
//	private void checkPermissions() {
//		Log.e(TAG, "checking permissions...");
//
//		int permission1 = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//		int permission2 = ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN);
//		if (permission1 != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED) {
//			// We don't have permission so prompt the user
//			ActivityCompat.requestPermissions(activity, PERMISSIONS, 1);
//		}
//	}

	private static String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS, Manifest.permission.BLUETOOTH_PRIVILEGED};

	private void checkPermissions() {
		Log.e(TAG, "checking permissions...");

		int permission1 = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int permission2 = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
		if (permission1 != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(activity, PERMISSIONS, 1);
		}
	}

	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(@NonNull Message msg) {
			if (msg.what == 0) {
				handler.sendEmptyMessageDelayed(0, 1000);
				YCBTClient.getAllRealDataFromDevice(bleDataResponse);
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

	private void tearDown() {
		synchronized (tearDownLock) {
			Log.d(TAG, "teardown");
			context = null;
			methodChannel.setMethodCallHandler(null);
			methodChannel = null;
			eventChannel.setStreamHandler(null);
			eventChannel = null;
			YCBTClient.unRegisterBleStateChange(bleConnectResponse);
			EventBus.getDefault().unregister(this);
		}
	}

	private void invokeMethodUIThread(final String name, final Object arguments) {
		new Handler(Looper.getMainLooper()).post(() -> {
			synchronized (tearDownLock) {
				// Could already be teared down at this moment
				if (methodChannel != null) {
					methodChannel.invokeMethod(name, arguments);
				} else {
					Log.w(TAG, "Tried to call " + name + " on closed channel");
				}
			}
		});
	}

	private final StreamHandler stateHandler = new StreamHandler() {
		private EventSink sink;

		private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				final String action = intent.getAction();
			}
		};

		@Override
		public void onListen(Object o, EventSink eventSink) {
			sink = eventSink;
			IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
			context.registerReceiver(broadcastReceiver, filter);
		}

		@Override
		public void onCancel(Object o) {
			sink = null;
			context.unregisterReceiver(broadcastReceiver);
		}
	};

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void connectEvent(ConnectEvent connectEvent) {
		Log.e(TAG, "Connected");
	}

	BleDeviceToAppDataResponse toAppDataResponse = new BleDeviceToAppDataResponse() {
		@Override
		public void onDataResponse(int dataType, HashMap dataMap) {
			Log.e(TAG, "Passive return data = " + dataMap);
		}
	};
}
