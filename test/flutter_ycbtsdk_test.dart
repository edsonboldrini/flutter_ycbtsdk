import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_ycbtsdk/flutter_ycbtsdk.dart';
import 'package:flutter_ycbtsdk/flutter_ycbtsdk_platform_interface.dart';
import 'package:flutter_ycbtsdk/flutter_ycbtsdk_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterYcbtsdkPlatform
    with MockPlatformInterfaceMixin
    implements FlutterYcbtsdkPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FlutterYcbtsdkPlatform initialPlatform = FlutterYcbtsdkPlatform.instance;

  test('$MethodChannelFlutterYcbtsdk is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterYcbtsdk>());
  });

  test('getPlatformVersion', () async {
    FlutterYcbtsdk flutterYcbtsdkPlugin = FlutterYcbtsdk();
    MockFlutterYcbtsdkPlatform fakePlatform = MockFlutterYcbtsdkPlatform();
    FlutterYcbtsdkPlatform.instance = fakePlatform;

    expect(await flutterYcbtsdkPlugin.getPlatformVersion(), '42');
  });
}
