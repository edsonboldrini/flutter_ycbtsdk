//
//  YCHealthDataViewController.swift
//  SwiftDemo
//
//  Created by macos on 2021/11/22.
//

import UIKit
import YCProductSDK

class YCHealthDataViewController: UIViewController {
    
    /// 设置选项
    private lazy var items = [
        
        "step",
        "sleep",
        "heartRate",
        "bloodPressure",
        "combinedData",
        "bloodOxygen",
        "temperatureHumidity",
        "bodyTemperature",
        "ambientLight",
        "wearState",
        "healthMonitoringData",
        "sportModeHistoryData"
    ]
    
    
    /// 列表
    @IBOutlet weak var listView: UITableView!
    

    override func viewDidLoad() {
        super.viewDidLoad()

        navigationItem.title = "Health data"
        listView.register(UITableViewCell.self, forCellReuseIdentifier: String(describing: UITableView.self))
    }
 

}

extension YCHealthDataViewController {
    
    
    /// 查询健康数据
    /// - Parameter index: <#index description#>
    func queryHealthData(_ index: NSInteger) {
        
        switch index {
        case 0:
            YCProduct.queryHealthData(datatType: YCQueryHealthDataType.step) { state, response in
                    
                if state == .succeed, let datas = response as? [YCHealthDataStep] {
                    
                    for info in datas {
                        print(info.startTimeStamp, info.endTimeStamp, info.step)
                    }
                }
            }
            
        case 1:
            YCProduct.queryHealthData(datatType: YCQueryHealthDataType.sleep) { state, response in
                    
                if state == .succeed, let datas = response as? [YCHealthDataSleep] {
                    
                    for info in datas {
                        
                        print(info)
                         
                    }
                }
            }
            
        case 2:
            YCProduct.queryHealthData(datatType: YCQueryHealthDataType.heartRate) { state, response in
                    
                if state == .succeed, let datas = response as? [YCHealthDataHeartRate] {
                    
                    for info in datas {
                        print(info.startTimeStamp, info.heartRate)
                    }
                }
            }
            
        case 3:
            YCProduct.queryHealthData(datatType: YCQueryHealthDataType.bloodPressure) { state, response in
                    
                if state == .succeed, let datas = response as? [YCHealthDataBloodPressure] {
                    
                    for info in datas {
                        print(info.startTimeStamp, info.systolicBloodPressure, info.diastolicBloodPressure
                        )
                    }
                }
            }
            
        case 4:
            YCProduct.queryHealthData(datatType: YCQueryHealthDataType.combinedData) { state, response in
                    
                if state == .succeed, let datas = response as? [YCHealthDataCombinedData] {
                    
                    for info in datas {
                        print(info.startTimeStamp, info.bloodOxygen, info.respirationRate,
                              info.temperature,
                              info.fat
                        )
                    }
                }
            }
            
        case 5:
            YCProduct.queryHealthData(datatType: YCQueryHealthDataType.bloodOxygen) { state, response in
                    
                if state == .succeed, let datas = response as? [YCHealthDataBloodOxygen] {
                    
                    for info in datas {
                        print(info.startTimeStamp, info.bloodOxygen
                        )
                    }
                }
            }
            
        case 6:
            YCProduct.queryHealthData(datatType: YCQueryHealthDataType.temperatureHumidity) { state, response in
                    
                if state == .succeed, let datas = response as? [YCHealthDataTemperatureHumidity] {
                    
                    for info in datas {
                        print(info.startTimeStamp,
                              info.temperature,
                              info.humidity
                        )
                    }
                }
            }
            
        case 7:
            YCProduct.queryHealthData(datatType: YCQueryHealthDataType.bodyTemperature) { state, response in
                    
                if state == .succeed, let datas = response as? [YCHealthDataBodyTemperature] {
                    
                    for info in datas {
                        print(info.startTimeStamp,
                              info.temperature
                        )
                    }
                }
            }
            
        case 8:
            YCProduct.queryHealthData(datatType: YCQueryHealthDataType.ambientLight) { state, response in
                    
                if state == .succeed, let datas = response as? [YCHealthDataAmbientLight] {
                    
                    for info in datas {
                        print(info.startTimeStamp,
                              info.ambientLight
                        )
                    }
                }
            }
            
        case 9:
            YCProduct.queryHealthData(datatType: YCQueryHealthDataType.wearState) { state, response in
                    
                if state == .succeed, let datas = response as? [YCHealthDataWearStateHistory] {
                    
                    for info in datas {
                        print(info.startTimeStamp,
                              info.state
                        )
                    }
                }
            }
            
        case 10:
            YCProduct.queryHealthData(datatType: YCQueryHealthDataType.healthMonitoringData) { state, response in
                    
                if state == .succeed, let datas = response as? [YCHealthDataMonitor] {
                    
                    for info in datas {
                        print(info.startTimeStamp,
                              info.step,
                              info.distance,
                              info.calories,
                              info.heartRate,
                              info.systolicBloodPressure,
                              info.diastolicBloodPressure,
                              info.bloodOxygen,
                              info.respirationRate
                        )
                    }
                }
            }
            
        case 11:
            YCProduct.queryHealthData(datatType: YCQueryHealthDataType.sportModeHistoryData) { state, response in
                    
                if state == .succeed, let datas = response as? [YCHealthDataSportModeHistory] {
                    
                    for info in datas {
                        print(info.startTimeStamp,
                              info.step,
                              info.distance,
                              info.calories,
                              info.heartRate,
                              info.sport,
                              info.flag
                        )
                    }
                }
            }
            
        default:
            break
        }
    }
    
    /// 删除数据
    func deleteHealthData(_ index: NSInteger) {
        
        switch index {
        case 0:
            YCProduct.deleteHealthData(datatType: YCDeleteHealthDataType.step) { state, response in
                
                if state == .succeed {
                    
                    print("Delete succeed")
                }
            }
            
        case 1:
            YCProduct.deleteHealthData(datatType: YCDeleteHealthDataType.sleep) { state, response in
                
                if state == .succeed {
                    
                    print("Delete succeed")
                }
            }
            
        case 2:
            YCProduct.deleteHealthData(datatType: YCDeleteHealthDataType.heartRate) { state, response in
                
                if state == .succeed {
                    
                    print("Delete succeed")
                }
            }
            
        case 3:
            YCProduct.deleteHealthData(datatType: YCDeleteHealthDataType.bloodPressure) { state, response in
                
                if state == .succeed {
                    
                    print("Delete succeed")
                }
            }
            
        case 4:
            YCProduct.deleteHealthData(datatType: YCDeleteHealthDataType.combinedData) { state, response in
                
                if state == .succeed {
                    
                    print("Delete succeed")
                }
            }
            
        case 5:
            YCProduct.deleteHealthData(datatType: YCDeleteHealthDataType.bloodOxygen) { state, response in
                
                if state == .succeed {
                    
                    print("Delete succeed")
                }
            }
            
        case 6:
            YCProduct.deleteHealthData(datatType: YCDeleteHealthDataType.temperatureHumidity) { state, response in
                
                if state == .succeed {
                    
                    print("Delete succeed")
                }
            }
            
        case 7:
            YCProduct.deleteHealthData(datatType: YCDeleteHealthDataType.bodyTemperature) { state, response in
                
                if state == .succeed {
                    
                    print("Delete succeed")
                }
            }
            
        case 8:
            YCProduct.deleteHealthData(datatType: YCDeleteHealthDataType.ambientLight) { state, response in
                
                if state == .succeed {
                    
                    print("Delete succeed")
                }
            }
            
        case 9:
            YCProduct.deleteHealthData(datatType: YCDeleteHealthDataType.wearState) { state, response in
                
                if state == .succeed {
                    
                    print("Delete succeed")
                }
            }
            
        case 10:
            YCProduct.deleteHealthData(datatType: YCDeleteHealthDataType.healthMonitoringData) { state, response in
                
                if state == .succeed {
                    
                    print("Delete succeed")
                }
            }
            
        case 11:
            YCProduct.deleteHealthData(datatType: YCDeleteHealthDataType.sportModeHistoryData) { state, response in
                
                if state == .succeed {
                    
                    print("Delete succeed")
                }
            }
            
        default:
            break
        }
    }
}

extension YCHealthDataViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        tableView.deselectRow(at: indexPath, animated: true)
        
        if indexPath.section == 0 {
            queryHealthData(indexPath.row)
        } else {
            deleteHealthData(indexPath.row)
        }
         
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        
        return 40
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        
        if section == 0 {
            return "Query"
        }
        
        return "Delete"
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: String(describing: UITableView.self), for: indexPath)
        
        cell.textLabel?.text = "\(indexPath.section + 1).\(indexPath.row + 1) " + items[indexPath.row]
        
        return cell
    }
     
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return items.count
    }
    
    func numberOfSections(in: UITableView) -> Int {
        return 2
    }
}

