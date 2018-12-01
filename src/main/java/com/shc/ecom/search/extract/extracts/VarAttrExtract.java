package com.shc.ecom.search.extract.extracts;

import com.shc.ecom.gb.doc.varattrs.DefiningAttrs;
import com.shc.ecom.gb.doc.varattrs.UidDefAttrs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rgopala
 */
public class VarAttrExtract implements Serializable {

    private static final long serialVersionUID = -2778022894557276870L;

    private boolean isSwatchLink;
    private boolean isSsinPresent;
    private List<DefiningAttrs> definingAttrsList;
    private Map<String, List<UidDefAttrs>> offerIdUiDefAttrsMap;
    private Map<String, List<String>> attrNameValuesMap;
    private String ssin;

    public String getSsin() {
        return ssin;
    }

    public void setSsin(String ssin) {
        this.ssin = ssin;
    }

    public boolean isSsinPresent() {
        return isSsinPresent;
    }

    public void setSsinPresent(boolean isSsinPresent) {
        this.isSsinPresent = isSsinPresent;
    }

    public Map<String, List<String>> getAttrsMap() {
        if (attrNameValuesMap == null) {
            return new HashMap<String, List<String>>();
        }
        return attrNameValuesMap;
    }

    public void setAttrNameValuesMap(Map<String, List<String>> attrNameValuesMap) {
        this.attrNameValuesMap = attrNameValuesMap;
    }

    public Map<String, List<UidDefAttrs>> getOfferIdUiDefAttrsMap() {
        return offerIdUiDefAttrsMap;
    }

    public void setOfferIdUiDefAttrsMap(Map<String, List<UidDefAttrs>> offerIdUiDefAttrsMap) {
        this.offerIdUiDefAttrsMap = offerIdUiDefAttrsMap;
    }

    /**
     * @return the isSwatchLink
     */
    public boolean isSwatchLink() {
        return isSwatchLink;
    }

    /**
     * @param isSwatchLink the isSwatchLink to set
     */
    public void setSwatchLink(boolean isSwatchLink) {
        this.isSwatchLink = isSwatchLink;
    }

    /**
     * @return the definingAttrsList
     */
    public List<DefiningAttrs> getDefiningAttrsList() {
        if (definingAttrsList == null) {
            return new ArrayList<>();
        }
        return definingAttrsList;
    }

    /**
     * @param definingAttrsList the definingAttrsList to set
     */
    public void setDefiningAttrsList(List<DefiningAttrs> definingAttrsList) {
        this.definingAttrsList = definingAttrsList;
    }

}
