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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
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
	private EventChannel stateChannel;
	private Context context;
	private Activity activity;
	private String deviceMacAddress;

	private final Object initializationLock = new Object();
	private final Object tearDownLock = new Object();
	private FlutterPluginBinding pluginBinding;
	private ActivityPluginBinding activityBinding;

	private ObjectMapper mapper = new ObjectMapper();

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
		setup(pluginBinding.getBinaryMessenger(), (Application) pluginBinding.getApplicationContext());
	}

	private void setup(final BinaryMessenger messenger, final Application application) {
		synchronized (initializationLock) {
			Log.d(TAG, "setup");
			context = application;
			methodChannel = new MethodChannel(messenger, NAMESPACE + "/methods");
			methodChannel.setMethodCallHandler(this);
			stateChannel = new EventChannel(messenger, NAMESPACE + "/state");
			stateChannel.setStreamHandler((StreamHandler) stateHandler);

			YCBTClient.initClient(context, true);
			YCBTClient.registerBleStateChange(bleConnectResponse);
			YCBTClient.deviceToApp(toAppDataResponse);

			EventBus.getDefault().register(this);
			// startService(new Intent(this, MyBleService.class));
		}
	}

	private void tearDown() {
		synchronized (tearDownLock) {
			Log.d(TAG, "teardown");
			context = null;
			methodChannel.setMethodCallHandler(null);
			methodChannel = null;
			stateChannel.setStreamHandler(null);
			stateChannel = null;
			EventBus.getDefault().unregister(this);
		}
	}

	private void invokeMethodUIThread(final String name, final Object arguments) {
		new Handler(Looper.getMainLooper()).post(() -> {
			synchronized (tearDownLock) {
				//Could already be teared down at this moment
				if (methodChannel != null) {
					methodChannel.invokeMethod(name, arguments);
				} else {
					Log.w(TAG, "Tried to call " + name + " on closed channel");
				}
			}
		});
	}

	private final StreamHandler stateHandler = new EventChannel.StreamHandler() {
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
			Log.e(TAG, "Passive return data = " + dataMap.toString());
		}
	};

	boolean isActiveDisconnect = false;
	BleConnectResponse bleConnectResponse = new BleConnectResponse() {
		@Override
		public void onConnectResponse(int code) {
			Log.e(TAG, "Global monitor return = " + code);

			if (code == com.yucheng.ycbtsdk.Constants.BLEState.Disconnect) {
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
				Log.e(TAG, "Connected = " + code);
			} else if (code == com.yucheng.ycbtsdk.Constants.BLEState.ReadWriteOK) {
				Log.e(TAG, "Bluetooth connection is successful");
				EventBus.getDefault().post(new ConnectEvent());
			} else {
				Log.e(TAG, "Bluetooth connection disconnected");
				EventBus.getDefault().post(new ConnectEvent());
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
			case "getPlatformVersion": {
				result.success("Android " + android.os.Build.VERSION.RELEASE);
				break;
			}
			case "checkPermissions": {
				checkPermissions();
				result.success(null);
				break;
			}
			case "initPlugin": {
				Log.e(TAG, "initPlugin...");
				/*
				YCBTClient.initClient(context, true);
				YCBTClient.registerBleStateChange(bleConnectResponse);
				YCBTClient.deviceToApp(toAppDataResponse);
				*/
				result.success(null);
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
							HashMap scanData = new HashMap<>();
							scanData.put("mac", scanDeviceBean.getDeviceMac());
							scanData.put("name", scanDeviceBean.getDeviceName());
							scanData.put("rssi", scanDeviceBean.getDeviceRssi());

							try {
								String scanDataString
												= mapper.writeValueAsString(scanData);
								invokeMethodUIThread("onScanResult", scanDataString);
							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}
						}
					}
				}, 15);
				result.success(null);
				break;
			}
			case "stopScan": {
				Log.e(TAG, "stopScan...");
				YCBTClient.stopScanBle();
				result.success(null);
				break;
			}
			case "connectDevice": {
				Log.e(TAG, "connectDevice...");
				Log.e(TAG, call.arguments.toString());
				YCBTClient.stopScanBle();

				deviceMacAddress = call.arguments.toString();

				YCBTClient.connectBle(deviceMacAddress, new BleConnectResponse() {
					@Override
					public void onConnectResponse(final int i) {
						Log.e(TAG, "onConnectResponse... " + i);
					}
				});
			}
			result.success(null);
			break;
			case "disconnectDevice": {
				Log.e(TAG, "disconnectDevice...");
//				PendingIntent pendingIntent = null;
//				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
//					pendingIntent = PendingIntent.getActivity
//									(context, 0, null, PendingIntent.FLAG_MUTABLE);
//				} else {
//					pendingIntent = PendingIntent.getActivity
//									(context, 0, null, PendingIntent.FLAG_ONE_SHOT);
//				}
				YCBTClient.disconnectBle();
				result.success(null);
				break;
			}
			case "startEcgTest": {
				Log.e(TAG, "startEcgTest...");
				//0x0200080047436FEC
				/*
				AITools.getInstance().init();
				AITools.getInstance().setAIDiagnosisHRVNormResponse(new BleAIDiagnosisHRVNormResponse() {
					@Override
					public void onAIDiagnosisResponse(HRVNormBean hrvNormBean) {

						float heavy_load = bean.heavy_load; // Load index (the bigger the better the outgoing)
						float pressure = bean.pressure; // Pressure index (the bigger the better the outgoing)
						float HRV_norm = bean.HRV_norm; // HRV index (the bigger the better the outgoing)
						float body = bean.body; // body index (the bigger the better the outgoing)
            int flag = -1; // 0 normal -1 error

						System.out.println("AIDiagnosis");
					}
				});
				*/

				YCBTClient.appEcgTestStart(new BleDataResponse() {
					@Override
					public void onDataResponse(int i, float v, HashMap hashMap) {
						try {
							String responseString
											= mapper.writeValueAsString(hashMap);
							Log.e("qob", "onRealDataResponse " + i + " " + v + " " + " dataType " + responseString);
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
						/*
						if (hashMap != null) {
							int dataType = (int) hashMap.get("dataType");
							Log.e("qob", "onRealDataResponse " + i + " " + v + " " + " dataType " + dataType);
						}
						*/
					}
				}, new BleRealDataResponse() {
					@Override
					public void onRealDataResponse(int i, HashMap hashMap) {
						if (hashMap != null) {
							int dataType = (int) hashMap.get("dataType");
							Log.e("qob", "onRealDataResponse " + i + " dataType " + dataType);
							if (i == Constants.DATATYPE.Real_UploadECG) {
								final List<Integer> tData = (List<Integer>) hashMap.get("data");
								System.out.println("ecgData==" + tData.toString());
								// Must be analyzed on the main thread
								Log.e("qob", "AI " + tData.size());
							} else if (i == Constants.DATATYPE.Real_UploadPPG) {
								byte[] param = (byte[]) hashMap.get("data");
								Log.e("qob", "ppg: " + Arrays.toString(param));
							} else if (i == Constants.DATATYPE.Real_UploadECGHrv) {
								float param = (float) hashMap.get("data");
								Log.e("qob", "HRV: " + param);
							} else if (i == Constants.DATATYPE.Real_UploadECGRR) {
								float param = (float) hashMap.get("data");
								Log.e("qob", "RR invo " + param);
							} else if (i == Constants.DATATYPE.Real_UploadBlood) {
								int heart = (int) hashMap.get("heartValue"); // heart rate
								int tDBP = (int) hashMap.get("bloodDBP"); // low pressure
								int tSBP = (int) hashMap.get("bloodSBP"); // high pressure
							}
						}
					}
				});
				result.success(null);
				break;
			}
			case "stopEcgTest": {
				Log.e(TAG, "stopEcgTest...");
				YCBTClient.appEcgTestEnd(new BleDataResponse() {
					@Override
					public void onDataResponse(int i, float v, HashMap hashMap) {
						try {
							String responseString
											= mapper.writeValueAsString(hashMap);
							Log.e("qob", "onRealDataResponse " + i + " " + v + " " + " dataType " + responseString);
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}

						if (i != 0) {
							// test exception
							return;
						}

						/*
						HRVNormBean bean = AITools.getInstance().getHrvNorm();
						if (bean != null) {
							if (bean.flag == -1) {
								//错误
							} else {//正常
								float heavy_load = bean.heavy_load; // Load index (the bigger the better the outgoing)
								float pressure = bean.pressure; // Pressure index (the bigger the better the outgoing)
								float HRV_norm = bean.HRV_norm; // HRV index (the bigger the better the outgoing)
								float body = bean.body; // body index (the bigger the better the outgoing)
							}
						}

						AITools.getInstance().getAIDiagnosisResult(new BleAIDiagnosisResponse() {
							@Override
							public void onAIDiagnosisResponse(AIDataBean aiDataBean) {
								if (aiDataBean != null) {
									short heart = aiDataBean.heart; // heart rate
									int qrstype = aiDataBean.qrstype; // Type 1 normal heart beat 5 atrial premature beat 9 atrial premature beat 14 noise
									boolean is_atrial_fibrillation = aiDataBean.is_atrial_fibrillation; // atrial fibrillation
									System.out.println("heart = " + heart + " qrstype = " + qrstype + " is_atrial_fibrillation = " + is_atrial_fibrillation);
								}
							}
						});
						*/
					}
				});
				result.success(null);
				break;
			}
			case "startMeasurement": {
				Log.e(TAG, "startMeasurement...");
				YCBTClient.appStartMeasurement(1, 0, new BleDataResponse() {
					@Override
					public void onDataResponse(int i, float v, HashMap hashMap) {
						try {
							String responseString
											= mapper.writeValueAsString(hashMap);
							Log.e("qob", "onRealDataResponse " + i + " " + v + " " + " dataType " + responseString);
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}

						if (i == 0) {
							//success
						}
					}
				});
				result.success(null);
			}
			default: {
				result.notImplemented();
				break;
			}
		}
	}
}
