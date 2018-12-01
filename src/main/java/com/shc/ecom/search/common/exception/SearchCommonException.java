package com.shc.ecom.search.common.exception;


public class SearchCommonException extends Exception {

    private static final long serialVersionUID = 1419100558165491796L;

    public SearchCommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchCommonException(String message) {
        super(message);
    }

    public SearchCommonException(Throwable cause) {
        super(cause);
    }

    public SearchCommonException() {
        super();
    }

    // TODO: Provide more meaningful descriptions for the error code and call that instead of toString().
    public SearchCommonException(ErrorCode errorCode, String message) {
        super(errorCode.toString() + ": " + message);
    }

}
