//
//  YCSettingViewController.swift
//  SwiftDemo
//
//  Created by macos on 2021/12/6.
//

import UIKit
import YCProductSDK

class YCSettingViewController: UIViewController {
    
    private lazy var items = [
        "time",
        "step goal",
        "calories goal",
        "distance goal",
        "sleep goal",
        "sport time goal",
        "effective step goal",
        
        "user info",
        "unit",
        "sedentar",
        "antiLost",
        "info push",
        "heart rate alarm",
        "heart rate monitoring",
        "not disturb",
        "reset",
        "language",
        "wrist bright screen",
        "brightness",
        "skin color",
        "blood pressure level",
        "device name",
        "sensor sample Rate",
        "theme",
        "sleep reminder",
        "data collection",
        "blood pressure monitoring",
        "temperature alarm",
        "temperature monitoring",
        "breath screen",
        "ambient light monitoring",
        "work mode",
        "accident monitoring",
        "reminder type",
        "bloodOxygen monitoring",
        "temperature humidity monitoring",
        "upload reminder",
        "pedometer time",
        "broadcast interval",
        "transmit power",
        "heart rate zone",
        "show insurance",
        "data collection setting for work mode",
        "bp alarm",
        "sp02 alarm",
        "motor vibration",
        
        "query alarm",
        "add alarm",
        "delete alarm",
        "modify alarm",
        
        "event enable",
        "add event",
        "delete event",
        "modify event",
        "query event",
    ]
    
    @IBOutlet weak var listView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        navigationItem.title = "Setting"
        listView.register(UITableViewCell.self, forCellReuseIdentifier: String(describing: UITableView.self))
    }

}

extension YCSettingViewController {
    
    private func exampleForItems(_ index: Int) {
        
        switch index {
        case 0:
            YCProduct.setDeviceTime(
                year: 2021,
                month: 12,
                day: 6,
                hour: 14,
                minute: 38,
                second: 59,
                weekDay: .monday) { state, response in
                
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 1: // step
            YCProduct.setDeviceStepGoal(
                step: 10000) { state, _ in
                
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 2:  // Calories
            YCProduct.setDeviceCaloriesGoal(calories: 1000) { state, _ in
                
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 3: // Distance
            YCProduct.setDeviceDistanceGoal(distance: 10000) { state, _ in
                
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 4: // Sleep
            YCProduct.setDeviceSleepGoal(hour: 8, minute: 30) { state, response in
                
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 5: // sport time
            YCProduct.setDeviceSportTimeGoal(hour: 1, minute: 20) { state, response in
                
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 6: // EffectiveStep
            YCProduct.setDeviceEffectiveStepsGoal(effectiveSteps: 8000) { state, response in
                
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 7: // user info
            YCProduct.setDeviceUserInfo(height: 180, weight: 90, gender: .male, age: 18) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 8: // unit
            YCProduct.setDeviceUnit(distance: .km, weight: .kg, temperature: .celsius, timeFormat: .hour24) { state, response in
                
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 9: // Sedentary
            YCProduct.setDeviceSedentary(startHour1: 9, startMinute1: 0, endHour1: 12, endMinute1: 30, startHour2: 13, startMinute2: 30, endHour2: 18, endMinute2: 00, interval: 15, repeat: [
                .monday, .tuesday, .wednesday, .thursday, .friday, .enable]) { state, response in
                    
                    if state == .succeed {
                        print("success")
                    } else {
                        print("fail")
                    }
                }
            
        case 10:
            YCProduct.setDeviceAntiLost(antiLostType: .middleDistance) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
//            YCProduct.setDeviceAntiLost(rssi: 0, delay: 1, supportDisconnectDelay: false, repeat: true) { state, response in
//                if state == .succeed {
//                    print("success")
//                } else {
//                    print("fail")
//                }
//            }
            
        case 11:  // info push
            YCProduct.setDeviceInfoPush(isEnable: true, infoPushType: [.call, .qq, .weChat]) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
//            YCProduct.setDeviceInfoPush(isEnable: false, infoPushType: [.call, .qq, .weChat ]) { state, response in
//                if state == .succeed {
//                    print("success")
//                } else {
//                    print("fail")
//                }
//            }
            
        case 12: // hr alarm
            YCProduct.setDeviceHeartRateAlarm(isEnable: true, maxHeartRate: 100, minHeartRate: 50) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 13: // HeartRate Monitoring
            YCProduct.setDeviceHeartRateMonitoringMode(isEnable: true, interval: 60) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 14: // not disturb
            YCProduct.setDeviceNotDisturb(isEable: true, startHour: 9, startMinute: 30, endHour: 12, endMinute: 0) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 15:
            YCProduct.setDeviceReset { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 16:
            YCProduct.setDeviceLanguage(language: .persian) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 17: // WristBrightScree
            YCProduct.setDeviceWristBrightScreen(isEnable: true) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 18: // Brightness
            YCProduct.setDeviceDisplayBrightness(level: .middle) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 19: // skin color
            YCProduct.setDeviceSkinColor(level: .yellow) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 20: // blood pressure level
            YCProduct.setDeviceBloodPressureRange(level: .normal) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 21: // device name
            YCProduct.setDeviceName(name: "YC2021") { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 22: // sensor sample Rate
            YCProduct.setDeviceSensorSamplingRate(ppg: 250, ecg: 100, gSensor: 25, tempeatureSensor: 10) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 23: // theme
            YCProduct.setDeviceTheme(index: 0) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 24: // 睡眠提醒时间
            YCProduct.setDeviceSleepReminder(hour: 22, minute: 30, repeat: [.monday, .thursday, .wednesday, .enable]) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 25:
            YCProduct.setDeviceDataCollection(isEnable: true, dataType: .ppg, acquisitionTime: 90, acquisitionInterval: 60) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 26: // BloodPressureMonitoring
            YCProduct.setDeviceBloodPressureMonitoringMode(isEnable: true, interval: 60) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 27: // temperature alarm
            YCProduct.setDeviceTemperatureAlarm(isEnable: true, highTemperatureIntegerValue: 37, highTemperatureDecimalValue: 3, lowTemperatureIntegerValue: 35, lowTemperatureDecimalValue: 5) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 28: // temperature monitoring
            YCProduct.setDeviceTemperatureMonitoringMode(isEnable: true, interval: 60) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 29: // 息屏时间
            YCProduct.setDeviceBreathScreen(interval: .fifteen) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 30: // ambient light monitoring
            YCProduct.setDeviceAmbientLightMonitoringMode(isEnable: true, interval: 60) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 31: // work mode
            YCProduct.setDeviceWorkMode(mode: .normal) { state, response in
                
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 32:    // 意外监测
            YCProduct.setDeviceAccidentMonitoring(isEnable: true) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 33: // reminder type
            YCProduct.setDeviceReminderType(isEnable: true, remindType: .deviceDisconnected) { state, response in
                
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 34: // bloodOxygen monitoring
            YCProduct.setDeviceBloodOxygenMonitoringMode(isEnable: true, interval: 60) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 35: // temperature humidity monitoring
            YCProduct.setDeviceTemperatureHumidityMonitoringMode(isEnable: true, interval: 60) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 36: // upload reminder
            YCProduct.setDeviceUploadReminder(isEnable: true, threshold: 50) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 37: // pedometer time
            YCProduct.setDevicePedometerTime(time: 10) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 38: // broadcast interval
            YCProduct.setDeviceBroadcastInterval(interval: 20) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 39: // transmit power
            YCProduct.setDeviceTransmitPower(power: 0) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 40: // hr zone
            YCProduct.setDeviceExerciseHeartRateZone(zoneType: .retreat, minimumHeartRate: 60, maximumHeartRate: 100) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 41: // Insurance
            YCProduct.setDeviceInsuranceInterfaceDisplay(isEnable: true) { state, response in                
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 42:
            YCProduct.setDeviceWorkModeDataCollection(mode: .normal, dataType: .ppg, acquisitionTime: 90, acquisitionInterval: 60) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 43: // bp alarm
            YCProduct.setDeviceBloodPressureAlarm(isEnable: true, maximumSystolicBloodPressure: 250, maximumDiastolicBloodPressure: 140, minimumSystolicBloodPressure: 160, minimumDiastolicBloodPressure: 90) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 44: // spo2 alarm
            YCProduct.setDeviceBloodOxygenAlarm(isEnable: true, minimum: 88) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 45:
            YCProduct.setDeviceMotorVibrationTime(time: 2 * 1000) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 46: // query alarm
            YCProduct.queryDeviceAlarmInfo { state, response in
                if state == .succeed,
                let datas = response as? [YCDeviceAlarmInfo] {
                    
                    for item in datas {
                        print(item.hour, item.minute)
                    }
                }
            }
        
        case 47:  // add alarm
            YCProduct.addDeviceAlarm(alarmType: .wakeUp, hour: 6, minute: 30, repeat: [.enable, .sunday, .saturday], snoozeTime: 0) { state, response in
                
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 48: // delete alarm
            YCProduct.deleteDeviceAlarm(hour: 6, minute: 30) { state, response in
                
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 49: // modify alarm
            YCProduct.modifyDeviceAlarm(oldHour: 6, oldMinute: 30, hour: 11, minute: 0, alarmType: .meeting, repeat: [.enable, .monday], snoozeTime: 0) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
            
        case 50: // event enable
            YCProduct.setDeviceEventEnable(isEnable: true) { state, response in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 51: // add event
            YCProduct.addDeviceEvent(name: "party", isEnable: true, hour: 19, minute: 50, interval: .ten, repeat: [.enable, .saturday]) { state, respose in
                
                if state == .succeed,
                   let eventID = respose as? UInt8 {
                    print("success \(eventID)")
                } else {
                    print("fail")
                }
            }
            
        case 52: // delete event
            YCProduct.deleteDeviceEvent(eventID: 1) { state, _ in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 53: // modify event
            YCProduct.modifyDeviceEvent(name: "sleep", eventID: 1, isEnable: true, hour: 22, minute: 30, interval: .twenty, repeat: [.enable, .monday, .tuesday, .wednesday, .thursday, .friday]) { state, _ in
                if state == .succeed {
                    print("success")
                } else {
                    print("fail")
                }
            }
            
        case 54: // query event
            YCProduct.queryDeviceEventnfo { state, response in
                
                if state == .succeed, let datas = response as? [YCDeviceEventInfo] {
                    
                    print("success")
                    for item in datas {
                        print(item.name, item.eventID,
                              item.hour, item.minute)
                    }
                } else {
                    
                    print("fail")
                }
            }
            
            
        default:
            break
        }
         
        
    }
}


extension YCSettingViewController: UITableViewDataSource, UITableViewDelegate {
    
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
