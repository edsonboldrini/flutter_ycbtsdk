import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_ycbt_client_method_channel.dart';

abstract class FlutterYcbtClientPlatform extends PlatformInterface {
  /// Constructs a FlutterYcbtClientPlatform.
  FlutterYcbtClientPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterYcbtClientPlatform _instance = MethodChannelFlutterYcbtClient();

  /// The default instance of [FlutterYcbtClientPlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterYcbtClient].
  static FlutterYcbtClientPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterYcbtClientPlatform] when
  /// they register themselves.
  static set instance(FlutterYcbtClientPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<void> startScan() {
    throw UnimplementedError('startScan() has not been implemented.');
  }

  Future<void> stopScan() {
    throw UnimplementedError('stopScan() has not been implemented.');
  }
}
