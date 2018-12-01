package com.shc.ecom.search.ssinconsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.shc.common.misc.PropertiesLoader;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.tm.TopicModelingDTO;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.docbuilder.OfferDoc;
import com.shc.ecom.search.docbuilder.OfferDocBuilder;
import com.shc.ecom.search.docbuilder.SearchDoc;
import com.shc.ecom.search.docbuilder.SearchDocBuilder;
import com.shc.ecom.search.extract.components.tm.TopicModelingService;
import com.shc.ecom.search.ssinproducer.C2CBean;
import com.shc.ecom.search.util.JsonUtil;

@Component
public class C2CConsumer {

    @Autowired
    private TopicModelingService topicModelingService;


    public SearchDoc process(String jsonString) {
        C2CBean c2CBean = JsonUtil.convertToEntityObject(jsonString, C2CBean.class);
        SearchDocBuilder builder = buildDoc(c2CBean);

        //Offer Level customization for C2C.
        //The separation of C2C offer-level from other flows is by design
        builder.offerDocs(generateC2COfferDoc(c2CBean));

        SearchDoc document = new SearchDoc(builder);
        return document;
    }

    private SearchDocBuilder buildDoc(C2CBean c2CBean) {

        SearchDocBuilder builder = new SearchDocBuilder(c2CBean.getPartnumber());
        builder.opsUuid_s(c2CBean.getOpsUuid_s())
                .opsCurrentServer_i(c2CBean.getOpsCurrentServer_i())
                .rank_km("1")
                .catalogs(normalizeList(c2CBean.getCatalogs()))
                .level1Cats(normalizeSet(c2CBean.getLevel1Cats()))
                .level2Cats(normalizeSet(c2CBean.getLevel2Cats()))
                .level3Cats(normalizeSet(c2CBean.getLevel3Cats()))
                .vendorId(c2CBean.getVendorId())
                .vNames(normalizeSet(c2CBean.getvNames()))
                .cNames(normalizeSet(c2CBean.getcNames()))
                .sNames(normalizeSet(c2CBean.getsNames()))
                .url(c2CBean.getUrl())
                .id(c2CBean.getId())
                .name(c2CBean.getName())
                .image(c2CBean.getImage())
                .brand(c2CBean.getBrand())
                .price(normalizeList(c2CBean.getPrice()))
                .regPrice(c2CBean.getRegPrice())
                .priceRangeInd(c2CBean.getPriceRangeInd())
                .sale(normalizeList(c2CBean.getSale()))
                .clearance(normalizeList(c2CBean.getClearance()))
                .status(c2CBean.getStatus())
                .availDate(c2CBean.getAvailDate())
                .avgRatingContextual(normalizeList(c2CBean.getAvgRating()))
                .numReviewsContextual(normalizeList(c2CBean.getNumReviews()))
                .itemsSold(normalizeList(c2CBean.getItemsSold()))
                .productViews(normalizeList(c2CBean.getProductViews()))
                .revenue(normalizeList(c2CBean.getRevenue()))
                .conversion(normalizeList(c2CBean.getConversion()))
                .search1(c2CBean.getSearch1())
                .search2(c2CBean.getSearch2())
                .search3(c2CBean.getSearch3())
                .search4(c2CBean.getSearch4())
                .search5(c2CBean.getSearch5())
                .search6(c2CBean.getSearch6())
                .search7(c2CBean.getSearch7())
                .search8(c2CBean.getSearch8())
                .search9(c2CBean.getSearch9())
                .search10(c2CBean.getSearch10())
                .storeOrigin(normalizeList(c2CBean.getStoreOrigin()))
                .sin(c2CBean.getSin())
                .sellerCount(c2CBean.getSellerCount())
                .mfpartno(c2CBean.getMfpartno())
                .clickUrl(normalizeList(c2CBean.getClickUrl()))
                .saveStory(c2CBean.getSaveStory())
                .cpcClickRate(c2CBean.getCpcClickRate())
                .cpcFlag(c2CBean.getCpcFlag())
                .newItemFlag(NumberUtils.toInt(c2CBean.getNewItemFlag(), 0))
                .daysOnline(NumberUtils.toInt(c2CBean.getDaysOnline(), 0))
                .shippingMsg(c2CBean.getShippingMsg())
                .countryGroup(normalizeList(c2CBean.getCountryGroup()))
                .shipVantage(addStorePrefixList(c2CBean.getShipVantage()))
                //.freeShipping(c2CBean.getFreeShipping())
                .countryGroupExists(NumberUtils.toInt(c2CBean.getCountryGroupExists(), 0))
                .catConfidence(c2CBean.getCatConfidence())
                .sellers(normalizeList(c2CBean.getSellers()))
                .browseBoost(c2CBean.getBrowseBoost())
                .matureContentFlag(NumberUtils.toInt(c2CBean.getMatureContentFlag(), 0))
                .accessories(NumberUtils.toInt(c2CBean.getAccessories(), 0))
                .behavioral(normalizeList(c2CBean.getBehavioral()))
                .offer(normalizeList(c2CBean.getOffer()))
                .imageStatus(c2CBean.getImageStatus())
                .categories(normalizeSet(c2CBean.getCategories()))
                //.searchableAttributes(normalizeList(c2CBean.getSearchableAttributes()))
                .category(normalizeList(c2CBean.getCategory()))
                .lnames(normalizeSet(c2CBean.getLnames()))
                .primaryVertical(c2CBean.getPrimaryVertical())
                .primaryCategory(normalizeList(c2CBean.getPrimaryCategory()))
                .primaryHierarchy(normalizeList(c2CBean.getPrimaryHierarchy()))
                .nameSearchable(c2CBean.getNameSearchable())
                .primaryLnames(normalizeSet(c2CBean.getPrimaryLnames()))
                .xref(normalizeSet(c2CBean.getXref()))
                .topicIdWithRank(getTopicIdWithRankForC2C(c2CBean.getNameSearchable()))
                .topicIdWithNameSearchable(getTopicIdWithNameSearchableForC2C(c2CBean.getNameSearchable()));

        //OfferLevel customization
        builder = getOfferLevelFields(builder, c2CBean);
        return builder;
    }

    private List<String> getTopicIdWithRankForC2C(String nameSearchable) {
        List<String> topicIdWithRanks = new ArrayList<String>();

        Map<String, TopicModelingDTO> extractDtoForTM = new HashMap<>();
        TopicModelingDTO tmDtoForNameSearchable = topicModelingService.getInferenceAndConvertToDto(nameSearchable);

        if (tmDtoForNameSearchable != null) {
            extractDtoForTM.put(GlobalConstants.NAME_SEARCHABLE, tmDtoForNameSearchable);
            topicIdWithRanks = topicModelingService.getTopicIdWithRank(extractDtoForTM);
        }

        return topicIdWithRanks;
    }

    private String getTopicIdWithNameSearchableForC2C(String nameSearchable) {
        String topicIdWithNameSearchable = "";
        Map<String, TopicModelingDTO> extractDtoForTM = new HashMap<>();
        TopicModelingDTO tmDtoForNameSearchable = topicModelingService.getInferenceAndConvertToDto(nameSearchable);

        if (tmDtoForNameSearchable != null) {
            extractDtoForTM.put(GlobalConstants.NAME_SEARCHABLE, tmDtoForNameSearchable);
            topicIdWithNameSearchable = topicModelingService.getTopicModelingWithNameSearchable(extractDtoForTM);
        }

        return topicIdWithNameSearchable;
    }


    private List<String> addStorePrefixList(String input) {
        List<String> storePrefixList = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(input)) {
            storePrefixList.add(Stores.C2C.getStoreId() + "_" + input);
        }
        return storePrefixList;
    }

    private List<String> normalizeList(String input) {

        List<String> toReturn = new ArrayList<>();
        if (StringUtils.isEmpty(input)) {
            return toReturn;
        }

        List<String> values = Arrays.asList(input.split(";"));
        for (String value : values) {
            if (!StringUtils.isEmpty(value)) {
                toReturn.add(value);
            }
        }
        return toReturn;
    }

    private Set<String> normalizeSet(String input) {

        Set<String> toReturn = new HashSet<>();
        if (StringUtils.isEmpty(input)) {
            return toReturn;
        }

        List<String> values = Arrays.asList(input.split(";"));
        for (String value : values) {
            if (!StringUtils.isEmpty(value)) {
                toReturn.add(value);
            }
        }
        return toReturn;
    }

    /**
     * This method returns the List of OfferDoc for C2C. It is intentionally kept separate from the other stores.
     *
     * @return
     */
    private List<OfferDoc> generateC2COfferDoc(C2CBean c2CBean) {
        OfferDocBuilder offerDocBuilder = new OfferDocBuilder();

        offerDocBuilder.freeShipping(addStorePrefixList(c2CBean.getFreeShipping()))
                .opsUuid_s(c2CBean.getOpsUuid_s())
                .opsCurrentServer(c2CBean.getOpsCurrentServer_i())
                .searchableAttributes(normalizeList(c2CBean.getSearchableAttributes()))
                .productType("child");
        String id;
        if (StringUtils.isEmpty(c2CBean.getOffer())) {
            id = c2CBean.getPartnumber() + "_o_c2c";
        } else {
            id = c2CBean.getPartnumber() + "_" + c2CBean.getOffer() + "_o_c2c";
        }
        offerDocBuilder.offerDocId(id);
        OfferDoc offerDoc = new OfferDoc(offerDocBuilder);
        List<OfferDoc> offerDocList = new ArrayList<>();
        offerDocList.add(offerDoc);

        return offerDocList;
    }

    /**
     * Sets the parent level fields based on a flag. It does not set parent fields if the child has the field based on a
     * flag
     *
     * @param builder
     * @return
     */
    private SearchDocBuilder getOfferLevelFields(SearchDocBuilder builder, C2CBean c2CBean) {

        boolean removeFieldsFromParent = BooleanUtils.toBoolean(PropertiesLoader.getProperty(GlobalConstants.FEAT_REMOVE_OFFER_FIELDS_FROM_PARENT));
        //Return the old fields if we are not removing them
        if (!removeFieldsFromParent) {
            builder.searchableAttributes(normalizeList(c2CBean.getSearchableAttributes()));
            //TODO - Add freeshipping after 8/22 meeting
        }
        return builder;
    }
}