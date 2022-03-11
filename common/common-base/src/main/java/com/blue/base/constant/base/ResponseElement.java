package com.blue.base.constant.base;

import org.springframework.http.HttpStatus;

/**
 * global exception infos
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public enum ResponseElement {


    //<editor-fold desc="base">
    OK(HttpStatus.OK.value(), HttpStatus.OK.value(), "Success"),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS.value(), HttpStatus.TOO_MANY_REQUESTS.value(), "Too many requests"),
    PARTIAL_CONTENT(HttpStatus.PARTIAL_CONTENT.value(), HttpStatus.PARTIAL_CONTENT.value(), "Fallback"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.value(), "Authentication failed or expired, please login"),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.value(), "Insufficient permissions"),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.value(), "Resource not found"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.value(), "Illegal parameter"),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "Invalid media type"),
    PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE.value(), HttpStatus.PAYLOAD_TOO_LARGE.value(), "Request body too large or request header too larger"),
    NO_CONTENT(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.value(), "No content"),
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE.value(), HttpStatus.NOT_ACCEPTABLE.value(), "You have been restricted access"),
    INTERNAL_SERVER_ERROR(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), "System busy"),
    REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT.value(), HttpStatus.REQUEST_TIMEOUT.value(), "Timeout"),
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT.value(), HttpStatus.GATEWAY_TIMEOUT.value(), "Gateway timeout"),
    EMPTY_PARAM(HttpStatus.BAD_REQUEST.value(), 400000001, "Empty param"),
    INVALID_PARAM(HttpStatus.BAD_REQUEST.value(), 400000002, "Invalid param"),
    EMPTY_PATH_VARIABLE(HttpStatus.BAD_REQUEST.value(), 400000003, "Empty path variable"),
    INVALID_PATH_VARIABLE(HttpStatus.BAD_REQUEST.value(), 400000004, "Invalid path variable"),
    TOO_MANY_HEADERS(HttpStatus.BAD_REQUEST.value(), 400000005, "Too many headers"),
    TOO_LARGE_HEADER(HttpStatus.BAD_REQUEST.value(), 400000006, "Too large headers"),
    TOO_LARGE_BODY(HttpStatus.BAD_REQUEST.value(), 400000007, "Too large body"),
    TOO_LARGE_URI(HttpStatus.BAD_REQUEST.value(), 400000008, "Uri is too long"),
    INVALID_METADATA_PARAM(HttpStatus.BAD_REQUEST.value(), 400000009, "metadata can't be null"),
    DECRYPTION_FAILED(HttpStatus.BAD_REQUEST.value(), 400000010, "Encryption/decryption or signature/verification failed"),
    INVALID_REQUEST_METHOD(HttpStatus.BAD_REQUEST.value(), 400000011, "Invalid request method"),
    INVALID_IDENTITY(HttpStatus.BAD_REQUEST.value(), 400000012, "Invalid or empty identity"),
    DATA_NOT_EXIST(HttpStatus.BAD_REQUEST.value(), 400000013, "Data not exist"),
    DATA_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), 400000014, "Data already exist"),
    DATA_HAS_NOT_CHANGED(HttpStatus.BAD_REQUEST.value(), 400000015, "Data has not changed"),
    VERIFY_IS_INVALID(HttpStatus.BAD_REQUEST.value(), 400000016, "Verify is invalid"),
    //</editor-fold>


    //<editor-fold desc="member">
    MEMBER_NOT_HAS_A_ROLE(HttpStatus.BAD_REQUEST.value(), 400100001, "Member not has a role"),
    MEMBER_ALREADY_HAS_A_ROLE(HttpStatus.BAD_REQUEST.value(), 400100002, "Member already has a role"),
    ACCOUNT_HAS_BEEN_FROZEN(HttpStatus.BAD_REQUEST.value(), 400100003, "Your account has been frozen"),
    PHONE_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), 400100004, "The phone number already exists"),
    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), 400100005, "The email already exists"),
    NAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), 400100006, "The name already exists"),
    //</editor-fold>


    //<editor-fold desc="auth">
    INVALID_ACCT_OR_PWD(HttpStatus.BAD_REQUEST.value(), 400200001, "Invalid account or password"),
    NO_AUTH_REQUIRED_RESOURCE(HttpStatus.BAD_REQUEST.value(), 400200002, "Resources do not require authentication access"),
    ROLE_NAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), 400200003, "Role name already exists"),
    ROLE_LEVEL_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), 400200004, "Role level already exists"),
    RESOURCE_NAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), 400200005, "Resource name already exists"),
    RESOURCE_FEATURE_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), 400200006, "Resource feature already exists"),
    //</editor-fold>


    //<editor-fold desc="marketing">
    REPEAT_SIGN_IN(HttpStatus.BAD_REQUEST.value(), 400800001, "Please don't sign in again"),
    //</editor-fold>


    //<editor-fold desc="gateways">
    UNKNOWN_IP(HttpStatus.NOT_ACCEPTABLE.value(), 401900001, "Unknown ip"),
    ILLEGAL_REQUEST(HttpStatus.NOT_ACCEPTABLE.value(), 401900002, "Illegal request"),
    FILE_NOT_EXIST(HttpStatus.BAD_REQUEST.value(), 401900003, "File not exist"),
    FILE_INVALID(HttpStatus.BAD_REQUEST.value(), 401900004, "File invalid, cause: %s"),
    INVALID_EMAIL_ADDRESS(HttpStatus.BAD_REQUEST.value(), 401900005, "Cause: %s");
    //</editor-fold>

    /**
     * http status
     */
    public final int status;

    /**
     * business code
     */
    public final int code;

    /**
     * message
     */
    public final String message;

    ResponseElement(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}