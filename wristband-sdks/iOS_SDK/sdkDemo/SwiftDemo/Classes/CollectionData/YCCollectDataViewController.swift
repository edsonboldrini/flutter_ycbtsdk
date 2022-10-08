//
//  YCCollectDataViewController.swift
//  SwiftDemo
//
//  Created by macos on 2021/11/30.
//

import UIKit
import YCProductSDK

class YCCollectDataViewController: UIViewController {
    
    private lazy var items = [
        "Query basic info",
        "Receive data",
        "Delete"
    ]

    @IBOutlet weak var listView: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()

        navigationItem.title = "Collection Data"
        
        listView.register(UITableViewCell.self, forCellReuseIdentifier: String(describing: UITableViewCell.self))
    }
 

}

extension YCCollectDataViewController: UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        tableView.deselectRow(at: indexPath, animated: true)
        
        switch indexPath.row {
        case 0:
            YCProduct.queryCollectDataBasicinfo(dataType: .ecg) { state, response in

                guard state == .succeed, let datas = response as? [YCCollectDataBasicInfo] else {
                    return
                }
 
                print(datas)
            }
            
        case 1:
            YCProduct.queryCollectDataInfo( dataType: .ecg, index: 0, uploadEnable: true) { state, response in
                
                if state == .succeed,
                let info = response as? YCCollectDataInfo {
                    
                    print(info.data, info.progress)
                }
                
            }
            
        case 2:
            YCProduct.deleteCollectData(dataType: .ecg, index: 0) { state, response in
                
            }
            
        default:
            break
        }
    }
}

extension YCCollectDataViewController: UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return items.count;
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: String(describing: UITableViewCell.self), for: indexPath)
        
        cell.textLabel?.text = items[indexPath.row]
        
        return cell
    }
}

