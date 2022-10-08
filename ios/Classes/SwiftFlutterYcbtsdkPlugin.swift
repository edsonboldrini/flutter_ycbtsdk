import Flutter
import UIKit
import YCProductSDK

public var devicesList = [CBPeripheral]()

public class SwiftFlutterYcbtsdkPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let namespace: String = "flutter_ycbtsdk"

    let methodChannel = FlutterMethodChannel(name: "\(namespace)/methods", binaryMessenger: registrar.messenger())
    let instance: SwiftFlutterYcbtsdkPlugin = SwiftFlutterYcbtsdkPlugin()
    registrar.addMethodCallDelegate(instance, channel: methodChannel)

    let eventChannel = FlutterEventChannel(name: "\(namespace)/events", binaryMessenger: registrar.messenger())
    eventChannel.setStreamHandler(SwiftStreamHandler())

    YCProduct.setLogLevel(.normal)
    _ = YCProduct.shared
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {    
    switch call.method {
    case "getPlatformVersion":
        result("iOS " + UIDevice.current.systemVersion)
    default:
        result("Not implemented method")
    }
  }
}
