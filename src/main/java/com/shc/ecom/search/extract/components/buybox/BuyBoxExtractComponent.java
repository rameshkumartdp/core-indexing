package com.shc.ecom.search.extract.components.buybox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import com.shc.common.index.rules.Decision;
import com.shc.common.index.rules.ValidationResults;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.shc.ecom.search.common.constants.ItemCondition;
import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.greenbox.GBServiceFacade;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.vo.buybox.BuyBoxDomain;
import com.shc.ecom.search.common.vo.buybox.Offers;
import com.shc.ecom.search.common.vo.buybox.RankGrps;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.rules.IRule;
import com.shc.ecom.search.util.DecisionUtil;
import com.shc.ecom.search.validator.Validator;

@Component
public class BuyBoxExtractComponent extends ExtractComponent<Map<Sites, BuyBoxDomain>> {

    private static final long serialVersionUID = 5807610701069673732L;

    @Autowired
    private GBServiceFacade gbServiceFacade;

    @Autowired
    private Validator validator;

    @Resource(name = "rankingRules")
    private List<IRule<Map<Sites, BuyBoxDomain>>> rules;

    private ConcurrentHashMap<String, BuyBoxDomain> buyboxDocuments = new ConcurrentHashMap<>();

    @Override
    protected Map<Sites, BuyBoxDomain> get(WorkingDocument wd, ContextMessage context) {
        String uuid = wd.getExtracts().getContentExtract().getUuid();

        List<Sites> sites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        Map<Sites, BuyBoxDomain> siteBuyboxDomainMap = new HashMap<>();
        for (Sites site : sites) {
            String rankCacheKey = site.getSiteName() + "_" + uuid;
            if (buyboxDocuments.containsKey(rankCacheKey)) {
                siteBuyboxDomainMap.put(site, buyboxDocuments.get(rankCacheKey));
            } else {
                BuyBoxDomain buyBoxDomain = gbServiceFacade.getBuyBoxDocument(uuid, site.getSiteName());
                siteBuyboxDomainMap.put(site, buyBoxDomain);
                buyboxDocuments.put(rankCacheKey, buyBoxDomain);
            }
        }
        return siteBuyboxDomainMap;
    }

    @Override
    public Decision validate(Map<Sites, BuyBoxDomain> source, WorkingDocument wd, ContextMessage context) {
        ValidationResults results = validator.validate(source, rules, context);
        Decision decision = wd.getDecision();
        decision.setId(wd.getExtracts().getSsin());
        decision.setIdType("SSIN");
        decision.setRejected(!results.isPassed());
        decision.addValidationResults(results);
        decision.setOfferId(context.getOfferId());
        decision.setStore(context.getStoreName());
        decision.setSites(DecisionUtil.getSitesList(Stores.getStore(context.getStoreName()).getEligibleSites(wd, context)));
        decision.setOpsUuid(context.getTimestamp());
        return decision;
    }

    @Override
    protected Extracts extract(Map<Sites, BuyBoxDomain> sitesBuyBoxDomainMap, WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();
        String offerId = StringUtils.EMPTY;
        if (!wd.getExtracts().getOfferIds().isEmpty()) {
            offerId = wd.getExtracts().getOfferIds().get(0);
        }

        Map<Sites, Integer> siteOfferRankMap = new HashMap<>();
        Map<Sites, Boolean> sitesItemConditionMap = Maps.newHashMap();
        Map<Sites, List<String>> groupedOfferIds = new HashMap<>();
        Set<String> kmartOrSearsOfferIds = new HashSet<>();
        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);

        for (Sites site : eligibleSites) {
            if (sitesBuyBoxDomainMap.containsKey(site)) {
                siteOfferRankMap.put(site, getRank(sitesBuyBoxDomainMap.get(site), offerId));
                /*
                 * For rank 1 offers store additional details regarding the other offers in the group
                 */
                if(getRank(sitesBuyBoxDomainMap.get(site), offerId)==1) {
                	
                	groupedOfferIds.put(site, getGroupedOfferIds(sitesBuyBoxDomainMap.get(site)));
                	
                	/*
                	 * Only for kmart site, we propose to make additional offer calls after this extraction to retrieve ksns for kmart offer in this group.
                	 */
                    if(site.matches("KMART") || site.matches("SEARS")) {
                        kmartOrSearsOfferIds.addAll(getKmartOrSearsGroupedOfferIds(sitesBuyBoxDomainMap.get(site), site, context.getStoreName()));
                    }
                }
                sitesItemConditionMap.put(site, getMultipleItemCondition(sitesBuyBoxDomainMap.get(site), extracts));
            }
        }

        extracts.getBuyboxExtract().setSitesOfferRankMap(siteOfferRankMap);
        extracts.getBuyboxExtract().setSitesMultipleItemConditionsMap(sitesItemConditionMap);
        extracts.getBuyboxExtract().setGroupedOfferIds(groupedOfferIds);
        extracts.getBuyboxExtract().setKmartOrSearsOfferIds(kmartOrSearsOfferIds);
        return extracts;
    }

    /**
     * getMultipleItemCondition - check if the ssin contains offers which are either used or refurbished
     * returns true if the ssin is new and even one offer is used or refurbished
     *
     * @param buyBoxDomain
     * @param extracts
     * @return
     */
    private boolean getMultipleItemCondition(BuyBoxDomain buyBoxDomain, Extracts extracts) {

        List<RankGrps> rankGrpsList = buyBoxDomain.getGroups();
        for (RankGrps rankGrps : rankGrpsList) {
            for (Offers offers : rankGrps.getOffers()) {
                String offerItemConditionParent = ItemCondition.getItemCondition(extracts.getOfferExtract().getItemCondition());
                String conditionStringParent = ItemCondition.getItemCondition(offers.getConditionString());
                if (!StringUtils.equalsIgnoreCase(offerItemConditionParent, conditionStringParent)) {
                    return true;
                }
            }
        }
        return false;

    }


    private int getRank(BuyBoxDomain buyBoxDomain, String offerId) {
        int rank = 0;
        if (CollectionUtils.isNotEmpty(buyBoxDomain.getGroups())) {
            List<Offers> buyboxListedOffers = buyBoxDomain.getGroups().get(0).getOffers();
            for (Offers offer : buyboxListedOffers) {
                if (StringUtils.equalsIgnoreCase(offer.getId(), offerId)) {
                    return offer.getRank();
                }
            }
        }
        return rank;
    }
    
	private List<String> getGroupedOfferIds(BuyBoxDomain buyBoxDomain) {
		List<String> offerIds = new ArrayList<>();
		 if (CollectionUtils.isNotEmpty(buyBoxDomain.getGroups())) {
	            List<Offers> buyboxListedOffers = buyBoxDomain.getGroups().get(0).getOffers();
	            for (Offers offer : buyboxListedOffers) {
	            	offerIds.add(offer.getId());
	            }
		 }
		return offerIds;
	}

    private Set<String> getKmartOrSearsGroupedOfferIds(BuyBoxDomain buyBoxDomain, Sites site, String contextStoreName) {
        Set<String> kmartOrSearsOfferIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(buyBoxDomain.getGroups())) {
            List<Offers> buyboxListedOffers = buyBoxDomain.getGroups().get(0).getOffers();
            for (Offers offer : buyboxListedOffers) {
                if(StringUtils.equalsIgnoreCase(contextStoreName,offer.getStoreName())){
                    continue;
                }
                switch (offer.getStoreName()) {
                    case "Kmart":
                    case "Sears":
                        kmartOrSearsOfferIds.add(offer.getId());
                        break;
                    default:
                        break;
                }
            }
        }
        return kmartOrSearsOfferIds;
    }
}