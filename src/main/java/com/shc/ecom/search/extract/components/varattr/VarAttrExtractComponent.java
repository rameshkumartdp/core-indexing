/**
 *
 */
package com.shc.ecom.search.extract.components.varattr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.shc.ecom.gb.doc.varattrs.UidDefAttrs;
import com.shc.ecom.gb.doc.varattrs.Uids;
import com.shc.ecom.gb.doc.varattrs.VarAttributes;
import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.greenbox.GBServiceFacade;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;

/**
 * @author rgopala Jul 27, 2015 search-doc-builder
 */
public class VarAttrExtractComponent extends ExtractComponent<VarAttributes> {

    private static final long serialVersionUID = 8661211345819468919L;

    @Autowired
    private GBServiceFacade gbServiceFacade;

    @Override
    protected VarAttributes get(WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();
        VarAttributes varAttributes = null;
        Sites site = Stores.getSite(context.getStoreName());

        //Fix for SEARSPR, they share the same varAttr but not site
        if (site == Sites.SEARSPR) {
            site = Sites.SEARS;
        }
        String id = site.getSiteName().toLowerCase() + "_" + extracts.getSsin();
        
        varAttributes = gbServiceFacade.getVarAttrDoc(id);
        
        return varAttributes;
    }

    @Override
    protected Extracts extract(VarAttributes varAttributes, WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();
        extracts.getVarAttrExtract().setSwatchLink(BooleanUtils.toBoolean(varAttributes.getIsSwatchLink()));
        extracts.getVarAttrExtract().setDefiningAttrsList(varAttributes.getDefiningAttrs());
        extracts.getVarAttrExtract().setOfferIdUiDefAttrsMap(getOfferIdUidDefAttrMap(varAttributes.getUids()));
        extracts.getVarAttrExtract().setAttrNameValuesMap(getAttrNameValuesMap(varAttributes.getUids()));
        extracts.getVarAttrExtract().setSsinPresent(isSsinPresent(varAttributes.getUids(), context.getPid()));
        extracts.getVarAttrExtract().setSsin(getSsin(varAttributes.getUids()));
        return extracts;
    }

    private String getSsin(List<Uids> uids) {
        String ssin = null;

        for (Uids uid : uids) {
            if (StringUtils.isNotEmpty(uid.getSsin())) {
                ssin = uid.getSsin();
                break;
            }
        }
        return ssin;
    }

    private boolean isSsinPresent(List<Uids> uids, String ssin) {
        boolean isSsinPresent = false;

        for (Uids uid : uids) {
            if (StringUtils.equalsIgnoreCase(uid.getSsin(), ssin)) {
                isSsinPresent = true;
                break;
            }
        }
        return isSsinPresent;
    }

    private Map<String, List<UidDefAttrs>> getOfferIdUidDefAttrMap(List<Uids> uids) {
        Map<String, List<UidDefAttrs>> offerIdUidDefAttrMap = new HashMap<>();
        for (Uids uid : uids) {
            // Assuming the contract that if offerId is null, offerList has mnore than 1 element
            if (CollectionUtils.isNotEmpty(uid.getOfferList())) {
                // Each offer will get the same the uidDefAttrs
                for (String offerId : uid.getOfferList()) {
                    offerIdUidDefAttrMap.put(offerId, uid.getUidDefAttrs());
                }
            }
        }
        return offerIdUidDefAttrMap;
    }

    public Map<String, List<String>> getAttrNameValuesMap(List<Uids> uids) {
        Map<String, List<String>> attrNameValuesMap = new HashMap<>();
        for (Uids uid : uids) {
            List<UidDefAttrs> uidDefAttrs = uid.getUidDefAttrs();
            for (UidDefAttrs uidDefAttr : uidDefAttrs) {
                String attrName = uidDefAttr.getAttrName();
                String value = uidDefAttr.getAttrVal();

                if (attrNameValuesMap.containsKey(attrName)) {
                    List<String> values = attrNameValuesMap.get(attrName);
                    values.add(value);
                    attrNameValuesMap.put(attrName, values);
                } else {
                    attrNameValuesMap.put(attrName, new ArrayList<>(Arrays.asList(value)));
                }
            }
        }
        return attrNameValuesMap;
    }

}
