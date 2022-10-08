import Foundation

class ScanBLEResponse : Codable {
    var Name:String
    var Mac:String
    var Rssi:Int

    init(name: String, mac: String, rssi: Int) {
        self.Name = name
        self.Mac = mac
        self.rssi = rssi
    }
}
