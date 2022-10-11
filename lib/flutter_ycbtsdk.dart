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

    startSubscriptions();
  }

  StreamSubscription? _streamSubscription;

  startSubscriptions() {
    _streamSubscription = eventChannel.receiveBroadcastStream().listen((event) {
      print('stream subscription event: $event');
    });
  }

  dispose() {
    _streamSubscription?.cancel();
  }

  static final FlutterYcbtsdk _instance = FlutterYcbtsdk._();
  static FlutterYcbtsdk get instance => _instance;

  final BehaviorSubject<List<ScanResult>> _scanResults =
      BehaviorSubject.seeded([]);

  Stream<List<ScanResult>> get scanResultsStream => _scanResults.stream;

  final BehaviorSubject<WristbandData> _data = BehaviorSubject();

  Stream<WristbandData> get dataStream => _data.stream;

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

  Future startScan(int timeoutInSeconds) async {
    await startScanStream(timeoutInSeconds).drain();
    return _scanResults.value;
  }

  Stream<ScanResult> startScanStream(int timeoutInSeconds) async* {
    // Clear scan results list
    _scanResults.add(<ScanResult>[]);
    await methodChannel.invokeMethod<String>('startScan', timeoutInSeconds);

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

  Future disconnectDevice() async {
    return await methodChannel.invokeMethod<String>('disconnectDevice');
  }

  Future connectState() async {
    return await methodChannel.invokeMethod<String>('connectState');
  }

  Future<void> resetQueue() async {
    await methodChannel.invokeMethod<String>('resetQueue');
  }

  Future<void> startEcgTest() async {
    await methodChannel.invokeMethod<String>('startEcgTest');
  }

  Future<void> stopEcgTest() async {
    await methodChannel.invokeMethod<String>('stopEcgTest');
  }

  Future<void> healthHistoryData() async {
    await methodChannel.invokeMethod<String>('healthHistoryData');
  }

  Future<void> deleteHealthHistoryData() async {
    await methodChannel.invokeMethod<String>('deleteHealthHistoryData');
  }

  Future<void> test() async {
    await methodChannel.invokeMethod<String>('test');
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

  // Dados virão em lote porém um de cada vez, posso ajustar o plugin depois caso queiramos receber
  // receber uma lista. Fiz assim para poder reutilizar a subscription de dados.
  // Cada objeto terá o formato:
  // {"heartValue":0,"hrvValue":21,"cvrrValue":5,"stepValue":0,"DBPValue":73,"bodyFatFloatValue":0,"OOValue":98,"bodyFatIntValue":0,"tempIntValue":36,"tempFloatValue":4,"startTime":1664680207000,"SBPValue":109,"respiratoryRateValue":17}

  // DBPValue ⬆️ e bloodDBP ⬇️ são a mesma coisa, assim como SBPValue e bloodSBP

  // Dados virão um de cada vez a cada 1 segundo um novo dado, teste em tempo real.
  // Cada objeto terá o formato:
  // {"bloodDBP":77,"heartValue":94,"code":0,"dataType":1539,"bloodSBP":118}

  // Sport data:
  // {"sportEndTime"=1665151200000, "sportStep"=26, "sportDistance"=18, "sportStartTime"=1665149400000, "sportCalorie"=1}

  onDataResponse(payload) {
    try {
      log(payload.toString());
      Map<String, dynamic> map = json.decode(payload);
      final mapKeys = map.keys;
      final dataAlreadyParsed = [];

      for (String key in mapKeys) {
        var dateTime = DateTime.now().toUtc();
        if (mapKeys.contains('startTime')) {
          dateTime =
              DateTime.fromMillisecondsSinceEpoch(map['startTime']).toUtc();
        }
        if (mapKeys.contains('sportEndTime')) {
          dateTime =
              DateTime.fromMillisecondsSinceEpoch(map['sportEndTime']).toUtc();
        }

        switch (key) {
          case 'heartValue':
            const dataType = WristbandDataType.heartRate;
            if (dataAlreadyParsed.contains(dataType)) break;
            dataAlreadyParsed.add(dataType);

            var data = WristbandData(
              dateTime: dateTime,
              dataType: dataType,
              rawValue: map[key],
              formattedValue: "${map[key]} bpm",
            );
            _data.add(data);
            break;
          case 'OOValue':
            const dataType = WristbandDataType.bloodOxygen;
            if (dataAlreadyParsed.contains(dataType)) break;
            dataAlreadyParsed.add(dataType);

            var data = WristbandData(
              dateTime: dateTime,
              dataType: dataType,
              rawValue: map[key],
              formattedValue: "${map[key]} SpO²",
            );
            _data.add(data);
            break;
          case 'respiratoryRateValue':
            const dataType = WristbandDataType.respiratoryRate;
            if (dataAlreadyParsed.contains(dataType)) break;
            dataAlreadyParsed.add(dataType);

            var data = WristbandData(
              dateTime: dateTime,
              dataType: dataType,
              rawValue: map[key],
              formattedValue: "${map[key]} rpm",
            );
            _data.add(data);
            break;
          case 'tempIntValue':
          case 'tempFloatValue':
            const dataType = WristbandDataType.temperature;
            if (dataAlreadyParsed.contains(dataType)) break;
            dataAlreadyParsed.add(dataType);

            final tempIntValue = map['tempIntValue'];
            final tempFloatValue = map['tempFloatValue'];
            var data = WristbandData(
              dateTime: dateTime,
              dataType: dataType,
              rawValue: double.parse("$tempIntValue.$tempFloatValue"),
              formattedValue: "$tempIntValue.$tempFloatValue ºC",
            );
            _data.add(data);
            break;
          case 'temperatureValue':
            const dataType = WristbandDataType.temperature;
            if (dataAlreadyParsed.contains(dataType)) break;
            dataAlreadyParsed.add(dataType);

            var data = WristbandData(
              dateTime: dateTime,
              dataType: dataType,
              rawValue: map[key],
              formattedValue: "${map[key]} ºC",
            );
            _data.add(data);
            break;
          case 'DBPValue':
          case 'SBPValue':
            const dataType = WristbandDataType.bloodPressure;
            if (dataAlreadyParsed.contains(dataType)) break;
            dataAlreadyParsed.add(dataType);

            final sbpValue = map['SBPValue'];
            final dbpValue = map['DBPValue'];
            var data = WristbandData(
              dateTime: dateTime,
              dataType: dataType,
              rawValue: {
                'systolic': sbpValue,
                'diastolic': dbpValue,
              },
              formattedValue: "$sbpValue x $dbpValue",
            );
            _data.add(data);
            break;
          case 'bloodSBP':
          case 'bloodDBP':
            const dataType = WristbandDataType.bloodPressure;
            if (dataAlreadyParsed.contains(dataType)) break;
            dataAlreadyParsed.add(dataType);

            final sbpValue = map['bloodSBP'];
            final dbpValue = map['bloodDBP'];
            var data = WristbandData(
              dateTime: dateTime,
              dataType: dataType,
              rawValue: {
                'systolic': sbpValue,
                'diastolic': dbpValue,
              },
              formattedValue: "$sbpValue x $dbpValue",
            );
            _data.add(data);
            break;
          case 'stepValue':
            const dataType = WristbandDataType.steps;
            if (dataAlreadyParsed.contains(dataType)) break;
            dataAlreadyParsed.add(dataType);

            var data = WristbandData(
              dateTime: dateTime,
              dataType: dataType,
              rawValue: map[key],
              formattedValue: "${map[key]} steps",
            );
            _data.add(data);
            break;
          case 'sportDistance':
            const dataType = WristbandDataType.distance;
            if (dataAlreadyParsed.contains(dataType)) break;
            dataAlreadyParsed.add(dataType);

            var data = WristbandData(
              dateTime: dateTime,
              dataType: dataType,
              rawValue: map[key],
              formattedValue: "${map[key]} meters",
            );
            _data.add(data);
            break;
          case 'sportCalorie':
            const dataType = WristbandDataType.calories;
            if (dataAlreadyParsed.contains(dataType)) break;
            dataAlreadyParsed.add(dataType);

            var data = WristbandData(
              dateTime: dateTime,
              dataType: dataType,
              rawValue: map[key],
              formattedValue: "${map[key]} kcal",
            );
            _data.add(data);
            break;
          default:
        }
      }
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

enum WristbandDataType {
  bloodOxygen,
  bloodPressure,
  calories,
  distance,
  heartRate,
  respiratoryRate,
  steps,
  temperature,
}

class WristbandData {
  final DateTime dateTime;
  final WristbandDataType dataType;
  final String formattedValue;
  final dynamic rawValue;

  WristbandData({
    required this.dateTime,
    required this.dataType,
    required this.formattedValue,
    required this.rawValue,
  });

  WristbandData copyWith({
    DateTime? dateTime,
    WristbandDataType? type,
    String? formattedValue,
    dynamic? rawValue,
  }) {
    return WristbandData(
      dateTime: dateTime ?? this.dateTime,
      dataType: type ?? dataType,
      formattedValue: formattedValue ?? this.formattedValue,
      rawValue: rawValue ?? this.rawValue,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'dateTime': dateTime.millisecondsSinceEpoch,
      'type': dataType.toString(),
      'formattedValue': formattedValue,
      'rawValue': rawValue,
    };
  }

  factory WristbandData.fromMap(Map<String, dynamic> map) {
    return WristbandData(
      dateTime: DateTime.fromMillisecondsSinceEpoch(map['dateTime']),
      dataType: map['type'],
      formattedValue: map['formattedValue'],
      rawValue: map['rawValue'],
    );
  }

  String toJson() => json.encode(toMap());

  factory WristbandData.fromJson(String source) =>
      WristbandData.fromMap(json.decode(source));

  @override
  String toString() {
    return 'WristbandData(dateTime: $dateTime, type: $dataType, formattedValue: $formattedValue, rawValue: $rawValue)';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    return other is WristbandData &&
        other.dateTime == dateTime &&
        other.dataType == dataType &&
        other.formattedValue == formattedValue &&
        other.rawValue == rawValue;
  }

  @override
  int get hashCode {
    return dateTime.hashCode ^
        dataType.hashCode ^
        formattedValue.hashCode ^
        rawValue.hashCode;
  }
}
