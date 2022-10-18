import Flutter
import UIKit
import YCProductSDK
import CoreBluetooth
import SwiftyJSON

public let NAMESPACE: String = "flutter_ycbtsdk"
public var devicesList = [CBPeripheral]()
public var currentDevice: CBPeripheral?

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
		
		//		NotificationCenter.default.addObserver(
		//		 self,
		//		 selector: #selector(receiveRealTimeData(_:)),
		//		 name: YCProduct.receivedRealTimeNotification,
		//		 object: nil
		//		)
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
			
			if devicesList.isEmpty {
				print("no devices found")
				result(nil)
				break
			}
			
			let device = devicesList.first(where: {$0.macAddress == mac})
			print("device: \(device?.macAddress ?? "nil")")
			
			if (device == nil) {
				print("no device match mac: \(mac!)")
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
						currentDevice = device
						result(jsonString)
					} catch {
						print(error)
					}
				}
			}
			break
		case "disconnectDevice":
			print("device: \(currentDevice?.macAddress ?? "all")")
			
			if (currentDevice == nil) {
				YCProduct.disconnectDevice()
			} else {
				YCProduct.disconnectDevice(currentDevice)
			}
			
			do {
				print("disconnected")
				let jsonObject: [String: String]  = [currentDevice?.macAddress ?? "all" : "disconnected"]
				let jsonData = try JSONEncoder().encode(jsonObject)
				let jsonString = String(data: jsonData, encoding: .utf8)!
				currentDevice = nil
				result(jsonString)
			} catch {
				print(error)
			}
			break
		case "connectState":
			result(nil)
			break
		case "resetQueue":
			result(nil)
			break
		case "shutdownDevice":
			YCProduct.deviceSystemOperator(mode: .shutDown) { state, response in
				if state == .succeed {
					print("succeed")
				} else {
					print("failed")
				}
				result(nil)
			}
			break
		case "restoreFactory":
			YCProduct.setDeviceReset() { state, response in
				if state == .succeed {
					print("succeed")
				} else {
					print("failed")
				}
				result(nil)
			}
			break
		case "startEcgTest":
			YCProduct.startECGMeasurement { state, _ in
				if state == .succeed {
					print("startedECG")
					result(nil)
				}
			}
			result(nil)
			break
		case "stopEcgTest":
			YCProduct.stopECGMeasurement { state, _ in
				if state == .succeed {
					print("stoppedECG")
					result(nil)
				}
			}
			break
		case "healthHistoryData":
			YCProduct.queryHealthData(datatType: YCQueryHealthDataType.combinedData) { state, response in
				if state == .succeed, let data = response as? [YCHealthDataCombinedData] {
					for info in data {
						do {
							let healthData: DataResponse = DataResponse(startTime: info.startTimeStamp * 1000, heartValue: info.heartRate, OOValue: info.bloodOxygen, respiratoryRateValue: info.respirationRate, temperatureValue: info.temperature, SBPValue: info.systolicBloodPressure, DBPValue: info.systolicBloodPressure)
							let jsonData = try JSONEncoder().encode(healthData)
							let jsonString = String(data: jsonData, encoding: .utf8)!
							self.invokeFlutterMethodChannel(method: "onDataResponse", arguments: jsonString)
						} catch {
							print(error)
						}
					}
				}
			}
			YCProduct.queryHealthData(datatType: YCQueryHealthDataType.heartRate) { state, response in
				if state == .succeed, let data = response as? [YCHealthDataHeartRate] {
					for info in data {
						do {
							let healthData: DataResponse = DataResponse(startTime: info.startTimeStamp * 1000, heartValue: info.heartRate)
							let jsonData = try JSONEncoder().encode(healthData)
							let jsonString = String(data: jsonData, encoding: .utf8)!
							self.invokeFlutterMethodChannel(method: "onDataResponse", arguments: jsonString)
						} catch {
							print(error)
						}
					}
				}
			}
			YCProduct.queryHealthData(datatType: YCQueryHealthDataType.bloodOxygen) { state, response in
				if state == .succeed, let data = response as? [YCHealthDataBloodOxygen] {
					for info in data {
						do {
							let healthData: DataResponse = DataResponse(startTime: info.startTimeStamp * 1000, OOValue: info.bloodOxygen)
							let jsonData = try JSONEncoder().encode(healthData)
							let jsonString = String(data: jsonData, encoding: .utf8)!
							self.invokeFlutterMethodChannel(method: "onDataResponse", arguments: jsonString)
						} catch {
							print(error)
						}
					}
				}
			}
			YCProduct.queryHealthData(datatType: YCQueryHealthDataType.bodyTemperature) { state, response in
				if state == .succeed, let data = response as? [YCHealthDataBodyTemperature] {
					for info in data {
						do {
							let healthData: DataResponse = DataResponse(startTime: info.startTimeStamp * 1000, temperatureValue: info.temperature)
							let jsonData = try JSONEncoder().encode(healthData)
							let jsonString = String(data: jsonData, encoding: .utf8)!
							self.invokeFlutterMethodChannel(method: "onDataResponse", arguments: jsonString)
						} catch {
							print(error)
						}
					}
				}
			}
			YCProduct.queryHealthData(datatType: YCQueryHealthDataType.bloodPressure) { state, response in
				if state == .succeed, let data = response as? [YCHealthDataBloodPressure] {
					for info in data {
						do {
							let healthData: DataResponse = DataResponse(startTime: info.startTimeStamp * 1000, SBPValue: info.systolicBloodPressure, DBPValue: info.diastolicBloodPressure)
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
		case "deleteHealthHistoryData":
			YCProduct.deleteHealthData(datatType: YCDeleteHealthDataType.combinedData) { state, response in
				if state == .succeed {
					print("succeed")
				} else {
					print("failed")
				}
				result(nil)
			}
			break
		case "sportHistoryData":
			YCProduct.queryHealthData(datatType: YCQueryHealthDataType.sportModeHistoryData) { state, response in
				if state == .succeed, let data = response as? [YCHealthDataSportModeHistory] {
					for info in data {
						do {
							let healthData: DataResponse = DataResponse(sportStartTime: info.startTimeStamp * 1000, sportEndTime: info.endTimeStamp * 1000, sportStep: info.step, sportDistance: info.distance, sportCalorie: info.calories)
							let jsonData = try JSONEncoder().encode(healthData)
							let jsonString = String(data: jsonData, encoding: .utf8)!
							self.invokeFlutterMethodChannel(method: "onDataResponse", arguments: jsonString)
						} catch {
							print(error)
						}
					}
					result(nil)
				}
			}
			break
		case "deleteSportHistoryData":
			YCProduct.deleteHealthData(datatType: YCDeleteHealthDataType.sportModeHistoryData) { state, response in
				if state == .succeed {
					print("succeed")
				} else {
					print("failed")
				}
				result(nil)
			}
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

