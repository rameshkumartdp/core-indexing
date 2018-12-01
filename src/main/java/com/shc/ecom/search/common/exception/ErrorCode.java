/**
 *
 */
package com.shc.ecom.search.common.exception;

/**
 * @author rgopala Jul 15, 2015 ssin-producer
 */
public enum ErrorCode {

    INVALID_INPUT_MESSAGE(100),
    GB_OFFER_BUCKET_EMPTY(201),
    NO_RESPONSE_FROM_SERVICE(301),
    FAILED_JSON_TO_OBJECT_MAPPING(401),
    PROPERTY_NOT_FOUND(501),
    PRICING_ERROR(301),
    PRICECLIENT_NULL(302),
    GB_OFFER_UNAVAILABLE(202),
    VARATTR_RESPONSE_UNAVAILABLE(203),
    RANK_SEARS_OFFER_UNAVAILABLE(501),
    FILE_OPEN_FAILURE(601),
    UNKNOWN_CLASSIFICATION(901);

    private int code;

    ErrorCode(int code) {
        this.setCode(code);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
