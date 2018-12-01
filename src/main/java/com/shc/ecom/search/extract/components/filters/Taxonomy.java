/**
 *
 */
package com.shc.ecom.search.extract.components.filters;

import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author rgopala
 *         <p>
 *         Taxonomy related transformations and utilities
 */
@Component
public class Taxonomy implements Serializable {

    private static final long serialVersionUID = 2136266503510282050L;
    private static final String SEPARATOR = "_";

    public List<String> getCategory(WorkingDocument wd, ContextMessage context) {
        List<String> category = new ArrayList<>();

        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);

        Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();

        for (Sites site : eligibleSites) {
            category.addAll(getHierarchiesAsString(site.getCatalogId(), siteWebHierarchiesMap.get(site)));
        }

        if (Stores.SEARS.matches(context.getStoreName())) {
            category.addAll(getHierarchiesAsString(Sites.CRAFTSMAN.getCatalogId(), siteWebHierarchiesMap.get(Sites.CRAFTSMAN)));
            category.addAll(getHierarchiesAsString(Sites.KENMORE.getCatalogId(), siteWebHierarchiesMap.get(Sites.KENMORE)));
        }
        return category;
    }

    private List<String> getHierarchiesAsString(String catalogId, List<List<String>> heirarchies) {
        List<String> hierarchiesAsString = new ArrayList<>();
        for (List<String> hierarchy : heirarchies) {
            hierarchiesAsString.add(catalogId + SEPARATOR + StringUtils.join(hierarchy, SEPARATOR));
        }
        return hierarchiesAsString;
    }

    public List<List<String>> getAllHierarchies(WorkingDocument wd, Sites site) {
        Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();
        List<List<String>> hierarchies = siteWebHierarchiesMap.get(site);
        return hierarchies;
    }

    public String getLNameAtLevel(List<String> hierarchy, int level) {
        if (CollectionUtils.isNotEmpty(hierarchy) && hierarchy.size() >= level) {
            return hierarchy.get(level - 1);
        }
        return null;
    }

    public String getPrimaryHierarchyLevelName(WorkingDocument wd, Sites site, int level) {
        List<List<String>> hierarchies = getAllHierarchies(wd, site);
        if (!hierarchies.isEmpty()) {
            return getLNameAtLevel(hierarchies.get(0), level);
        }
        return null;
    }
}
