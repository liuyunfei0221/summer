-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- lake
use lake;

-- risk
use risk;

-- analyze
use analyze;



-- auth
use auth;

db.refreshInfo.createIndex({"id":1});
db.refreshInfo.createIndex({"memberId":1,"credentialType":1,"deviceType":1});
db.refreshInfo.createIndex({"expireAt":1},{expireAfterSeconds:0});

db.credentialHistory.createIndex({"memberId":1});
db.credentialHistory.createIndex({"credential":1});




-- base
use base;

db.country.createIndex({"id":1});

db.state.createIndex({"id":1});
db.state.createIndex({"countryId":1});

db.city.createIndex({"id":1});
db.city.createIndex({"stateId":1});

db.area.createIndex({"id":1});
db.area.createIndex({"cityId":1});


-- member
use member;

db.card.createIndex({"id":1});
db.card.createIndex({"memberId":1});
db.card.createIndex({"createTime":-1});

db.address.createIndex({"id":1});
db.address.createIndex({"memberId":1});
db.address.createIndex({"createTime":-1});


-- media
use media;

db.attachment.createIndex({"id":1});
db.attachment.createIndex({"createTime":-1});
db.attachment.createIndex({"creator":1});

db.downloadHistory.createIndex({"id":1});
db.downloadHistory.createIndex({"attachmentId":1});
db.downloadHistory.createIndex({"createTime":-1});
db.downloadHistory.createIndex({"creator":1});

db.getCollection("qrCodeConfig").insert( {
    _id: NumberLong("41122926732247041"),
    name: "member info",
    description: "member info",
    type: NumberInt("1"),
    genHandlerType: NumberInt("1"),
    domain: "localhost:11000",
    pathToBeFilled: "blue-member/basic/%s",
    placeholderCount: NumberInt("1"),
    allowedRoles: [
        NumberLong("1"),
        NumberLong("2"),
        NumberLong("3"),
        NumberLong("4"),
        NumberLong("5"),
        NumberLong("6")
    ],
    status: NumberInt("1"),
    createTime: NumberLong("1657089007"),
    updateTime: NumberLong("1657089007"),
    creator: NumberLong("1"),
    updater: NumberLong("1"),
    _class: "com.blue.media.repository.entity.QrCodeConfig"
} );
db.qrCodeConfig.createIndex({"type":1});
db.qrCodeConfig.createIndex({"createTime":-1});


-- verify
use verify;

db.verifyHistory.createIndex({"id":1});
db.verifyHistory.createIndex({"verifyType":1});
db.verifyHistory.createIndex({"businessType":1});
db.verifyHistory.createIndex({"destination":1});
db.verifyHistory.createIndex({"requestIp":1});
db.verifyHistory.createIndex({"createTime":-1});