//
//  ViewController.swift
//  SwiftDemo
//
//  Created by macos on 2021/11/16.
//

import UIKit
import YCProductSDK


class ViewController: UIViewController {
    

    /// 设置选项
    private lazy var items = [
        "Query",
        "Set",
        "App and device control",
        "Health data",
        "ECG",
        "Data collection",
        "Watch face",
        "Disconnect"
    ]
    
    /// 功能演示
    @IBOutlet weak var listView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    

        navigationItem.title = "SDK Demo"
        navigationItem.rightBarButtonItem = UIBarButtonItem(barButtonSystemItem: UIBarButtonItem.SystemItem.search, target: self, action: #selector(searchDevice))
        
        listView.register(UITableViewCell.self, forCellReuseIdentifier: String(describing: UITableView.self))
        
        // 初始化
        YCProduct.setLogLevel(.normal)
        _ = YCProduct.shared
         
        // 增加通知
        NotificationCenter.default.addObserver(
            self, selector: #selector(deviceStateChange(_:)), name: YCProduct.deviceStateNotification, object: nil
        )
        
    }
    
    /// 连接状态的变化
    @objc private func deviceStateChange(_ ntf: Notification) {
        
        guard let info = ntf.userInfo as? [String: Any],
              let state = info[YCProduct.connecteStateKey] as? YCProductState else {
            return
        }
        
        print("=== 状态变化 \(state.rawValue)")
        
    }
    
    @objc private func searchDevice() {
        
        if YCProduct.shared.currentPeripheral != nil {
            return
        }
        
        let vc = YCSearchDeviceViewController()
        navigationController?.pushViewController(vc, animated: true)
    }
    
    
    
        
}

extension ViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        tableView.deselectRow(at: indexPath, animated: true)
        
        switch indexPath.row {
        case 0:
            navigationController?.pushViewController(
                YCQueryViewController(),
                animated: true
            )
            
        case 1:
            navigationController?.pushViewController(
                YCSettingViewController(),
                animated: true
            )
            
        case 2:
            navigationController?.pushViewController(
                YCAppControlViewController(),
                animated: true
            )
            
        case 3:
            navigationController?.pushViewController(
                YCHealthDataViewController(),
                animated: true
            )
             
        case 4:
            navigationController?.pushViewController(
                YCECGMeasureViewController(),
                animated: true
            )
            
        case 5: // 采集数据
            navigationController?.pushViewController(
                YCCollectDataViewController(),
                animated: true
            )
            
        case 6:
            navigationController?.pushViewController(
                YCWatchFaceViewController(),
                animated: true
            )
            
        default:
            
            // 断开连接
            YCProduct.disconnectDevice()
            
            break
        }
    }
    
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return items.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: String(describing: UITableView.self), for: indexPath)
        
        cell.textLabel?.text = items[indexPath.row]
        
        return cell
    }
}


