import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_ycbtsdk_platform_interface.dart';

/// An implementation of [FlutterYcbtsdkPlatform] that uses method channels.
class MethodChannelFlutterYcbtsdk extends FlutterYcbtsdkPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_ycbtsdk');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
