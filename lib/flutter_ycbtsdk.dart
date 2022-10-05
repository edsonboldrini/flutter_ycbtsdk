library flutter_ycbtsdk;

import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
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
      // log(call.toString());
      methodStreamController.add(call);
      switch (call.method) {
        case 'onScanResult':
          await onScanResult(call.arguments);
          break;
        case 'onConnectResponse':
          await onConnectResponse(call.arguments);
          break;
        case 'onDataResponse':
          await onDataResponse(call.arguments);
          break;
        default:
          throw UnimplementedError('${call.method} has not been implemented.');
      }
    });

    // onStreamBatteryStatus();
  }

  late StreamSubscription _streamSubscription;

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

  Stream<List<ScanResult>> get scanResultsStream => _scanResults.stream;

  final BehaviorSubject<Map<String, dynamic>> _data = BehaviorSubject();

  Stream<Map<String, dynamic>> get dataStream => _data.stream;

  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  Future<void> checkPermissions() async {
    await methodChannel.invokeMethod<String>('checkPermissions');
  }

  Future<void> initPlugin() async {
    await methodChannel.invokeMethod<String>('initPlugin');
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
    await methodChannel.invokeMethod<String>('stopScan');
  }

  Future connectDevice(String deviceMacAddress) async {
    return await methodChannel.invokeMethod<String>(
        'connectDevice', deviceMacAddress);
  }

  Future<void> disconnectDevice() async {
    await methodChannel.invokeMethod<String>('disconnectDevice');
  }

  Future<void> startEcgTest() async {
    await methodChannel.invokeMethod<String>('startEcgTest');
  }

  Future<void> stopEcgTest() async {
    await methodChannel.invokeMethod<String>('stopEcgTest');
  }

  Future<void> startMeasurement(int onOff, int type) async {
    await methodChannel.invokeMethod<String>(
        'startMeasurement', {"onOff": onOff, "type": type});
  }

  Future<void> healthHistoryData() async {
    await methodChannel.invokeMethod<String>('healthHistoryData');
  }

  onScanResult(payload) async {
    try {
      log(payload.toString());
      final result = ScanResult.fromJson(payload);
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

  onConnectResponse(payload) {
    try {
      log(payload.toString());
    } catch (e) {
      log(e.toString());
    }
  }

  onDataResponse(payload) {
    try {
      log(payload.toString());
      Map<String, dynamic> map = json.decode(payload);
      _data.add(map);
      return map;
    } catch (e) {
      log(e.toString());
    }
  }
}

// enum RealDataType {
//   heartRate,
//   bloodPressure,
//   bloodOxygen,
//   steps,
//   temperature,
// }

// class RealData {
//   final String type;
//   final String value;

//   RealData({
//     required this.type,
//     required this.value,
//   });

//   RealData copyWith({
//     String? type,
//     String? value,
//   }) {
//     return RealData(
//       type: type ?? this.type,
//       value: value ?? this.value,
//     );
//   }

//   Map<String, dynamic> toMap() {
//     return {
//       'type': type,
//       'value': value,
//     };
//   }

//   factory RealData.fromMap(Map<String, dynamic> map) {
//     return RealData(
//       type: map['type'],
//       value: map['value'],
//     );
//   }
//   String toJson() => json.encode(toMap());
//   factory RealData.fromJson(String source) =>
//       RealData.fromMap(json.decode(source));
//   @override
//   String toString() => 'RealData(type: $type, value: $value)';
//   @override
//   bool operator ==(Object other) {
//     if (identical(this, other)) return true;

//     return other is RealData && other.type == type && other.value == value;
//   }

//   @override
//   int get hashCode => type.hashCode ^ value.hashCode;
// }

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
