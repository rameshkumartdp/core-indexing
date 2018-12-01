package com.shc.ecom.search.extract.components.uas;

import com.shc.common.index.rules.Decision;
import com.shc.common.index.rules.ValidationResults;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.kafka.SearchKafkaProducer;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.uas.UasDtoByOffer;
import com.shc.ecom.search.common.uas.UasDtoOfferChannels;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.rules.IRule;
import com.shc.ecom.search.util.DecisionUtil;
import com.shc.ecom.search.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class UasExtractComponent extends ExtractComponent<UasDtoOfferChannels> implements Serializable {

    private static final long serialVersionUID = -5905828228642145436L;

    @Autowired
    private UasService uasService;

    @Resource(name = "uasRules")
    private List<IRule<UasDtoByOffer>> rules;

    @Autowired
    private Validator validator;

    @Autowired
    private SearchKafkaProducer kafkaProducer;

    @Override
    protected UasDtoOfferChannels get(WorkingDocument wd, ContextMessage context) {
        UasDtoOfferChannels uasDtoOfferChannels = new UasDtoOfferChannels();
        uasDtoOfferChannels.setViewOnlyoffers(wd.getExtracts().getOfferExtract().getViewOnlyOffers());
        uasDtoOfferChannels.setUasDto(uasService.process(wd.getExtracts().getOfferIds(), context));
        return uasDtoOfferChannels;
    }

    @Override
    public Decision validate(UasDtoOfferChannels source, WorkingDocument wd, ContextMessage context) {
        ValidationResults offerResult = null;
        ValidationResults productResult = null;
        boolean productValid = false;

        Decision decision = wd.getDecision();

        for (Iterator<Map.Entry<String,Map<String,Boolean>>> offerIterator = source.getUasDto().getItemMap().entrySet().iterator(); offerIterator.hasNext();) {
            Map.Entry<String,Map<String,Boolean>> entry = offerIterator.next();
            String offerId = entry.getKey();
            boolean isViewOnlyOffer = checkViewOnlyOffer(source, offerId);
            UasDtoByOffer uasDtoByOffer = new UasDtoByOffer();
            uasDtoByOffer.setOfferId(offerId);
            uasDtoByOffer.setViewOnlyOffer(isViewOnlyOffer);
            uasDtoByOffer.setOfferItemMap(entry.getValue());
            offerResult = validator.validate(uasDtoByOffer, rules, context);
            boolean offerValid = offerResult.isPassed();

            if (!offerValid) {
                Decision offerDecision = DecisionUtil.generateDecision(wd,context,offerId,offerResult);
                DecisionUtil.incrementCounterAddToKafka(metricManager, kafkaProducer, offerDecision, offerResult.getRejectedRule());
                wd.getExtracts().getUasExtract().getItemAvailabilityMap().remove(offerId);
                offerIterator.remove();
            }
            productValid |= offerValid;
        }

        decision.setId(wd.getExtracts().getSsin());
        decision.setIdType("SSIN");
        decision.setOfferId("");
        productResult = DecisionUtil.constructValidationResults(productValid, Metrics.OUT_OF_STOCK_PRODUCT_RULE);
        decision.addValidationResults(productResult);
        decision.setRejected(!productValid);
        decision.setStore(context.getStoreName());
        decision.setSites(DecisionUtil.getSitesList(Stores.getStore(context.getStoreName()).getEligibleSites(wd, context)));
        decision.setOpsUuid(context.getTimestamp());
        return decision;
    }

    @Override
    protected Extracts extract(UasDtoOfferChannels uasDomainObject, WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();

        Map<String, Map<String, Boolean>> itemAvailabilityMap = uasDomainObject.getUasDto().getItemMap();
        extracts.getUasExtract().setItemAvailabilityMap(itemAvailabilityMap);
        extracts.getUasExtract().setAvailableItems(new ArrayList<>(itemAvailabilityMap.keySet()));
        return extracts;
    }

    public static boolean checkViewOnlyOffer(UasDtoOfferChannels source, String offerId){
        return source.getViewOnlyoffers().containsKey(offerId)
                && source.getViewOnlyoffers().get(offerId).equals(GlobalConstants.VIEW_ONLY_FFM_CHANNEL);
    }
}
