import Foundation

class DataResponse : Codable {
    var startTimestemp: Int
    var heartValue: Int
    var OOValue: Int
    var respiratoryRateValue: Int
    var temperatureValue: Double
    var DBPValue: Int
    var SBPValue: Int

    init(startTimestamp: Int, heartValue: Int, OOValue: Int, respiratoryRateValue: Int, temperatureValue: Double, DBPValue: Int, SBPValue: Int) {
        self.startTimestemp = startTimestamp
        self.heartValue = heartValue
        self.OOValue = OOValue
        self.respiratoryRateValue = respiratoryRateValue
        self.temperatureValue = temperatureValue
        self.DBPValue = DBPValue
        self.SBPValue = SBPValue
    }
}
