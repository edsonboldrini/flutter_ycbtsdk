//
//  YCQueryViewController.swift
//  SwiftDemo
//
//  Created by macos on 2021/11/20.
//

import UIKit
import YCProductSDK

class YCQueryViewController: UIViewController {
    
    /// 设置选项
    private lazy var items = [
        "Basic info",
        "Mac address",
        "Device model",
        "HR",
        "BP",
        "User configuration",
        "Theme",
        "Electrode position",
        "Screen info",
        "Real timee xercise",
        "History summary",
        "Real time temperature",
        "Screen display info",
        
        "BloodOxygen",
        "Ambient light",
        "Temperature humidity",
        "Sensor sample info",
        "Work mode",
        "Upload reminder info",
        "MCU"
    ]
    
    @IBOutlet weak var listView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        navigationItem.title = "Query"
        listView.register(UITableViewCell.self, forCellReuseIdentifier: String(describing: UITableView.self))
    }

}

extension YCQueryViewController {
    
    func exampleForItems(_ index: Int) {
        
        switch index {
        case 0:     // basic info
            YCProduct.queryDeviceBasicInfo { state, response in
                
                if state == YCProductState.succeed,
                 let info = response as? YCDeviceBasicInfo {
                    
                    print(info.batteryPower)
                }
            }
            
        case 1: // macaddress
            YCProduct.queryDeviceMacAddress { state, response in
                
                if state == YCProductState.succeed,
                   let macaddress = response as? String {
                    
                    print(macaddress)
                }
            }
            
        case 2: // DeviceModel
            YCProduct.queryDeviceModel { state, response in
                
                if state == YCProductState.succeed,
                   let name = response as? String {
                    
                    print(name)
                }
            }
            
        case 3:
//            YCProduct.queryDeviceFunctionEnableState { state, response in
//
//            }
            
            YCProduct.queryDeviceCurrentHeartRate { state, response in
                
                if state == YCProductState.succeed,
                   let info = response as? YCDeviceCurrentHeartRate {
                    
                    print(info.isMeasuring, info.heartRate)
                }
            }
            
        case 4:
            YCProduct.queryDeviceCurrentBloodPressure { state, response in
                
                if state == YCProductState.succeed,
                   let info = response as? YCDeviceCurrentBloodPressure {
                    
                    print(info.isMeasuring, info.systolicBloodPressure, info.diastolicBloodPressure)
                }
            }
            
        case 5:
            YCProduct.queryDeviceUserConfiguration { state, response in
                
                if state == .succeed,
                   let info = response as? YCProductUserConfiguration {
                    
                    print(info.age)
                }
            }
            
        case 6:
            YCProduct.queryDeviceTheme { state, response in
                
                if state == YCProductState.succeed,
                   let info = response as? YCDeviceTheme {
                    
                    print(info.themeCount, info.themeIndex)
                }
            }
            
        case 7:
            YCProduct.queryDeviceElectrodePosition { state, response in
                
                if state == .succeed,
                let info = response as? YCDeviceElectrodePosition {
                    
                    print(info.rawValue)
                }
            }
            
        case 8:
            YCProduct.queryDeviceScreenInfo { state, response in
                
                if state == .succeed,
                let info = response as? YCDeviceScreenInfo {
                    
                    print(info.screenWidth, info.screenHeight, info.fontWidth, info.fontHeight)
                }
            }
            
        case 9:
            YCProduct.queryDeviceCurrentExerciseInfo { state, response in
                
                if state == .succeed,
                let info = response as? YCDeviceCurrentExercise {
                    
                    print(info.step, info.calories, info.distance)
                }
            }
            
        case 10:
            YCProduct.queryDeviceHistorySummary { state, response in
                
                if state == .succeed,
                let info = response as? YCDeviceHistorySummary {
                    
                    print(info.sleepTime, info.sportCount, info.heartRateCount)
                }
            }
            
        case 11:
            YCProduct.queryDeviceRealTimeTemperature { state, response in
                if state == .succeed,
                   let temperature = response as? Double {
                    
                    print(temperature)
                }
            }
            
        case 12:
            YCProduct.queryDeviceScreenDisplayInfo { state, response in
                
                if state == .succeed,
                   let info = response as? YCDeviceScreenDisplayInfo {
                    
                    print(info.brightnessLevel, info.restScreenTime, info.language, info.workmode)
                }
            }
            
        case 13:
            YCProduct.queryDeviceBloodOxygen { state, response in
                
                if state == .succeed,
                let info = response as? YCDeviceBloodOxygen {
                    
                    print(info.bloodOxygen)
                }
            }
            
        case 14:
            YCProduct.queryDeviceAmbientLight { state, response in
                
                if state == .succeed,
                let info = response as? YCDeviceAmbientLight {
                    
                    print(info.ambientLight)
                }
            }
            
        case 15:
            YCProduct.queryDeviceTemperatureHumidity { state, response in
                
                if state == .succeed,
                let info = response as? YCDeviceTemperatureHumidity {
                    
                    print(info.temperature, info.humidity)
                }
            }
            
        case 16:
            YCProduct.queryDeviceSensorSampleInfo(dataType: YCDeviceDataCollectionType.ppg) { state, response in
                
                if state == .succeed,
                let info = response as? YCDeviceSensorSampleInfo {
                    
                    print(info.isOn, info.acquisitionTime, info.acquisitionInterval)
                }
            }
            
        case 17:
            YCProduct.queryDeviceWorkMode { state, response in
                
                if state == .succeed,
                let info = response as? YCDeviceWorkModeType {
                    
                    print(info)
                }
            }
            
        case 18:
            YCProduct.queryDeviceUploadReminderInfo { state, response in
                
                if state == .succeed,
                   let info = response as? YCDeviceUploadReminderInfo {
                    
                    print(info.isOn, info.threshold)
                }
            }
            
        case 19:
            YCProduct.queryDeviceMCU { state, response in
                
                if state == .succeed,
                let mcu = response as? YCDeviceMCUType{
                    
                    print(mcu)
                
                } else if state == .unavailable {
                    
                    print("NRF52832")
                }
            }
            
        case 20:
            YCProduct.queryDeviceRemindSettingInfo(dataType: .deviceDisconnected) { state, response in
                
                if state == .succeed,
                   let state = response as? YCDeviceReminderSettingState
                {
                    print(state == .on)
                }
            }
            
        default:
            break
        }
    }
}

extension YCQueryViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        tableView.deselectRow(at: indexPath, animated: true)
        
        exampleForItems(indexPath.row)
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: String(describing: UITableView.self), for: indexPath)
        
        cell.textLabel?.text = "\(indexPath.row + 1). " + items[indexPath.row]
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return items.count
    }
}
