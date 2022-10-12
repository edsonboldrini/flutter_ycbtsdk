import Foundation

class DataResponse : Codable {
    var startTime: Int?
    var heartValue: Int?
    var OOValue: Int?
    var respiratoryRateValue: Int?
    var temperatureValue: Double?
    var DBPValue: Int?
    var SBPValue: Int?
    var sportStartTime: Int?
    var sportEndTime: Int?
    var sportStep: Int?
    var sportDistance: Int?
    var sportCalorie: Int?

    init(startTime: Int? = nil, heartValue: Int? = nil, OOValue: Int? = nil, respiratoryRateValue: Int? = nil, temperatureValue: Double? = nil, DBPValue: Int? = nil, SBPValue: Int? = nil, sportStartTime: Int? = nil, sportEndTime: Int? = nil, sportStep: Int? = nil, sportDistance: Int? = nil, sportCalorie: Int? = nil) {
        self.startTime = startTime
        self.heartValue = heartValue
        self.OOValue = OOValue
        self.respiratoryRateValue = respiratoryRateValue
        self.temperatureValue = temperatureValue
        self.DBPValue = DBPValue
        self.SBPValue = SBPValue
        self.sportStartTime = sportStartTime
        self.sportEndTime = sportEndTime
        self.sportStep = sportStep
        self.sportDistance = sportDistance
        self.sportCalorie = sportCalorie
    }
}
