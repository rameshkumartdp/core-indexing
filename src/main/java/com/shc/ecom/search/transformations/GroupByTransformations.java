package com.shc.ecom.search.transformations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.shc.ecom.search.common.constants.SearchCommonConstants;
import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.docbuilder.GroupByOfferDoc;
import com.shc.ecom.search.docbuilder.GroupByParentDoc;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jsingar on 1/24/18.
 */
@Component
public class GroupByTransformations {
    private ObjectMapper mapper;

    @PostConstruct
    public void configureMapper() {
        // Confiugure the ObjectMapper with the custom Boolean serializer
        SimpleModule module = new SimpleModule("BooleanAsString", new Version(1, 0, 0, null, null, null));
        // module.addSerializer(Boolean.class,new BooleanSerializer());
        //module.addSerializer(boolean.class,new BooleanSerializer());
        mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(module);

    }

    public List<String> groupByTransform(List<String> jsonDocList) throws IOException {
        return jsonDocList.stream()
                .map(n -> {
                    try {
                        return getGroupByJson(n);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .map(n -> {
                    try {
                        return transformCategories(n);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .map(n -> {
                    try {
                        return addProductTitle(n);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .map(n -> {
                    try {
                        return transformContextualFields(n);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .map(n -> {
                    try {
                        return transformSearchableAttributes(n);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .map(n -> {
                    try {
                        return transformOfferSearchableAttributes(n);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .map(n -> {
                    try {
                        return transformQtSearchField(n);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .map(n -> {
                    try {
                        return transformGbiPartNumberOrSSIN(n);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .map(n -> {
                    try {
                        return addCommercialNodes(n);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .map(n -> {
                    try {
                        return gbDoctoStr(n);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());

    }

    /**
     * Method to combine ssin and partnumber
     * This field will be used for gby deletes.
     *
     * @param groupByParentDoc
     * @return
     */
    public GroupByParentDoc transformGbiPartNumberOrSSIN(GroupByParentDoc groupByParentDoc) {
        List<String> gbiPartNumberOrSSIN = new ArrayList<>();

        if (StringUtils.isNotBlank(groupByParentDoc.getPartnumber())) {
            gbiPartNumberOrSSIN.add(groupByParentDoc.getPartnumber());
        }

        if (StringUtils.isNotBlank(groupByParentDoc.getSin())) {
            gbiPartNumberOrSSIN.add(groupByParentDoc.getSin());
        }

        // appending partnumber field from child document
        if (CollectionUtils.isNotEmpty(groupByParentDoc.get_childDocuments_())) {
            for (GroupByOfferDoc offerDoc : groupByParentDoc.get_childDocuments_()) {
                gbiPartNumberOrSSIN.add(offerDoc.getPartnumber());
            }
        }

        if (CollectionUtils.isNotEmpty(gbiPartNumberOrSSIN)) {
            groupByParentDoc.setGbiPartNumberOrSSIN(gbiPartNumberOrSSIN);
        }

        return groupByParentDoc;
    }

    /**
     * This method will combine 8 fields and update groupByParentDoc.genericPartNumber field.
     *
     * @param groupByParentDoc
     * @return
     */
    public GroupByParentDoc transformQtSearchField(GroupByParentDoc groupByParentDoc) {
        List<String> genericPartNumberList = new ArrayList<>();

        List<String> genericPartnum = getGenericPartnumber(groupByParentDoc);
        if (CollectionUtils.isNotEmpty(genericPartnum)) {
            genericPartNumberList.addAll(genericPartnum);
        }

        List<String> genericPartNumberExt = getGenericPartNumberExt(groupByParentDoc);
        if (CollectionUtils.isNotEmpty(genericPartNumberExt)) {
            genericPartNumberList.addAll(genericPartNumberExt);
        }

        if (CollectionUtils.isNotEmpty(genericPartNumberList)) {
            groupByParentDoc.setGenericPartNumber(genericPartNumberList);
        }
        return groupByParentDoc;
    }

    /**
     * This method will populate partnumber ,itemnumber , xref , parentIds.
     * All fields are present at parent level except partnumber field is present at both parent and child level
     *
     * @param groupByParentDoc
     * @return
     */
    public List<String> getGenericPartNumberExt(GroupByParentDoc groupByParentDoc) {
        List<String> fieldList = new ArrayList<>();

        if (StringUtils.isNoneBlank(groupByParentDoc.getPartnumber())) {
            fieldList.add(groupByParentDoc.getPartnumber());
        }

        // appending partnumber field from child document
        if (CollectionUtils.isNotEmpty(groupByParentDoc.get_childDocuments_())) {
            for (GroupByOfferDoc offerDoc : groupByParentDoc.get_childDocuments_()) {
                fieldList.add(offerDoc.getPartnumber());
            }
        }

        if (StringUtils.isNoneBlank(groupByParentDoc.getItemnumber())) {
            fieldList.add(groupByParentDoc.getItemnumber());
        }

        if (CollectionUtils.isNotEmpty(groupByParentDoc.getXref())) {
            fieldList.addAll(groupByParentDoc.getXref());
        }

        if (CollectionUtils.isNotEmpty(groupByParentDoc.getParentIds())) {
            fieldList.addAll(groupByParentDoc.getParentIds());
        }
        return fieldList;
    }

    /**
     * This method will populate sin, fullmfpartno , mfpartno, ksn
     *
     * @param groupByParentDoc
     * @return
     */
    public List<String> getGenericPartnumber(GroupByParentDoc groupByParentDoc) {
        List<String> fieldList = new ArrayList<>();
        if (StringUtils.isNoneBlank(groupByParentDoc.getSin())) {
            fieldList.add(groupByParentDoc.getSin());
        }
        if (StringUtils.isNotBlank(groupByParentDoc.getFullmfpartno())) {
            fieldList.add(groupByParentDoc.getFullmfpartno());
        }
        if (StringUtils.isNotBlank(groupByParentDoc.getMfpartno())) {
            fieldList.add(groupByParentDoc.getMfpartno());
        }
        if (CollectionUtils.isNotEmpty(groupByParentDoc.getKsn())) {
            fieldList.addAll(groupByParentDoc.getKsn());
        }
        return fieldList;
    }

    public GroupByParentDoc getGroupByJson(String jsonDoc) throws IOException {
        return mapper.readValue(jsonDoc, GroupByParentDoc.class);

    }

    public GroupByParentDoc transformCategories(GroupByParentDoc gbJsonObj) throws IOException {
        List<String> categories = new ArrayList<>(gbJsonObj.getCategory());
        gbJsonObj.setSHCCategories(gbJsonObj.getCategories());

        String scomCatalogId = Sites.COMMERCIAL.getCatalogId();
        if(gbJsonObj.getCatalogs().contains(scomCatalogId)) {
            List<String> tempCategories = new ArrayList<>();
            Iterator<String> itr = categories.iterator();
            while(itr.hasNext()) {
                tempCategories.add(scomCatalogId+"_"+itr.next());
            }
            categories.clear();
            categories.addAll(tempCategories);
        }

        if (categories == null) {
            return gbJsonObj;
        }
        List<Map<String, String>> catMapList = new ArrayList<>();
        for (String category : categories) {
            String[]            categorySplit = category.split("_");
            Map<String, String> categoryMap   = new HashMap<>();
            int                 nodePos       = 0;
            for (String cat : categorySplit) {
                categoryMap.put(Integer.toString(++nodePos), cat);
                if (cat.equalsIgnoreCase(categorySplit[categorySplit.length - 1])) {
                    categoryMap.put("Leaf", cat);
                }
            }
            catMapList.add(categoryMap);
        }
        if (catMapList != null && !catMapList.isEmpty()) {
            gbJsonObj.setGbiCategories(catMapList);
        }

        return gbJsonObj;

    }

    public GroupByParentDoc addProductTitle(GroupByParentDoc groupByParentDoc) throws IOException {
        groupByParentDoc.setTitle(groupByParentDoc.getName());
        return groupByParentDoc;

    }

    public GroupByParentDoc addCommercialNodes(GroupByParentDoc groupByParentDoc) throws IOException {
        if(groupByParentDoc.getCatalogs().contains(Sites.COMMERCIAL.getCatalogId())) {
            groupByParentDoc.setScomPrice(groupByParentDoc.getScomPrice());
            groupByParentDoc.setBrand(groupByParentDoc.getBrand());
            groupByParentDoc.setColorFamily(groupByParentDoc.getColorFamily());
            groupByParentDoc.setWidth(groupByParentDoc.getWidth());
            groupByParentDoc.setHeight(groupByParentDoc.getHeight());
            groupByParentDoc.setDepth(groupByParentDoc.getDepth());
            groupByParentDoc.setEnergyStarCompliant(groupByParentDoc.getEnergyStarCompliant());
            groupByParentDoc.setAdaCompliant(groupByParentDoc.getAdaCompliant());
        }
        return groupByParentDoc;
    }

    public GroupByParentDoc transformSearchableAttributes(GroupByParentDoc groupByParentDoc) throws IOException {
        List<String> searchableAttrs = groupByParentDoc.getSearchableAttributes();
        if (searchableAttrs == null) {
            return groupByParentDoc;
        }
        groupByParentDoc.setFilters(searchableAttrTransform(searchableAttrs));
        return groupByParentDoc;

    }

    public GroupByParentDoc transformOfferSearchableAttributes(GroupByParentDoc groupByParentDoc) throws IOException {
        if (groupByParentDoc.get_childDocuments_().size() == 0) {
            return groupByParentDoc;
        }
        for (GroupByOfferDoc groupByOfferDoc : groupByParentDoc.get_childDocuments_()) {
            List<String> searchableAttrs = groupByOfferDoc.getSearchableAttributesOffer_ss();
            if (searchableAttrs != null) {
                groupByOfferDoc.setOfferFilters(searchableAttrTransform(searchableAttrs));
            }
            //Changes to merge all the store unit fields into one

            List<String> storeFilterList           = new ArrayList<>();
            List<String> storeUnitList             = groupByOfferDoc.getStoreUnit_ss();
            List<String> reservableStoresList      = groupByOfferDoc.getReservableStores_ss();
            String       reservableAllStoresboolan = String.valueOf(groupByOfferDoc.isReservableAllStores_b());
            if (storeUnitList != null && !storeUnitList.isEmpty()) {
                for (String storeUnits : storeUnitList) {
                    storeFilterList.add("storeUnit_ss" + SearchCommonConstants.UNDERSCORE + storeUnits);
                }

            }
            if (reservableStoresList != null && !reservableStoresList.isEmpty()) {
                for (String reservableStores : reservableStoresList) {
                    storeFilterList.add("reservableStores_ss" + SearchCommonConstants.UNDERSCORE + reservableStores);
                }
            }
            if (!reservableAllStoresboolan.isEmpty()) {

                storeFilterList.add("reservableAllStores_b" + SearchCommonConstants.UNDERSCORE +
                        reservableAllStoresboolan);

            }
            groupByOfferDoc.setStoreFilters(storeFilterList);

        }
        return groupByParentDoc;
    }

    public GroupByParentDoc transformContextualFields(GroupByParentDoc groupByParentDoc) throws IOException {

        // set default rank and prdType for c2c products
        if (groupByParentDoc.getVendorId()!=null && groupByParentDoc.getVendorId().equalsIgnoreCase("PD")) {
            if (groupByParentDoc.getRank() == null || groupByParentDoc.getRank().isEmpty()) {
                groupByParentDoc.setRank("1");
            }

            if (groupByParentDoc.getPrdType() == null || groupByParentDoc.getPrdType().isEmpty()) {
                groupByParentDoc.setPrdType("online");
            }
        }

        //promoId
        if (groupByParentDoc.getPromoId() != null) {
            groupByParentDoc.setGbiPromoId(contextualSetToMapwithLimit(groupByParentDoc.getPromoId()));
        }
        //promo
        if (groupByParentDoc.getPromo() != null) {
            groupByParentDoc.setGbiPromo(contextualListToMap(groupByParentDoc.getPromo()));
        }
        //price
        if (groupByParentDoc.getPrice() != null) {
            groupByParentDoc.setGbiPrice(contextualListToMap(groupByParentDoc.getPrice()));
        }
        //sale
        if (groupByParentDoc.getSale() != null) {
            groupByParentDoc.setGbiSale(contextualListToMap(groupByParentDoc.getSale()));
        }
        //clearance
        if (groupByParentDoc.getClearance() != null) {
            groupByParentDoc.setGbiClearance(contextualListToMap(groupByParentDoc.getClearance()));
        }
        //instock
        if (groupByParentDoc.getInstock() != null) {
            groupByParentDoc.setGbiInstock(contextualListToMap(groupByParentDoc.getInstock()));
        }
        //itemssold
        if (groupByParentDoc.getItemsSold() != null) {
            groupByParentDoc.setGbiItemsSold(contextualListToMap(groupByParentDoc.getItemsSold()));
        }
        //revenue
        if (groupByParentDoc.getRevenue() != null) {
            groupByParentDoc.setGbiRevenue(contextualListToMap(groupByParentDoc.getRevenue()));
        }
        //conversion
        if (groupByParentDoc.getConversion() != null) {
            groupByParentDoc.setGbiConversion(contextualListToMap(groupByParentDoc.getConversion()));
        }
        //productviews
        if (groupByParentDoc.getProductViews() != null) {
            groupByParentDoc.setGbiProductViews(contextualListToMap(groupByParentDoc.getProductViews()));
        }
        //sears_international
        if (groupByParentDoc.getSears_international() != null) {
            groupByParentDoc.setGbiSears_international(contextualListToMap(groupByParentDoc.getSears_international()));
        }
        //spueligible
        if (groupByParentDoc.getSpuEligible() != null) {
            groupByParentDoc.setGbiSpuEligible(contextualListToMap(groupByParentDoc.getSpuEligible()));
        }
        //freeshipping
        if (groupByParentDoc.getFreeShipping() != null) {
            groupByParentDoc.setGbiFreeShipping(contextualListToMap(groupByParentDoc.getFreeShipping()));
        }
        //shipVantage
        if (groupByParentDoc.getShipVantage() != null) {
            groupByParentDoc.setGbiShipVantage(contextualListToMap(groupByParentDoc.getShipVantage()));
        }
        //freedelivery
        if (groupByParentDoc.getFreeDelivery() != null) {
            groupByParentDoc.setGbiFreeDelivery(contextualListToMap(groupByParentDoc.getFreeDelivery()));
        }
        //promotionTxt
        if (groupByParentDoc.getPromotionTxt() != null) {
            groupByParentDoc.setGbiPromotionTxt(contextualListToMapwithLimit(groupByParentDoc.getPromotionTxt()));
        }
        //delivery
        if (groupByParentDoc.getDelivery() != null) {
            groupByParentDoc.setGbiDelivery(contextualListToMap(groupByParentDoc.getDelivery()));
        }
        //offer
        if (groupByParentDoc.getOffer() != null) {
            groupByParentDoc.setGbiOffer(contextualListToMap(groupByParentDoc.getOffer()));
        }
        //avgratingContextual
        if (groupByParentDoc.getAvgRatingContextual() != null) {
            groupByParentDoc.setGbiAvgRatingContextual(contextualListToMap(groupByParentDoc.getAvgRatingContextual()));
        }
        //numreviewscontextual
        if (groupByParentDoc.getNumReviewsContextual() != null) {
            groupByParentDoc.setGbiNumReviewsContextual(contextualListToMap(groupByParentDoc.getNumReviewsContextual
                    ()));
        }
        //consumerreportratingcontextual
        if (groupByParentDoc.getConsumerReportRatingContextual() != null) {
            groupByParentDoc.setGbiConsumerReportRatingContextual(contextualListToMap(groupByParentDoc
                    .getConsumerReportRatingContextual()));
        }
        //fulfillment
        if (groupByParentDoc.getFulfillment() != null) {
            groupByParentDoc.setGbiFulfillment(contextualListToMap(groupByParentDoc.getFulfillment()));
        }

        //expirablefields
        if (groupByParentDoc.getExpirableFields() != null) {
            groupByParentDoc.setGbiExpirableFields(contextualListToMaponLast(groupByParentDoc.getExpirableFields()));
        }

        //freeShippingOffer_ss NOTE that this change is at the offer/child level unlike above ones which is on parent
        // levelk
        if (groupByParentDoc.get_childDocuments_().size() > 0) {
            for (GroupByOfferDoc groupByOfferDoc : groupByParentDoc.get_childDocuments_()) {
                List<String> freeShippingOffer_ss = groupByOfferDoc.getFreeShippingOffer_ss();
                if (freeShippingOffer_ss != null) {
                    groupByOfferDoc.setGbiFreeShippingOffer_ss(contextualListToMap(freeShippingOffer_ss));
                }
            }

        }

        return groupByParentDoc;


    }

    public String gbDoctoStr(GroupByParentDoc gbDoc) throws JsonGenerationException, JsonMappingException,
            JsonProcessingException {
        return mapper.writeValueAsString(gbDoc);
    }

    public List<Map<String, String>> contextualListToMap(List<String> contextualList) {
        return contextualList.stream().map(s -> {
            Map<String, String> element = new HashMap<>();
            String[]            arr     = s.split("_");
            element.put(arr[0], arr[1]);
            return element;
        }).collect(Collectors.toList());

    }

    public List<Map<String, String>> contextualListToMapwithLimit(List<String> contextualList) {
        return contextualList.stream().map(s -> {
            Map<String, String> element = new HashMap<>();
            String[]            arr     = s.split("_", 2);
            element.put(arr[0], arr[1]);
            return element;
        }).collect(Collectors.toList());

    }

    public List<Map<String, String>> contextualSetToMap(Set<String> contextualSet) {
        return contextualSet.stream().map(s -> {
            Map<String, String> element = new HashMap<>();
            String[]            arr     = s.split("_");
            element.put(arr[0], arr[1]);
            return element;
        }).collect(Collectors.toList());
    }

    public List<Map<String, String>> contextualSetToMapwithLimit(Set<String> contextualList) {
        return contextualList.stream().map(s -> {
            Map<String, String> element = new HashMap<>();
            String[]            arr     = s.split("_", 2);
            element.put(arr[0], arr[1]);
            return element;
        }).collect(Collectors.toList());

    }

    public List<Map<String, String>> contextualListToMaponLast(List<String> contextualList) {
        //Since the extracted value is a date field, epouch time is calculated instead of date/timestamp
        return contextualList.stream().map(s -> {
            Map<String, String> element = new HashMap<>();
            String[]            arr     = s.split("_(?!.*_)", 2);
            element.put(arr[0], epouchConverter(arr[1]));
            return element;
        }).collect(Collectors.toList());

    }

    public String epouchConverter(String instantTimeStamp) {
        Instant instant = Instant.parse(instantTimeStamp);
        Date    date    = Date.from(instant);
        Long    epouch  = date.getTime() / 1000L;
        return Long.toString(epouch);

    }

    public List<Map<String, String>> searchableAttrTransform(List<String> searchableAttrs) {
        return searchableAttrs.stream().map(s -> {
            Map<String, String> element = new HashMap<>();
            String[]            arr     = s.split("=");
            element.put(striptoMatchRegex(arr[0]), arr[1]);
            return element;
        }).collect(Collectors.toList());

    }

    //Dirty method to handle field names regexpression [a-zA-Z0-9-_] for gbi fieldNames
    public String striptoMatchRegex(String key) {
        return key.replace(" ", "_").replace(".", "_dot_").replace("(", "_open_").replace(")", "_close_").replace("&", "_and_").replace("/", "_forward_").replace(",", "_comma_");
    }


}
