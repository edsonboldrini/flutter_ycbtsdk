import Flutter
import UIKit
import YCProductSDK
import CoreBluetooth
import SwiftyJSON

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
		print("\(call.method)...")
		
    switch call.method {
    case "getPlatformVersion":
			result("iOS " + UIDevice.current.systemVersion)
			break
		case "checkPermissions":
			result(nil)
			break
    case "startScan":
			let scanTimeout: Double = call.arguments as! Double
			YCProduct.scanningDevice(delayTime: scanTimeout) { devices, error in
				for device in devices {
					var scanBLEResponse : [ScanBLEResponse] = [ScanBLEResponse]()
					print("name: \(device.name ?? ""); mac: \(device.macAddress); rssi: \(device.rssiValue)")
					let BLEObject : ScanBLEResponse = ScanBLEResponse(name: device.name ?? "", mac: device.macAddress, rssi: device.rssiValue)
					scanBLEResponse.append(BLEObject)
					do {
						let jsonData = try JSONEncoder().encode(scanBLEResponse)
						let jsonString = String(data: jsonData, encoding: .utf8)!
						result(nil)
					} catch {
						print(error)
					}
				}
			}
			result(nil)
			break
		case "disconnectDevice":
			result(nil)
			break
    default:
			print("Method not implemented")
			result(nil)
			break
    }
  }
}

class SwiftStreamHandler: NSObject, FlutterStreamHandler {
	public func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
		if arguments as! String == "startScan"{
			YCProduct.scanningDevice { devices, error in
			var scanBLEResponse : [ScanBLEResponse] = [ScanBLEResponse]()
			devicesList = devices
				for device in devices {
					print(device.name ?? "", device.macAddress)
					let BLEObject : ScanBLEResponse = ScanBLEResponse(name: device.name ?? "", mac: device.macAddress, rssi: device.rssiValue)
					scanBLEResponse.append(BLEObject)
				}
				do {
					let jsonData = try JSONEncoder().encode(scanBLEResponse)
					let jsonString = String(data: jsonData, encoding: .utf8)!
					events(jsonString)
				} catch {
					print(error)
				}
				// let jsonData = self.json(from: scanBLEResponse)
				// events(jsonData)
			}
		}
		// events(FlutterError(code: "ERROR_CODE", message: "Detailed message", details: nil)) // in case of errors
		// events(FlutterEndOfEventStream) // when stream is over
		return nil
	}
    
	public func onCancel(withArguments arguments: Any?) -> FlutterError? {
		return nil
	}
	
	func json(from object:Any) -> String? {
		guard let data = try? JSONSerialization.data(withJSONObject: object, options: []) else {
				return nil
		}
		return String(data: data, encoding: String.Encoding.utf8)
	}
}

