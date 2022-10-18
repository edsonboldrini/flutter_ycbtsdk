import Foundation

class DataResponse : Codable {
    var startTime: Int?
    var heartValue: Int?
    var OOValue: Int?
    var respiratoryRateValue: Int?
    var temperatureValue: Double?
    var SBPValue: Int?
    var DBPValue: Int?
    var sportStartTime: Int?
    var sportEndTime: Int?
    var sportStep: Int?
    var sportDistance: Int?
    var sportCalorie: Int?

    init(startTime: Int? = nil, heartValue: Int? = nil, OOValue: Int? = nil, respiratoryRateValue: Int? = nil, temperatureValue: Double? = nil, SBPValue: Int? = nil, DBPValue: Int? = nil, sportStartTime: Int? = nil, sportEndTime: Int? = nil, sportStep: Int? = nil, sportDistance: Int? = nil, sportCalorie: Int? = nil) {
        self.startTime = startTime
        self.heartValue = heartValue
        self.OOValue = OOValue
        self.respiratoryRateValue = respiratoryRateValue
        self.temperatureValue = temperatureValue
        self.SBPValue = SBPValue
        self.DBPValue = DBPValue
        self.sportStartTime = sportStartTime
        self.sportEndTime = sportEndTime
        self.sportStep = sportStep
        self.sportDistance = sportDistance
        self.sportCalorie = sportCalorie
    }
}
