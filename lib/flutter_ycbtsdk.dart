
import 'flutter_ycbtsdk_platform_interface.dart';

class FlutterYcbtsdk {
  Future<String?> getPlatformVersion() {
    return FlutterYcbtsdkPlatform.instance.getPlatformVersion();
  }

  Future<void> startScan() async {
    await FlutterYcbtsdkPlatform.instance.startScan();
  }

  Future<void> stopScan() async {
    await FlutterYcbtsdkPlatform.instance.stopScan();
  }
}
