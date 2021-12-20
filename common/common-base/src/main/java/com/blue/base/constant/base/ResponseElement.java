package com.blue.base.constant.base;

import org.springframework.http.HttpStatus;

/**
 * global exception infos
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public enum ResponseElement {

    //common

    /**
     * success
     */
    OK(HttpStatus.OK.value(), HttpStatus.OK.value(), "Success"),

    /**
     * too many requests
     */
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS.value(), HttpStatus.TOO_MANY_REQUESTS.value(), "Too many requests"),

    /**
     * fallback
     */
    PARTIAL_CONTENT(HttpStatus.PARTIAL_CONTENT.value(), HttpStatus.PARTIAL_CONTENT.value(), "Fallback"),

    /**
     * authentication failed or expired
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.value(), "Authentication failed or expired, please login"),

    /**
     * insufficient permissions
     */
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.value(), "Insufficient permissions"),

    /**
     * resource not found
     */
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.value(), "Resource not found"),

    /**
     * Illegal parameter
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.value(), "Illegal parameter"),

    /**
     * invalid media type
     */
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "Invalid media type"),

    /**
     * request body too large or request header too larger
     */
    PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE.value(), HttpStatus.PAYLOAD_TOO_LARGE.value(), "Request body too large or request header too larger"),

    /**
     * no content
     */
    NO_CONTENT(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.value(), "No content"),

    /**
     * you have been restricted access/ reject access by risk control
     */
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE.value(), HttpStatus.NOT_ACCEPTABLE.value(), "You have been restricted access"),

    /**
     * system error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), "System busy"),

    /**
     * timeout
     */
    REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT.value(), HttpStatus.REQUEST_TIMEOUT.value(), "Timeout"),

    /**
     * gateway timeout
     */
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT.value(), HttpStatus.GATEWAY_TIMEOUT.value(), "Gateway timeout"),

    //business

    /**
     * EMPTY_PARAM
     */
    EMPTY_PARAM(HttpStatus.BAD_REQUEST.value(), 400000001, "Empty param"),

    /**
     * EMPTY_PATH_VARIABLE
     */
    EMPTY_PATH_VARIABLE(HttpStatus.BAD_REQUEST.value(), 400000002, "Empty path variable"),

    /**
     * INVALID_PATH_VARIABLE
     */
    INVALID_PATH_VARIABLE(HttpStatus.BAD_REQUEST.value(), 400000003, "Invalid path variable"),

    /**
     * TOO_MANY_HEADERS
     */
    TOO_MANY_HEADERS(HttpStatus.BAD_REQUEST.value(), 400000004, "Too many headers"),

    /**
     * TOO_LARGE_HEADER
     */
    TOO_LARGE_HEADER(HttpStatus.BAD_REQUEST.value(), 400000005, "Too large headers"),

    /**
     * TOO_LARGE_BODY
     */
    TOO_LARGE_BODY(HttpStatus.BAD_REQUEST.value(), 400000006, "Too large body"),

    /**
     * TOO_LARGE_URI
     */
    TOO_LARGE_URI(HttpStatus.BAD_REQUEST.value(), 400000007, "Uri is too long"),

    /**
     * INVALID_METADATA_PARAM
     */
    INVALID_METADATA_PARAM(HttpStatus.BAD_REQUEST.value(), 400000008, "metadata can't be null"),

    /**
     * RSA_FAILED
     */
    RSA_FAILED(HttpStatus.BAD_REQUEST.value(), 400000009, "Encryption/decryption or signature/verification failed"),

    /**
     * INVALID_REQUEST_METHOD
     */
    INVALID_REQUEST_METHOD(HttpStatus.BAD_REQUEST.value(), 400000010, "Invalid request method"),

    /**
     * INVALID_IDENTITY
     */
    INVALID_IDENTITY(HttpStatus.BAD_REQUEST.value(), 400000011, "Invalid or empty identity"),

    /**
     * DATA_NOT_EXIST
     */
    DATA_NOT_EXIST(HttpStatus.BAD_REQUEST.value(), 400000012, "Data not exist"),

    /**
     * DATA_ALREADY_EXIST
     */
    DATA_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), 400000013, "Data already exist"),

    //user

    /**
     * INVALID_ACCT_OR_PWD
     */
    INVALID_ACCT_OR_PWD(HttpStatus.BAD_REQUEST.value(), 400100001, "Invalid account or password"),

    /**
     * NO_AUTH_REQUIRED_RES
     */
    NO_AUTH_REQUIRED_RESOURCE(HttpStatus.BAD_REQUEST.value(), 400100002, "Resources do not require authentication access"),

    /**
     * FILE_NOT_EXIST
     */
    FILE_NOT_EXIST(HttpStatus.BAD_REQUEST.value(), 400100003, "File not exist"),

    /**
     * MEMBER_NOT_HAS_A_ROLE
     */
    MEMBER_NOT_HAS_A_ROLE(HttpStatus.BAD_REQUEST.value(), 400100004, "Member not has a role"),

    /**
     * MEMBER_ALREADY_HAS_A_ROLE
     */
    MEMBER_ALREADY_HAS_A_ROLE(HttpStatus.BAD_REQUEST.value(), 400100005, "Member already has a role"),

    /**
     * ACCOUNT_HAS_BEEN_FROZEN
     */
    ACCOUNT_HAS_BEEN_FROZEN(HttpStatus.BAD_REQUEST.value(), 400100006, "Your account has been frozen");


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