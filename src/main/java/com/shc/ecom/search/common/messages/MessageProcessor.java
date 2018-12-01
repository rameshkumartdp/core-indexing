package com.shc.ecom.search.common.messages;

import com.shc.ecom.search.common.exception.ErrorCode;
import com.shc.ecom.search.common.exception.SearchCommonException;
import com.shc.ecom.search.indexer.Arguments;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class MessageProcessor implements Serializable {

    public static final String MSG_DELIMITER = "|";
    private static final long serialVersionUID = 1368201550794838540L;

    public boolean isValid(String input) {

        if (StringUtils.isEmpty(input)) {
            return false;
        }

        String[] tokens = input.split("\\" + MSG_DELIMITER);
        MessageType msgType = MessageType.getMessageType(tokens[0]);

        if (!msgType.isLengthValid(tokens.length)) {
            return false;
        }

        if (msgType == MessageType.UNKNOWN) {
            return false;
        }

        return true;
    }

    public ContextMessage getContextMessage(String message) throws SearchCommonException {

        if (!isValid(message)) {
            throw new SearchCommonException(ErrorCode.INVALID_INPUT_MESSAGE, message);
        }

        ContextMessage inboundMsg = new ContextMessage();
        String[] tokens = message.split("\\" + MSG_DELIMITER);
        switch (MessageType.getMessageType(tokens[0])) {
            case REFRESH_SSIN:
                inboundMsg.setMessageId(tokens[0]);
                inboundMsg.setStoreName(tokens[1]);
                inboundMsg.setRunMode(tokens[2]);
                inboundMsg.setCurrentServer(tokens[3]);
                inboundMsg.setTimestamp(tokens[4]);
                break;
            case BUCKET:
                inboundMsg.setMessageId(tokens[0]);
                inboundMsg.setBucketId(tokens[1]);
                inboundMsg.setStoreName(tokens[2]);
                inboundMsg.setRunMode(tokens[3]);
                inboundMsg.setTimestamp(tokens[4]);
                inboundMsg.setCurrentServer(tokens[5]);
                break;
            case SSIN_OFFERID:
            case DRYRUN_OFFERID:
                inboundMsg.setMessageId(tokens[0]);
                inboundMsg.setPid(tokens[1]);
                inboundMsg.setOfferId(tokens[2]);
                inboundMsg.setStoreName(tokens[3]);
                inboundMsg.setTimestamp(tokens[4]);
                inboundMsg.setCurrentServer(tokens[5]);
                inboundMsg.setKey(constructKey(tokens[1], tokens[2]));
                break;
            case SSIN:
            case OFFERID:
            case DRYRUN:
                inboundMsg.setMessageId(tokens[0]);
                inboundMsg.setRunMode(tokens[0]);
                inboundMsg.setPid(tokens[1]);
                inboundMsg.setStoreName(tokens[2]);
                inboundMsg.setTimestamp(tokens[3]);
                inboundMsg.setCurrentServer(tokens[4]);
                break;
            case FULL:
            case GROUPED_NV:
            case INCR:
                inboundMsg.setRunMode(tokens[0]);
                inboundMsg.setMessageId(tokens[0]);
                inboundMsg.setStoreName(tokens[1]);
                inboundMsg.setTimestamp(tokens[2]);
                inboundMsg.setCurrentServer(tokens[3]);
                break;
            case QADD:
                inboundMsg.setRunMode(tokens[0]);
                inboundMsg.setMessageId(tokens[0]);
                inboundMsg.setStoreName(tokens[1]);
                inboundMsg.setTimestamp(tokens[2]);
                inboundMsg.setCurrentServer(tokens[3]);
                break;
            case DOCUMENT:
                inboundMsg.setMessageId(tokens[0]);
                inboundMsg.setJsonDocument(tokens[1]);
                inboundMsg.setStoreName(tokens[2]);
                inboundMsg.setTimestamp(tokens[3]);
                inboundMsg.setCurrentServer(tokens[4]);
                break;
            case UNKNOWN:
                break;
            default:
                break;
        }
        return inboundMsg;
    }

    public ContextMessage getContextMessage(Arguments args) throws SearchCommonException {

        ContextMessage inboundMsg = new ContextMessage();
        inboundMsg.setIndexCommercial(args.getIndexCommercial());

        switch (MessageType.getMessageType(args.getMode())) {
            case BUCKET:
                inboundMsg.setMessageId(args.getMessage());
                inboundMsg.setBucketId(args.getBucketId());
                inboundMsg.setStoreName(args.getStore());
                inboundMsg.setRunMode(args.getMode());
                inboundMsg.setTimestamp(args.getOpsUuid());
                inboundMsg.setCurrentServer(args.getServer());
                break;
            case SSIN_OFFERID:
            case DRYRUN_OFFERID:
                inboundMsg.setMessageId(args.getMode());
                inboundMsg.setPid(args.getSsin());
                inboundMsg.setOfferId(args.getOfferId());
                inboundMsg.setStoreName(args.getStore());
                inboundMsg.setTimestamp(args.getOpsUuid());
                inboundMsg.setCurrentServer(args.getServer());
                inboundMsg.setKey(constructKey(args.getSsin(), args.getOfferId()));
                break;
            case SSIN:
            case OFFERID:
            case DRYRUN:
                inboundMsg.setMessageId(args.getMode());
                inboundMsg.setRunMode(args.getMode());
                inboundMsg.setPid(args.getSsin());
                inboundMsg.setStoreName(args.getStore());
                inboundMsg.setTimestamp(args.getOpsUuid());
                inboundMsg.setCurrentServer(args.getServer());
                break;
            case FULL:
            case GROUPED_NV:
            case INCR:
            case QADD:
            case REFRESH_SSIN:
                inboundMsg.setRunMode(args.getMode());
                inboundMsg.setMessageId(args.getMode());
                inboundMsg.setStoreName(args.getStore());
                inboundMsg.setTimestamp(args.getOpsUuid());
                inboundMsg.setCurrentServer(args.getServer());
                break;
            case DOCUMENT:
                inboundMsg.setMessageId(args.getMode());
                inboundMsg.setJsonDocument(args.getJson());
                inboundMsg.setStoreName(args.getStore());
                inboundMsg.setTimestamp(args.getOpsUuid());
                inboundMsg.setCurrentServer(args.getServer());
                break;
            case UNKNOWN:
                break;
            default:
                break;
        }
        return inboundMsg;
    }

    private String constructKey(String ssin, String offerId) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(ssin);
        if (StringUtils.isNotEmpty(offerId)) {
            keyBuilder.append(" ").append(offerId);
        }
        return keyBuilder.toString();
    }

    public String constructSSINAndOfferMessage(String ssin, String offerId, ContextMessage context) {
        return MessageType.SSIN_OFFERID.getCode() + MSG_DELIMITER + ssin + MSG_DELIMITER + (offerId == null ? StringUtils.EMPTY : offerId) + MSG_DELIMITER + context.getStoreName() + MSG_DELIMITER + context.getTimestamp() + MSG_DELIMITER + context.getCurrentServer();
    }

    public String constructSSINAndOfferMessageForDryRun(String ssin, String offerId, ContextMessage context) {
        return MessageType.DRYRUN_OFFERID.getCode() + MSG_DELIMITER + ssin + MSG_DELIMITER + (offerId == null ? StringUtils.EMPTY : offerId) + MSG_DELIMITER + context.getStoreName() + MSG_DELIMITER + context.getTimestamp() + MSG_DELIMITER + context.getCurrentServer();
    }

    /**
     * Given a ContextMessage (context), a name and value, this method returns a new context message with all the values
     * same as the original context message, but only the given name modified with the given value.
     *
     * @param context
     * @param storeName
     * @return
     */
    public ContextMessage modifyContextMessageStore(ContextMessage context, String storeName) {
        ContextMessage newContext = new ContextMessage();
        newContext.setMessageId(context.getMessageId());
        newContext.setPid(context.getPid());
        newContext.setOfferId(context.getOfferId());
        newContext.setBucketId(context.getBucketId());
        newContext.setStoreName(storeName);
        newContext.setRunMode(context.getRunMode());
        newContext.setTimestamp(context.getTimestamp());
        newContext.setCurrentServer(context.getCurrentServer());
        newContext.setKey(context.getKey());
        return newContext;
    }


    /**
     * Given a contextMessage, this method returns a new context message with all the values same as the original
     * context message, but only the given pid is modified in the context message
     *
     * @param context
     * @param pid
     * @return
     */
    // TODO: change to use reflection
    public ContextMessage modifyContextMessagePid(ContextMessage context, String pid) {
        ContextMessage contextMessage = new ContextMessage();
        contextMessage.setMessageId(context.getMessageId());
        contextMessage.setPid(pid);
        contextMessage.setOfferId(context.getOfferId());
        contextMessage.setBucketId(context.getBucketId());
        contextMessage.setStoreName(context.getStoreName());
        contextMessage.setRunMode(context.getRunMode());
        contextMessage.setTimestamp(context.getTimestamp());
        contextMessage.setCurrentServer(context.getCurrentServer());
        contextMessage.setKey(context.getKey());
        return contextMessage;
    }

}
