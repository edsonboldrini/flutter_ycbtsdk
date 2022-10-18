import Foundation

class ScanBLEResponse : Codable {
    var name: String
    var mac: String
    var rssi: Int

    init(name: String, mac: String, rssi: Int) {
        self.name = name
        self.mac = mac
        self.rssi = rssi
    }
}
