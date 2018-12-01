package com.shc.ecom.search.common.messages;


import org.apache.commons.lang3.StringUtils;

public enum MessageType {

    // START-SSIN-REFRESH|SEARS|FULL|0|1460527490
    REFRESH_SSIN("START-SSIN-REFRESH", 5),
    // BUCKET|4093|SEARS|FULL|1462996878|0
    BUCKET("BUCKET", 6),
    // SSIN|05053993000P|SEARS|1462996878|0
    SSIN("SSIN", 5),
    // DRYRUN|05053993000P|SEARS|1462996878|0
    DRYRUN("DRYRUN", 5),
    // DRYRUN-OFFERID|05053993000P|05053993000|SEARS|1462996878|0
    DRYRUN_OFFERID("DRYRUN-OFFERID", 6),
    // OFFERID|05053993000|SEARS|1462996878|0
    OFFERID("OFFERID", 5),
    // SSIN-OFFERID|05053993000P|05053993000|SEARS|1462996878|0
    SSIN_OFFERID("SSIN-OFFERID", 6),
    // QADD|SEARS|1462996878|0
    QADD("QADD", 4),
    // FULL|SEARS|1462996878|0
    FULL("FULL", 4),
    // INCR|FBM|1462996878|0
    INCR("INCR", 4),
    // GROUPED_NV|FBM|1462996878|0
    GROUPED_NV("GROUPED_NV", 4),
    // DOC|{"catentryId":"44836956","image"...}|SEARS|1462996878|0
    DOCUMENT("DOC", 5),
    UNKNOWN("", 1);

    private String code;

    private int msgLength;

    MessageType(String code, int length) {
        this.setCode(code);
        this.setMsgLength(length);
    }

    public static MessageType getMessageType(String code) {
        for (MessageType messageType : MessageType.values()) {
            if (code.equalsIgnoreCase(messageType.getCode())) {
                return messageType;
            }
        }
        return null;
    }

    public static boolean isMessageTypeValid(String messageType) {
        for (MessageType type : MessageType.values()) {
            if (StringUtils.equalsIgnoreCase(messageType, type.getCode())) {
                return true;
            }
        }
        return false;
    }

    public boolean isLengthValid(int len) {
        return msgLength == len;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }
}
