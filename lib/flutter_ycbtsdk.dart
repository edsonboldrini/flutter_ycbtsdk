library flutter_ycbtsdk;

import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_ycbtsdk_platform_interface.dart';

part 'flutter_ycbtsdk_method_channel.dart';

class FlutterYcbtsdk {
  static const String namespace = 'flutter_ycbtsdk';

  /// The method channel used to interact with the native platform methods.
  @visibleForTesting
  MethodChannel methodChannel = const MethodChannel('$namespace/methods');

  /// The event channel used to interact with the native platform state.
  @visibleForTesting
  EventChannel stateChannel = const EventChannel('$namespace/state');

  StreamController<MethodCall> methodStreamController =
      StreamController.broadcast(); // ignore: close_sinks
  Stream<MethodCall> get methodStream => methodStreamController
      .stream; // Used internally to dispatch methods from platform.

  /// Singleton boilerplate
  FlutterYcbtsdk._() {
    methodChannel.setMethodCallHandler((MethodCall call) async {
      print(call);
      methodStreamController.add(call);
    });
  }

  static final FlutterYcbtsdk _instance = FlutterYcbtsdk._();
  static FlutterYcbtsdk get instance => _instance;

  Future<String?> getPlatformVersion() {
    return FlutterYcbtsdkPlatform.instance.getPlatformVersion();
  }

  Future<void> initPlugin() {
    return FlutterYcbtsdkPlatform.instance.initPlugin();
  }

  Future startScan() {
    return FlutterYcbtsdkPlatform.instance.startScan();
  }

  Future<void> stopScan() {
    return FlutterYcbtsdkPlatform.instance.stopScan();
  }
}
