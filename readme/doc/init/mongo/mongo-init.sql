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

db.refreshInfo.createIndex({"id":1},{unique: true});
db.refreshInfo.createIndex({"memberId":1,"credentialType":1,"deviceType":1},{unique: true});
db.refreshInfo.createIndex({"expireAt":1},{expireAfterSeconds:0});




-- base
use base;

db.country.createIndex({"id":1},{unique: true});

db.state.createIndex({"id":1},{unique: true});
db.state.createIndex({"countryId":1});

db.city.createIndex({"id":1},{unique: true});
db.city.createIndex({"stateId":1});

db.area.createIndex({"id":1},{unique: true});
db.area.createIndex({"cityId":1});



-- media
use media;

db.downloadHistory.createIndex({"id":1},{unique: true});
db.downloadHistory.createIndex({"attachmentId":1});
db.downloadHistory.createIndex({"createTime":-1});
db.downloadHistory.createIndex({"creator":1});
