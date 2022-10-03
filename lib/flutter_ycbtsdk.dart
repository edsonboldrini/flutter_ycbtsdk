library flutter_ycbtsdk;

import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:rxdart/rxdart.dart';

class FlutterYcbtsdk {
  static const String namespace = 'flutter_ycbtsdk';

  /// The method channel used to interact with the native platform methods.
  @visibleForTesting
  MethodChannel methodChannel = const MethodChannel('$namespace/methods');

  /// The event channel used to interact with the native platform state.
  @visibleForTesting
  EventChannel eventChannel = const EventChannel('$namespace/events');

  StreamController<MethodCall> methodStreamController =
      StreamController.broadcast(); // ignore: close_sinks
  Stream<MethodCall> get methodStream => methodStreamController
      .stream; // Used internally to dispatch methods from platform.

  /// Singleton boilerplate
  FlutterYcbtsdk._() {
    methodChannel.setMethodCallHandler((MethodCall call) async {
      log(call.toString());
      methodStreamController.add(call);
      switch (call.method) {
        case 'onScanResult':
          await onScanResult(call.arguments);
          break;
        default:
          throw UnimplementedError('${call.method} has not been implemented.');
      }
    });

    // onStreamBatteryStatus();
  }

  String batteryStatus = 'Streaming';
  onStreamBatteryStatus() {
    _streamSubscription = eventChannel.receiveBroadcastStream().listen((event) {
      print('$event');
      batteryStatus = event;
    });
  }

  static final FlutterYcbtsdk _instance = FlutterYcbtsdk._();
  static FlutterYcbtsdk get instance => _instance;

  final BehaviorSubject<List<ScanResult>> _scanResults =
      BehaviorSubject.seeded([]);

  /// Returns a stream that is a list of [ScanResult] results while a scan is in progress.
  ///
  /// The list emitted is all the scanned results as of the last initiated scan. When a scan is
  /// first started, an empty list is emitted. The returned stream is never closed.
  ///
  /// One use for [scanResults] is as the stream in a StreamBuilder to display the
  /// results of a scan in real time while the scan is in progress.
  Stream<List<ScanResult>> get scanResults => _scanResults.stream;

  late StreamSubscription _streamSubscription;

  Future<String?> getPlatformVersion() async {
    final version = await FlutterYcbtsdk.instance.methodChannel
        .invokeMethod<String>('getPlatformVersion');
    return version;
  }

  Future<void> checkPermissions() async {
    await FlutterYcbtsdk.instance.methodChannel
        .invokeMethod<String>('checkPermissions');
  }

  Future<void> initPlugin() async {
    await FlutterYcbtsdk.instance.methodChannel
        .invokeMethod<String>('initPlugin');
  }

  Future startScan(int timeout) async {
    await startScanStream(timeout).drain();
    return _scanResults.value;
  }

  Stream<ScanResult> startScanStream(int timeout) async* {
    // Clear scan results list
    _scanResults.add(<ScanResult>[]);
    await methodChannel.invokeMethod<String>('startScan', timeout);

    // yield* methodStream
    //     .where((m) => m.method == 'ScanResult')
    //     .map((m) => m.arguments)
    //     .map((arguments) {
    //   final result = ScanResult.fromMap(arguments);
    //   final list = _scanResults.value;
    //   int index = list.indexOf(result);
    //   if (index != -1) {
    //     list[index] = result;
    //   } else {
    //     list.add(result);
    //   }
    //   _scanResults.add(list);
    //   return result;
    // });
  }

  Future<void> stopScan() async {
    await FlutterYcbtsdk.instance.methodChannel
        .invokeMethod<String>('stopScan');
  }

  Future<void> connectDevice(String deviceMacAddress) async {
    await FlutterYcbtsdk.instance.methodChannel
        .invokeMethod<String>('connectDevice', deviceMacAddress);
  }

  Future<void> disconnectDevice() async {
    await FlutterYcbtsdk.instance.methodChannel
        .invokeMethod<String>('disconnectDevice');
  }

  Future<void> startEcgTest() async {
    await FlutterYcbtsdk.instance.methodChannel
        .invokeMethod<String>('startEcgTest');
  }

  Future<void> stopEcgTest() async {
    await FlutterYcbtsdk.instance.methodChannel
        .invokeMethod<String>('stopEcgTest');
  }

  Future<void> startMeasurement(int onOff, int type) async {
    await FlutterYcbtsdk.instance.methodChannel.invokeMethod<String>(
        'startMeasurement', {"onOff": onOff, "type": type});
  }

  onScanResult(arguments) async {
    try {
      log(arguments.toString());
      final result = ScanResult.fromJson(arguments);
      final list = _scanResults.value;
      int index = list.indexWhere((s) => s.mac == result.mac);
      if (index != -1) {
        list[index] = result;
      } else {
        list.add(result);
      }
      _scanResults.add(list);
      return result;
    } catch (e) {
      log(e.toString());
    }
  }
}

class ScanResult {
  final String mac;
  final String name;
  final int rssi;

  ScanResult({
    required this.mac,
    required this.name,
    required this.rssi,
  });

  ScanResult copyWith({
    String? mac,
    String? name,
    int? rssi,
  }) {
    return ScanResult(
      mac: mac ?? this.mac,
      name: name ?? this.name,
      rssi: rssi ?? this.rssi,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'mac': mac,
      'name': name,
      'rssi': rssi,
    };
  }

  factory ScanResult.fromMap(Map<String, dynamic> map) {
    return ScanResult(
      mac: map['mac'],
      name: map['name'],
      rssi: map['rssi'],
    );
  }
  String toJson() => json.encode(toMap());
  factory ScanResult.fromJson(String source) =>
      ScanResult.fromMap(json.decode(source));

  @override
  String toString() => 'ScanResult(mac: $mac, name: $name, rssi: $rssi)';

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    return other is ScanResult &&
        other.mac == mac &&
        other.name == name &&
        other.rssi == rssi;
  }

  @override
  int get hashCode => mac.hashCode ^ name.hashCode ^ rssi.hashCode;
}
