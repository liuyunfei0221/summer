-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- lake
use lake;

-- risk
use risk;

-- analyze
use analyze;



-- base
use base;

db.country.createIndex({"id":1});

db.state.createIndex({"id":1});
db.state.createIndex({"countryId":1});

db.city.createIndex({"id":1});
db.city.createIndex({"stateId":1});

db.area.createIndex({"id":1});
db.area.createIndex({"cityId":1});



-- auth
use auth;
db.createCollection('refreshInfo');

db.refreshInfo.createIndex({"id":1});
db.refreshInfo.createIndex({"memberId":1});
db.refreshInfo.createIndex({"memberId":1,"credentialType":1,"deviceType":1},{unique:true});
db.refreshInfo.createIndex({"expireAt":1},{expireAfterSeconds:0});

db.credentialHistory.createIndex({"memberId":1});
db.credentialHistory.createIndex({"credential":1});



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



-- verify
use verify;

db.verifyHistory.createIndex({"id":1});
db.verifyHistory.createIndex({"verifyType":1});
db.verifyHistory.createIndex({"businessType":1});
db.verifyHistory.createIndex({"destination":1});
db.verifyHistory.createIndex({"requestIp":1});
db.verifyHistory.createIndex({"createTime":-1});

db.verifyTemplate.createIndex({"type":1});
db.verifyTemplate.createIndex({"businessType":1});
db.verifyTemplate.createIndex({"language":1});
db.verifyTemplate.createIndex({"type":1,"businessType":1,"language":1},{unique:true});



-- agreement
use agreement;

db.agreementRecord.createIndex({"memberId":1,"agreementId":1},{unique:true});



-- marketing
use marketing;

db.eventRecord.createIndex({"memberId":1});
db.eventRecord.createIndex({"type":1});





-- media
use risk;

db.eventRecord.createIndex({"type":1});






-- temp1

use auth;
db.createCollection('refreshInfo');
db.createCollection('credentialHistory');

use member;
db.createCollection('card');
db.createCollection('address');

use media;
db.createCollection('attachment');
db.createCollection('downloadHistory');

use verify;
db.createCollection('verifyHistory');
db.createCollection('verifyTemplate');

use agreement;
db.createCollection('agreementRecord');



-- temp2

use auth;
db.refreshInfo.createIndex({"id":1});
db.refreshInfo.createIndex({"memberId":1});
db.refreshInfo.createIndex({"memberId":1,"credentialType":1,"deviceType":1},{unique:true});
db.refreshInfo.createIndex({"expireAt":1},{expireAfterSeconds:0});
db.credentialHistory.createIndex({"memberId":1});
db.credentialHistory.createIndex({"credential":1});


use member;
db.card.createIndex({"id":1});
db.card.createIndex({"memberId":1});
db.card.createIndex({"createTime":-1});
db.address.createIndex({"id":1});
db.address.createIndex({"memberId":1});
db.address.createIndex({"createTime":-1});


use media;
db.attachment.createIndex({"id":1});
db.attachment.createIndex({"createTime":-1});
db.attachment.createIndex({"creator":1});
db.downloadHistory.createIndex({"id":1});
db.downloadHistory.createIndex({"attachmentId":1});
db.downloadHistory.createIndex({"createTime":-1});
db.downloadHistory.createIndex({"creator":1});


use verify;
db.verifyHistory.createIndex({"id":1});
db.verifyHistory.createIndex({"verifyType":1});
db.verifyHistory.createIndex({"businessType":1});
db.verifyHistory.createIndex({"destination":1});
db.verifyHistory.createIndex({"requestIp":1});
db.verifyHistory.createIndex({"createTime":-1});
db.verifyTemplate.createIndex({"type":1});
db.verifyTemplate.createIndex({"businessType":1});
db.verifyTemplate.createIndex({"language":1});
db.verifyTemplate.createIndex({"type":1,"businessType":1,"language":1},{unique:true});


use agreement;
db.agreementRecord.createIndex({"memberId":1,"agreementId":1},{unique:true});


use marketing;
db.eventRecord.createIndex({"memberId":1});
db.eventRecord.createIndex({"type":1});