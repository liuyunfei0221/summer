package com.blue.base.constant.base;

/**
 * Common response messages
 *
 * @author liuyunfei
 * @date 2021/9/22
 * @apiNote
 */
public enum ResponseMessage {

    /**
     * GENERIC_SUCCESS
     */
    GENERIC_SUCCESS("Success"),

    /**
     * GENERIC_FAILED
     */
    GENERIC_FAILED("Failed"),

    /**
     * EMPTY_REQUEST_BODY
     */
    EMPTY_REQUEST_BODY("Empty request body"),

    /**
     * TOO_MANY_HEADERS
     */
    TOO_MANY_HEADERS("Too many headers"),

    /**
     * TOO_LARGE_HEADER
     */
    TOO_LARGE_HEADER("Too large headers"),

    /**
     * TOO_LARGE_BODY
     */
    TOO_LARGE_BODY("Too large body"),

    /**
     * TOO_LARGE_URI
     */
    TOO_LARGE_URI("Uri is too long"),

    /**
     * RSA_FAILED
     */
    RSA_FAILED("Encryption/decryption or signature/verification failed"),

    /**
     * INVALID_REQUEST_METHOD
     */
    INVALID_REQUEST_METHOD("Invalid request method"),

    /**
     * INVALID_IDENTITY
     */
    INVALID_IDENTITY("Invalid or empty identity"),

    /**
     * INVALID_CONSTANT_IDENTITY
     */
    INVALID_CONSTANT_IDENTITY("Invalid or empty identity element"),

    /**
     * INVALID_ACCT_OR_PWD
     */
    INVALID_ACCT_OR_PWD("Invalid account or password"),

    /**
     * NO_AUTH_REQUIRED_RES
     */
    NO_AUTH_REQUIRED_RESOURCE("Resources do not require authentication access"),

    /**
     * ACCESS
     */
    ACCESS("Access"),

    /**
     * FILE_NOT_EXIST
     */
    FILE_NOT_EXIST("File not exist"),

    /**
     * MEMBER_NOT_HAS_A_ROLE
     */
    MEMBER_NOT_HAS_A_ROLE("Member not has a role"),

    /**
     * MEMBER_ALREADY_HAS_A_ROLE
     */
    MEMBER_ALREADY_HAS_A_ROLE("Member already has a role"),

    /**
     * ACCOUNT_HAS_BEEN_FROZEN
     */
    ACCOUNT_HAS_BEEN_FROZEN("Your account has been frozen");

    public final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

}
