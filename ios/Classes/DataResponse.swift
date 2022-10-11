import Foundation

class DataResponse : Codable {
    var startTime: Int?
    var heartValue: Int?
    var OOValue: Int?
    var respiratoryRateValue: Int?
    var temperatureValue: Double?
    var DBPValue: Int?
    var SBPValue: Int?

    init(startTime: Int? = nil, heartValue: Int? = nil, OOValue: Int? = nil, respiratoryRateValue: Int? = nil, temperatureValue: Double? = nil, DBPValue: Int? = nil, SBPValue: Int? = nil) {
        self.startTime = startTime
        self.heartValue = heartValue
        self.OOValue = OOValue
        self.respiratoryRateValue = respiratoryRateValue
        self.temperatureValue = temperatureValue
        self.DBPValue = DBPValue
        self.SBPValue = SBPValue
    }
}
