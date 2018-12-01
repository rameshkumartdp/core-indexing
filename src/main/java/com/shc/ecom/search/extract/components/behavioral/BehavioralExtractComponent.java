package com.shc.ecom.search.extract.components.behavioral;

import com.shc.ecom.search.common.behavioral.BehavioralDto;
import com.shc.ecom.search.common.behavioral.Docs;
import com.shc.ecom.search.common.behavioral.Response;
import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.pidmapping.RetailDta;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Behavioral extraction is done here along with feature to override behavioral data of new ssin with old ssin in pidMapping
 * file.
 *
 * @author vsingh8
 */
@Component
public class BehavioralExtractComponent extends ExtractComponent<BehavioralDto> implements Serializable {

    private static final long serialVersionUID = -2741765720506664591L;

    private static final String SEPARATOR = "_";

    @Autowired
    private BehavioralService behavioralService;

    @Override
    protected BehavioralDto get(WorkingDocument wd, ContextMessage context) {

        BehavioralDto orignalBehavioralDTO = getBsoExtract(wd, context);
        String ssinToReplace = wd.getExtracts().getSsin();
        if (RetailDta.isPresent(ssinToReplace)) {
            // Replace bso data for those sites only which are present in ssin in context.
            List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
            String ssinToReplaceWith = RetailDta.getOldPid(ssinToReplace);
            BehavioralDto behavouralToReplaceWith = getBsoExtractForReplacement(eligibleSites, ssinToReplaceWith);
            orignalBehavioralDTO = getMaxBehavioralDTO(orignalBehavioralDTO, behavouralToReplaceWith, ssinToReplace);
        }
        return orignalBehavioralDTO;
    }

    private BehavioralDto getBsoExtract(WorkingDocument wd, ContextMessage context) {

        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        String ssin = wd.getExtracts().getSsin();
        BehavioralDto behavioralDTO = behavioralService.process(eligibleSites, ssin);

        // Fall-back retry for KMART if there is no BSO/Prodstats history with SSIN. Try with Kmart offer's Parent-ID.
        if (Stores.KMART.matches(context.getStoreName()) && behavioralDTO.getResponse().getNumFound() == 0) {
            String kmartOfferParentId = wd.getExtracts().getOfferExtract().getKmartOfferParentId();
            if (!StringUtils.equalsIgnoreCase(ssin, kmartOfferParentId)) {
                behavioralDTO = behavioralService.process(eligibleSites, kmartOfferParentId);
            }
        }
        return behavioralDTO;
    }

    /**
     * This method should be used in case where we are not worrying about the availability of behavioral data. If behavioral data
     * is not present then we will not fall-back retry behavioralService using parent id of offer in context. This method assumes
     * in pidMapping file the old ssin is required ssin or parent of offer with correct bso data.
     *
     * @param eligibleSites
     * @param ssin
     * @return
     */
    private BehavioralDto getBsoExtractForReplacement(List<Sites> eligibleSites, String ssin) {

        return behavioralService.process(eligibleSites, ssin);

    }

    @Override
    protected Extracts extract(BehavioralDto behavioralDomainObject, WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();

        List<Docs> documents = behavioralDomainObject.getResponse().getDocs();

        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);

        Map<Sites, List<String>> siteBehavioralMap = new HashMap<>();
        for (Sites eligibleSite : eligibleSites) {
            for (Docs document : documents) {
                if (CollectionUtils.isNotEmpty(document.getBehavioral())) {
                    String id = document.getId();
                    String site = null;
                    if (id.split("_").length >= 2) {
                        site = id.split("_")[0];
                    }

                    if (StringUtils.equalsIgnoreCase(eligibleSite.getSiteName(), site)) {
                        List<String> behavioralData;
                        if (siteBehavioralMap.containsKey(eligibleSite)) {
                            behavioralData = siteBehavioralMap.get(eligibleSite);
                        } else {
                            behavioralData = new ArrayList<>();
                        }
                        behavioralData.addAll(document.getBehavioral());
                        siteBehavioralMap.put(eligibleSite, behavioralData);
                    }
                }
            }
        }

        extracts.getBsoExtract().setSitesBehavioralMap(siteBehavioralMap);
        return extracts;
    }

    private BehavioralDto getMaxBehavioralDTO(BehavioralDto orignalBehavioralDTO, BehavioralDto behavouralToReplaceWith,
        String ssin) {

        Map<String, Map<String, Float>> orignalBsoMap = createMapForOrignalbso(orignalBehavioralDTO);
        replaceBsowithNewBsoDto(orignalBsoMap, behavouralToReplaceWith.getResponse().getDocs());
        return createBehavioralDtoFromMap(orignalBsoMap, ssin);

    }

    private BehavioralDto createBehavioralDtoFromMap(Map<String, Map<String, Float>> orignalBsoMap, String ssin) {
        BehavioralDto behavioralDto = new BehavioralDto();
        Response response = new Response();
        List<Docs> docsForAllSites = new ArrayList<>();

        for (Entry<String, Map<String, Float>> entry : orignalBsoMap.entrySet()) {
            String site = entry.getKey();
            String id = site + SEPARATOR + ssin;
            Docs doc = new Docs();
            Map<String, Float> mapForSingleSite = orignalBsoMap.get(site);
            List<String> bsoListForSingleSite = new ArrayList<>();
            for (Entry<String, Float> entry2 : mapForSingleSite.entrySet()) {
                String singleBso = entry2.getKey();
                String value = String.valueOf(mapForSingleSite.get(singleBso));
                String bsoValueString = singleBso + SEPARATOR + value;
                bsoListForSingleSite.add(bsoValueString);
            }
            doc.setBehavioral(bsoListForSingleSite);
            doc.setId(id);
            docsForAllSites.add(doc);
        }
        response.setDocs(docsForAllSites);
        response.setNumFound(orignalBsoMap.keySet().size());
        behavioralDto.setResponse(response);
        return behavioralDto;
    }

    private Map<String, Map<String, Float>> createMapForOrignalbso(BehavioralDto orignalBehavioralDTO) {
        Map<String, Map<String, Float>> orignalBsoMapForAllSites = new HashMap<>();
        List<Docs> docs = orignalBehavioralDTO.getResponse().getDocs();
        for (Docs singleDto : docs) {
            String id = singleDto.getId();
            String site = getSiteFromDocId(id);
            Map<String, Float> bsoMapForSingleSite;
            if (orignalBsoMapForAllSites.containsKey(site)) {
                bsoMapForSingleSite = orignalBsoMapForAllSites.get(site);
            } else {
                bsoMapForSingleSite = new HashMap<>();
            }
            createBsoValueMap(singleDto, bsoMapForSingleSite);
            orignalBsoMapForAllSites.put(site, bsoMapForSingleSite);
        }
        return orignalBsoMapForAllSites;
    }

    private void createBsoValueMap(Docs singleDoc, Map<String, Float> bsoMapForSingleSite) {
        List<String> behavioralList = singleDoc.getBehavioral();
        for (String singleBehaviroal : behavioralList) {
            String behaviroalString = singleBehaviroal.substring(0, singleBehaviroal.lastIndexOf(SEPARATOR));
            String value = singleBehaviroal.substring(singleBehaviroal.lastIndexOf(SEPARATOR) + 1);
            Float valueInFloat = Float.parseFloat(value);
            if (bsoMapForSingleSite.containsKey(behaviroalString)) {
                Float presentValue = bsoMapForSingleSite.get(behaviroalString);
                if (valueInFloat > presentValue) {
                    bsoMapForSingleSite.put(behaviroalString, valueInFloat);
                }
            } else {
                bsoMapForSingleSite.put(behaviroalString, valueInFloat);
            }
        }
    }

    private String getSiteFromDocId(String id) {
        String[] parts = id.split(SEPARATOR);
        if (StringUtils.isEmpty(parts[0])) {
            return StringUtils.EMPTY;
        }
        return parts[0];
    }

    private void replaceBsowithNewBsoDto(Map<String, Map<String, Float>> orignalBsoMap, List<Docs> docsToReplaceWith) {
        for (Docs singleDto : docsToReplaceWith) {
            String id = singleDto.getId();
            String site = getSiteFromDocId(id);
            Map<String, Float> bsoValueMapForSingleSite;
            if (orignalBsoMap.containsKey(site)) {
                bsoValueMapForSingleSite = orignalBsoMap.get(site);
                mergeForSingleSite(bsoValueMapForSingleSite, singleDto);
            } else {
                bsoValueMapForSingleSite = new HashMap<>();
                createBsoValueMap(singleDto, bsoValueMapForSingleSite);
                orignalBsoMap.put(site, bsoValueMapForSingleSite);
            }
            orignalBsoMap.put(site, bsoValueMapForSingleSite);
        }
    }

    private void mergeForSingleSite(Map<String, Float> orignalBsoMap, Docs bsoDtoToReplaceWith) {
        List<String> behavioralList = bsoDtoToReplaceWith.getBehavioral();
        for (String singleBehaviroal : behavioralList) {
            String behaviroalString = singleBehaviroal.substring(0, singleBehaviroal.lastIndexOf(SEPARATOR));
            String valueToReplaceWith = singleBehaviroal.substring(singleBehaviroal.lastIndexOf(SEPARATOR) + 1);
            Float valueInFloatToReplaceWith = Float.parseFloat(valueToReplaceWith);
            if (orignalBsoMap.containsKey(behaviroalString)) {
                Float currentValuePresent = orignalBsoMap.get(behaviroalString);
                valueInFloatToReplaceWith = valueInFloatToReplaceWith > currentValuePresent ? valueInFloatToReplaceWith
                    : currentValuePresent;
            }
            orignalBsoMap.put(behaviroalString, valueInFloatToReplaceWith);
        }
    }

}
