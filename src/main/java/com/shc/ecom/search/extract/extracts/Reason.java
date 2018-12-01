package com.shc.ecom.search.extract.extracts;

public enum Reason {

    MARKED_FOR_DELETE(""),
    NOT_DISPLAY_ELIGIBLE(""),
    HIERARCHY_NOT_EXIST(""),
    IDENTITY_NOT_EXIST(""),
    OFFER_COUNT_FAIL("Offer Count failed"),
    OFFER_SITE_NOT_EXIST(""),
    OFFER_OPERATION_SITE_NOT_EXIST(""),
    OFFER_NOT_DISPLAY_ELIGIBLE(""),
    SSIN_NOT_EXIST(""),
    UID_NOT_EXIST(""),
    OFFER_TYPE_NOT_EXIST(""),
    SEARCH_SUPRESSION(""),
    SYW_EXCLUSIVE(""), PRICE_NULL(""), PRICE_ZERO_SALE_EMPTY(""), VARATTR_DOC_NULL("");

    private String msg;

    private Reason(String msg) {
        this.setMsg(msg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
