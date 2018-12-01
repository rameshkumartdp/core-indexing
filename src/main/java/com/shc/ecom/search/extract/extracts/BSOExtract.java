package com.shc.ecom.search.extract.extracts;

import com.shc.ecom.search.common.constants.Sites;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rgopala
 */
public class BSOExtract implements Serializable {

    private static final long serialVersionUID = 8096116773022406353L;

    private Map<Sites, List<String>> sitesBehavioralMap = new HashMap<>();

    public Map<Sites, List<String>> getSitesBehavioralMap() {
        return sitesBehavioralMap;
    }

    public void setSitesBehavioralMap(Map<Sites, List<String>> sitesBehavioralMap) {
        this.sitesBehavioralMap = sitesBehavioralMap;
    }

}
