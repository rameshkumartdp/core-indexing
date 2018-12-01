package com.shc.ecom.search.ssinproducer;

import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.exception.SearchCommonException;
import com.shc.ecom.search.common.greenbox.GBServiceFacade;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.messages.MessageProcessor;
import com.shc.ecom.search.common.messages.MessageType;
import com.shc.ecom.search.persistence.Incremental;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
@Scope("prototype")
public class BucketWorkerThread implements Runnable {

    public static final int TIME_DELTA_IN_HOURS = 4;
    private static final Logger LOGGER = LoggerFactory.getLogger(BucketWorkerThread.class);
    private static final Logger DEBUG_LOGGER = LoggerFactory.getLogger("debuglog");
    private BlockingQueue<String> translateQueue;
    private BlockingQueue<Integer> bucketQueue;
    private ContextMessage contextMessage;
    @Autowired
    private MessageProcessor messageProcessor;

    @Autowired
    private GBServiceFacade gbServiceFacade;

    @Autowired
    private Incremental incremental;

    public BucketWorkerThread(BlockingQueue<String> translateQueue, BlockingQueue<Integer> bucketQueue, ContextMessage contextMessage) {
        this.translateQueue = translateQueue;
        this.bucketQueue = bucketQueue;
        this.contextMessage = contextMessage;
    }

    public BucketWorkerThread(BlockingQueue<String> translateQueue, ContextMessage contextMessage, int start, int end) {
        this.translateQueue = translateQueue;
        this.contextMessage = contextMessage;
        if (start > end) {
            start = end;
        }
        bucketQueue = new ArrayBlockingQueue<>(end - start + 1);
        for (int i = start; i <= end; i++) {
            bucketQueue.add(i);
        }
    }

    @Override
    public void run() {
        while (!bucketQueue.isEmpty()) {
            Integer bucketId = bucketQueue.poll();
            String bucketMessage = MessageType.BUCKET.name() + "|" + bucketId + "|" + StringUtils.capitalize(contextMessage.getStoreName()) + "|" + StringUtils.capitalize(contextMessage.getRunMode()) + "|" + contextMessage.getTimestamp() + "|" + contextMessage.getCurrentServer();
            try {
                DEBUG_LOGGER.info("Bucket Message - " + bucketMessage);

                ContextMessage context = messageProcessor.getContextMessage(bucketMessage);
                context.setDuration(this.contextMessage.getDuration());

                /*
                 * The following Grouped-NV mode is bascially to support FBM
				 * offers that are grouped with Sears and/or Kmart. When it is
				 * difficult to run the full or delta indexing for FBM, but want
				 * to have ability to touch (the rank, particularly) the FBM
				 * offers also that were grouped with Sears and Kmart, this mode
				 * can be used. It only runs on the subsets of documents
				 */
                if (StringUtils.equalsIgnoreCase(contextMessage.getRunMode(), MessageType.GROUPED_NV.getCode())) {
                    translateQueue.addAll(getNVIds(context));
                } else {
                    translateQueue.addAll(getAllIds(context));
                }

                DEBUG_LOGGER.info("Processed bucket message - " + bucketMessage);
            } catch (SearchCommonException scE) {
                LOGGER.error("Could not get offerIds for the bucket message" + bucketMessage, scE);
            }
        }
    }


    // TODO: Dry run context check inside this should be removed
    private List<String> getAllIds(ContextMessage context) {
        List<String> messages = new ArrayList<>();
        List<String> offerIdList = gbServiceFacade.getOfferList(context.getStoreName(), context.getBucketId(), getFromDateTime(context), getToDateTime(context));
        Set<List<String>> offerIdSet = gbServiceFacade.getSetOfOfferIds(offerIdList);
        for (List<String> offerIds : offerIdSet) {
            Map<String, String> ssinToOfferMap = gbServiceFacade.getSsinAndOfferMapBasedOnOfferType(offerIds, "NV");
            for (Map.Entry<String, String> entry : ssinToOfferMap.entrySet()) {
                switch (MessageType.getMessageType(context.getMessageId())) {

                    case DRYRUN:
                        messages.add(messageProcessor.constructSSINAndOfferMessageForDryRun(entry.getKey(), entry.getValue(), context));
                        break;
                    default:
                        messages.add(messageProcessor.constructSSINAndOfferMessage(entry.getKey(), entry.getValue(), context));
                        break;
                }
            }
        }
        List<String> contentIds = gbServiceFacade.getContentIds(context.getStoreName(), context.getBucketId(), getFromDateTime(context), getToDateTime(context));
        for (String contentId : contentIds) {
            switch (MessageType.getMessageType(context.getMessageId())) {

                case DRYRUN:
                    messages.add(messageProcessor.constructSSINAndOfferMessageForDryRun(contentId, StringUtils.EMPTY, context));
                    break;
                default:
                    messages.add(messageProcessor.constructSSINAndOfferMessage(contentId, StringUtils.EMPTY, context));
                    break;
            }
        }
        LOGGER.info("Bucket " + context.getBucketId() + " retrieved total " + messages.size() + " ids.");
        return messages;
    }

    private List<String> getNVIds(ContextMessage context) {
        List<String> messages = new ArrayList<>();
        String storeName = context.getStoreName();
        if (context.getStoreName().matches("FBM")) {
            storeName = Stores.SEARS.name();
        }
        List<String> offerIdList = gbServiceFacade.getOfferList(storeName, context.getBucketId(), getFromDateTime(context), getToDateTime(context));
        Set<List<String>> offerIdSet = gbServiceFacade.getSetOfOfferIds(offerIdList);
        for (List<String> offerIds : offerIdSet) {
            List<String> ssins = gbServiceFacade.getSsinForOfferType(offerIds, "NV");
            for (String ssin : ssins) {
                List<String> groupedOfferIds = gbServiceFacade.getOfferIdsByAltKey(ssin);
                List<Offer> offerDocs = gbServiceFacade.getOfferDocsList(groupedOfferIds);
                for (Offer offerDoc : offerDocs) {
                    if (StringUtils.equalsIgnoreCase(offerDoc.getClassifications().getIsMpPgmType(), context.getStoreName())) {
                        String offerId = offerDoc.getId();
                        messages.add(messageProcessor.constructSSINAndOfferMessage(ssin, offerId, context));
                    }
                }
            }
        }
        LOGGER.info("Bucket " + context.getBucketId() + " retrieved " + messages.size() + " ids.");
        return messages;
    }

    private String getFromDateTime(ContextMessage context) {
        String fromDateTimeUTC = StringUtils.EMPTY;
        if (context.getDuration().getFromEpoch() != 0) {
            long fromEpoch = context.getDuration().getFromEpoch();
            fromDateTimeUTC = incremental.getUTCTime(fromEpoch);
        }
        return fromDateTimeUTC;
    }

    private String getToDateTime(ContextMessage context) {
        String toDateTimeUTC = StringUtils.EMPTY;
        if (context.getDuration().getFromEpoch() != 0) {
            toDateTimeUTC = incremental.getUTCTime(context.getDuration().getToEpoch());
        }
        return toDateTimeUTC;
    }

}
