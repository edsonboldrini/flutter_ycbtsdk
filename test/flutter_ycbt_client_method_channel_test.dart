import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_ycbt_client/flutter_ycbt_client_method_channel.dart';

void main() {
  MethodChannelFlutterYcbtClient platform = MethodChannelFlutterYcbtClient();
  const MethodChannel channel = MethodChannel('flutter_ycbt_client');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
