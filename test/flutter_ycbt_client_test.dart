import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_ycbt_client/flutter_ycbt_client.dart';
import 'package:flutter_ycbt_client/flutter_ycbt_client_platform_interface.dart';
import 'package:flutter_ycbt_client/flutter_ycbt_client_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterYcbtClientPlatform
    with MockPlatformInterfaceMixin
    implements FlutterYcbtClientPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FlutterYcbtClientPlatform initialPlatform = FlutterYcbtClientPlatform.instance;

  test('$MethodChannelFlutterYcbtClient is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterYcbtClient>());
  });

  test('getPlatformVersion', () async {
    FlutterYcbtClient flutterYcbtClientPlugin = FlutterYcbtClient();
    MockFlutterYcbtClientPlatform fakePlatform = MockFlutterYcbtClientPlatform();
    FlutterYcbtClientPlatform.instance = fakePlatform;

    expect(await flutterYcbtClientPlugin.getPlatformVersion(), '42');
  });
}
