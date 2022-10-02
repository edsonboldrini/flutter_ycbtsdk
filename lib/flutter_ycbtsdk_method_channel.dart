part of flutter_ycbtsdk;

/// An implementation of [FlutterYcbtsdkPlatform] that uses method channels.
class MethodChannelFlutterYcbtsdk extends FlutterYcbtsdkPlatform {
  @override
  Future<String?> getPlatformVersion() async {
    final version = await FlutterYcbtsdk.instance.methodChannel
        .invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<void> initPlugin() async {
    await FlutterYcbtsdk.instance.methodChannel
        .invokeMethod<String>('initPlugin');
  }

  @override
  Future startScan() async {
    final scanResult = await FlutterYcbtsdk.instance.methodChannel
        .invokeMethod<String>('startScan');
    return scanResult;
  }

  @override
  Future<void> stopScan() async {
    await FlutterYcbtsdk.instance.methodChannel
        .invokeMethod<String>('stopScan');
  }
}
