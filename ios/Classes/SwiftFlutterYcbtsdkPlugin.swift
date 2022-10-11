import Flutter
import UIKit
import YCProductSDK
import CoreBluetooth
import SwiftyJSON

public let NAMESPACE: String = "flutter_ycbtsdk"
public var devicesList = [CBPeripheral]()

public class SwiftFlutterYcbtsdkPlugin: NSObject, FlutterPlugin {
	public static var instance: SwiftFlutterYcbtsdkPlugin?
	public static var methodChannel: FlutterMethodChannel?
	public static var eventChannel: FlutterEventChannel?
	
	public static func register(with registrar: FlutterPluginRegistrar) {
		SwiftFlutterYcbtsdkPlugin.methodChannel = FlutterMethodChannel(name: "\(NAMESPACE)/methods", binaryMessenger: registrar.messenger())
		SwiftFlutterYcbtsdkPlugin.instance = SwiftFlutterYcbtsdkPlugin()
		registrar.addMethodCallDelegate(SwiftFlutterYcbtsdkPlugin.instance!, channel: SwiftFlutterYcbtsdkPlugin.methodChannel!)
		
		SwiftFlutterYcbtsdkPlugin.eventChannel = FlutterEventChannel(name: "\(NAMESPACE)/events", binaryMessenger: registrar.messenger())
		SwiftFlutterYcbtsdkPlugin.eventChannel?.setStreamHandler(SwiftStreamHandler())
		
		YCProduct.setLogLevel(.normal)
		_ = YCProduct.shared
	}
	
	public func invokeFlutterMethodChannel(method: String, arguments: String) {
		print("invokeFlutterMethodChannel: \(method) \(arguments)")
		SwiftFlutterYcbtsdkPlugin.methodChannel?.invokeMethod(method, arguments: arguments)
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
			let scanTimeout: Double = (call.arguments as? Double) ?? 10
			print("scanTimeout: \(scanTimeout) seconds")
			devicesList.removeAll()
			
			YCProduct.scanningDevice(delayTime: scanTimeout) { devices, error in
				for device in devices {
					print("name: \(device.name ?? ""); mac: \(device.macAddress); rssi: \(device.rssiValue)")
					devicesList.append(device)
					let BLEObject: ScanBLEResponse = ScanBLEResponse(name: device.name ?? "", mac: device.macAddress, rssi: device.rssiValue)
					do {
						let jsonData = try JSONEncoder().encode(BLEObject)
						let jsonString = String(data: jsonData, encoding: .utf8)!
						self.invokeFlutterMethodChannel(method: "onScanResult", arguments: jsonString)
					} catch {
						print(error)
					}
				}
			}
			result(nil)
			break
		case "stopScan":
			result(nil)
			break
		case "connectDevice":
			let mac: String? = call.arguments as? String
			
			if mac == nil {
				print("no mac provided")
				result(nil)
				break
			}
			
			let device = devicesList.first(where: {$0.macAddress == mac})
			print("device: \(device?.macAddress ?? "nil")")
			
			if (device == nil) {
				print("no device found")
				result(nil)
				break
			}
			
			YCProduct.connectDevice(device!) { state, error in
				print("state: \(state)")
				if state == .connected {
					print("connected")
					do {
						let jsonObject: [String: String]  = [mac!: "connected"]
						let jsonData = try JSONEncoder().encode(jsonObject)
						let jsonString = String(data: jsonData, encoding: .utf8)!
						print(jsonString)
						result(jsonString)
					} catch {
						print(error)
					}
				}
			}
			break
		case "disconnectDevice":
			let mac: String? = call.arguments as? String
			let device = devicesList.first(where: {$0.macAddress == mac})
			print("device: \(device?.macAddress ?? "nil")")
			
			if (device == nil) {
				YCProduct.disconnectDevice()
			} else {
				YCProduct.disconnectDevice(device)
			}
			
			do {
				print("disconnected")
				let jsonObject: [String: String]  = [mac ?? "all" : "disconnected"]
				let jsonData = try JSONEncoder().encode(jsonObject)
				let jsonString = String(data: jsonData, encoding: .utf8)!
				print(jsonString)
				result(jsonString)
			} catch {
				print(error)
			}
			break
		case "healthHistoryData":
			YCProduct.queryHealthData(datatType: YCQueryHealthDataType.combinedData) { state, response in
				if state == .succeed, let data = response as? [YCHealthDataCombinedData] {
					for info in data {
						do {
							let healthData: DataResponse = DataResponse(startTimestamp: info.startTimeStamp, heartValue: info.heartRate, OOValue: info.bloodOxygen, respiratoryRateValue: info.respirationRate, temperatureValue: info.temperature, DBPValue: info.diastolicBloodPressure, SBPValue: info.systolicBloodPressure)
							let jsonData = try JSONEncoder().encode(healthData)
							let jsonString = String(data: jsonData, encoding: .utf8)!
							self.invokeFlutterMethodChannel(method: "onDataResponse", arguments: jsonString)
						} catch {
							print(error)
						}
					}
				}
			}
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
		print("onListen: \(arguments ?? "nil")")
		
		if arguments == nil { return nil }
		
		if arguments as! String == "startScan"{
			YCProduct.scanningDevice { devices, error in
				for device in devices {
					print(device.name ?? "", device.macAddress)
					devicesList.append(device)
					let BLEObject : ScanBLEResponse = ScanBLEResponse(name: device.name ?? "", mac: device.macAddress, rssi: device.rssiValue)
					do {
						let jsonData = try JSONEncoder().encode(BLEObject)
						let jsonString = String(data: jsonData, encoding: .utf8)!
						events(jsonString)
					} catch {
						print(error)
					}
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

