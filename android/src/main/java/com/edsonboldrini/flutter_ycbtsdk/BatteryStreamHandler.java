package com.edsonboldrini.flutter_ycbtsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.EventChannel.StreamHandler;
import io.flutter.plugin.common.EventChannel.EventSink;

public class BatteryStreamHandler implements StreamHandler {
	private final Context context;
	private BroadcastReceiver receiver;

	public BatteryStreamHandler(Context context) {
		this.context = context;
	}

	@Override
	public void onListen(Object arguments, EventSink events) {
		receiver = initReceiver(events);
		context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}

	@Override
	public void onCancel(Object arguments) {
		context.unregisterReceiver(receiver);
		receiver = null;
	}

	private BroadcastReceiver initReceiver(final EventSink events) {
		return new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

				switch (status) {
					case BatteryManager.BATTERY_STATUS_CHARGING:
						events.success("charging");
						break;
					case BatteryManager.BATTERY_STATUS_FULL:
						events.success("full");
						break;
					case BatteryManager.BATTERY_STATUS_DISCHARGING:
						events.success("discharging");
						break;
				}
			}
		};
	}
}
