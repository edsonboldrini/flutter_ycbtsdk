//
//  YCWatchFaceViewController.swift
//  SwiftDemo
//
//  Created by macos on 2021/11/30.
//

import UIKit
import YCProductSDK

class YCWatchFaceViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        navigationItem.title = "Watch face"
        
        // 监听表盘切换
        NotificationCenter.default.addObserver(self, selector: #selector(watchFaceChanged(_:)), name: YCProduct.deviceControlNotification, object: nil)
          
        let dialID: UInt32 =  2147483539
        
//        YCProduct.changeWatchFace(dialID: dialID) { state, _ in
//            
//            if state == .succeed {
//                
//                print("change success")
//            }
//        }
        
//        YCProduct.deleteWatchFace(dialID: dialID) { state, _ in
//
//            if state == .succeed {
//
//                print("delete success")
//            }
//        }
    }
    
     
    /// 查询表盘信息
    private func queryDeviceFaceInfo() {
        
        YCProduct.queryWatchFaceInfo { state, response in
            
            if state == YCProductState.succeed,
            let info = response as? YCWatchFaceBreakCountInfo {
                
//                if info.localCount > 0 {
                
                    for item in info.dials {
                        
                        print(item.dialID, item.blockCount,
                              item.isCustomDial,
                              item.isShowing
                        )
//                    }
                    
                }
            }
        }
    }
    
    
    /// 表盘变更
    /// - Parameter ntf:
    @objc private func watchFaceChanged(_ ntf: Notification) {
        
        guard let info = ntf.userInfo,
              let dialID = ((info[YCDeviceControlType.switchWatchFace.string]) as? YCReceivedDeviceReportInfo)?.data as? UInt32  else {
                  return
              }
        
        print("=== dialID = \(dialID)")
        
    }
    
}

// MARK: - 下载自定义表盘
extension YCWatchFaceViewController {
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        
        queryDeviceFaceInfo()
//        downloadCustomDial()
    }
    
    /// 下载
    private func downloadCustomDial() {
        
        // 1. 加载资源
        guard let path = Bundle.main.path(forResource: "customE80.bin", ofType: nil),
        let dialData = NSData(contentsOfFile: path) else {
            
            return
        }
        
       
        
        let customDialData = YCProduct.generateCustomDialData(dialData as Data, backgroundImage: UIImage(named: "test"), thumbnail: UIImage(named: "test"), timePosition: CGPoint(x: 120, y: 120), redColor: 255, greenColor: 0, blueColor: 0) as NSData
        
        
        let dialID: UInt32 = 2147483539
        
        // 删除表盘
        YCProduct.deleteWatchFace(dialID: dialID) { state, response in
            
            // 直接下载
            YCProduct.downloadWatchFace(isEnable: true, data: customDialData, dialID: dialID, blockCount: 0, dialVersion: 1) { state, response in
                
                if state == .succeed,
                    let info = response as? YCWatchFaceDownloadProgressInfo {
                    
                    print(info.downloaded, info.progress)
                    
                } else {
                    
                }
                
                // 进度，是否完成 ， 已下载字数, 成功，失败
            }
        }
        
    }
}
