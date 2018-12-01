package com.shc.ecom.search.rules;

import org.springframework.beans.factory.annotation.Autowired;

import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.persistence.NFiltersData;

public class RetiredTaxonomyRule implements IRule<String> {

    private static final long serialVersionUID = -3897592778099050729L;

    @Autowired
    private NFiltersData nFiltersData;

    /**
     * Returns <i>false</i> if the taxonomy is retired - i.e., unavailable in attached-data (from n-filters-data source); else <i>true</i>.
     */
    @Override
    public boolean evaluate(String hierarchy, ContextMessage context) {
        if (nFiltersData.getAttachedData().containsKey(hierarchy)) {
            return true;
        }
        return false;
    }

	/*@Override
    public boolean evaluate(Content content, ContextMessage context) {
		Stores store = Stores.getStore(context.getStoreName());
		Sites site = Stores.getSite(context.getStoreName());

		String primaryHierarchy = null;
		SpecificHierarchy specificHierarchy = null;
		switch (store) {
			case SEARS:
			case AUTO:
			case CPC:
				specificHierarchy = content.getTaxonomy().getWeb().getSites().getSears().getHierarchies().get(0);
				break;
			case KMART:
				specificHierarchy = content.getTaxonomy().getWeb().getSites().getKmart().getHierarchies().get(0);
				break;
			case MYGOFER:
				specificHierarchy = content.getTaxonomy().getWeb().getSites().getMygofer().getHierarchies().get(0);
				break;
			case SEARSPR:
				specificHierarchy = content.getTaxonomy().getWeb().getSites().getPuertorico().getHierarchies().get(0);
				break;
		}
		primaryHierarchy = getHierarchy(specificHierarchy, site);
		return nFiltersData.getAttachedData().containsKey(primaryHierarchy);
	}

	*//**
     * This methods return the hierarchy in the format that is present in the nFilterData file
     *
     * @param specificHierarchy
     * @param site
     * @return
     *//*
    private String getHierarchy(SpecificHierarchy specificHierarchy, Sites site) {
		StringBuilder hierarchy = new StringBuilder();

		String catalogId = site.getCatalogId();
		hierarchy.append(catalogId);
		List<WebIdName> webIdNameList = specificHierarchy.getSpecificHierarchy();
		for (WebIdName webIdName : webIdNameList) {
			hierarchy.append(SEPERATOR + webIdName.getName());
		}
		return hierarchy.toString().replaceAll("\\s", StringUtils.EMPTY);
	}*/
}
