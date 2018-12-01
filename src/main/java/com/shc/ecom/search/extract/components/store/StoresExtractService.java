package com.shc.ecom.search.extract.components.store;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.shc.common.misc.PropertiesLoader;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.shc.ecom.search.common.constants.OnlineWarehouse;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.greenbox.GBServiceFacade;
import com.shc.ecom.search.common.store.Store;
import com.shc.ecom.search.config.GlobalConstants;

public class StoresExtractService implements Serializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(StoresExtractService.class.getName());
	
	private static final int KMART_STORE_ID_LENGTH = 7;

	private static final long serialVersionUID = -2867153798444715941L;

	private Set<String> searsStsEligibleStores = new HashSet<>();
	private Set<String> kmartStsEligibleStores = new HashSet<>();


	private static final boolean STORE_ZIP_ENABLED = Boolean.parseBoolean(PropertiesLoader.getProperty(GlobalConstants.ENABLE_STORE_ZIP));
	private static final boolean FETCH_STORE_IDS_FROM_FILE = Boolean.parseBoolean(PropertiesLoader.getProperty(GlobalConstants.FETCH_STORE_IDS_FROM_FILE));
	private static final String SEARS_STOREZIP_STORE_IDS = PropertiesLoader.getProperty(GlobalConstants.SEARS_STOREZIP_STORE_IDS);
	private static final String KMART_STOREZIP_STORE_IDS = PropertiesLoader.getProperty(GlobalConstants.KMART_STOREZIP_STORE_IDS);
	
	@Autowired
	private GBServiceFacade gbServiceFacade;
	

	public void loadCrossFormatEligibleStores() {
		if(!STORE_ZIP_ENABLED) {
			if(!FETCH_STORE_IDS_FROM_FILE) {
				LOGGER.info("Store-zip (szip) calls and fetching stores from file disabled.");
				return;
			}
			
			LOGGER.info("Store-zip (szip) calls disabled.");
			String[] searsStoreIds = StringUtils.split(SEARS_STOREZIP_STORE_IDS, ",");
			for(String storeId : searsStoreIds) {				
				searsStsEligibleStores.add(storeId);
			}
			LOGGER.info("Fetched "+searsStsEligibleStores.size()+" store-ids for SEARS site from file.");
			String[] kmartStoreIds = StringUtils.split(KMART_STOREZIP_STORE_IDS, ",");
			for(String storeId : kmartStoreIds) {				
				kmartStsEligibleStores.add(storeId);
			}
			LOGGER.info("Fetched "+kmartStsEligibleStores.size()+" store-ids for KMART site from file.");
			
			return;
		}
		
		searsStsEligibleStores.addAll(getStoreIds(Stores.SEARS.name()));
		LOGGER.info("Fetched "+searsStsEligibleStores.size()+" store-ids for SEARS site via store-zip.");
		
		kmartStsEligibleStores.addAll(getStoreIds(Stores.KMART.name()));
		LOGGER.info("Fetched "+kmartStsEligibleStores.size()+" store-ids for KMART site via store-zip.");
	}

	private Set<String> getStoreIds(String storeName) {
		Set<String> ids = new HashSet<>();
		LOGGER.info("Fetching store-ids via store-zip for site "+storeName);
		List<String> storeIds = gbServiceFacade.getActiveStoreIdsByAltKey(storeName);
		storeIds = filterOutMktPlcStores(storeIds);		
		LOGGER.info("Fetching store documents for "+storeIds.size()+" store-ids via store-zip for site "+storeName);
		for (String storeId : storeIds) {
			Store storeDoc = gbServiceFacade.getStoreDoc(storeId);
			boolean stsElig = storeDoc.getBlob().getUnit().getStrAttr().getSites().getStrAttrDetailsForStore(storeName).isStsElig();
			boolean stsXfmt = storeDoc.getBlob().getUnit().getStrAttr().getSites().getStrAttrDetailsForStore(storeName).isStsXfmt();
			if (stsElig && stsXfmt) {
				ids.add(getPaddedStoreId(storeId, storeName));
			}
		}
		LOGGER.info("Filtering of ineligible stores is complete for site "+storeName);
		return ids;
	}
	
	/**
	 * Get 7 digit store-id as expected by IAS and other places in search
	 * @param storeId
	 * @param storeName
	 * @return
	 */
	private String getPaddedStoreId(String storeId, String storeName) {
		if(!StringUtils.equalsIgnoreCase(storeName, Stores.KMART.name())) {
			return storeId;
		}
        return Strings.padStart(storeId, KMART_STORE_ID_LENGTH, '0');
    }
	private List<String> filterOutMktPlcStores(List<String> storeIds) {
		LOGGER.info("Filtering out marketplace store-ids.");
		Iterator<String> listIterator = storeIds.iterator();
		while (listIterator.hasNext()) {
			String storeId = listIterator.next();
			if (StringUtils.contains(storeId, '-')) {
				listIterator.remove();
			}
		}
		return storeIds;
	}

	public Set<String> getSearsStsEligibleStores() {
		return searsStsEligibleStores;
	}


	public Set<String> getKmartStsEligibleStores() {
		return kmartStsEligibleStores;
	}
	
	public Set<String> getAllShippedToStores() {
		Set<String> allStsStores = new HashSet<>();
		allStsStores.addAll(searsStsEligibleStores);
		allStsStores.addAll(kmartStsEligibleStores);
		return allStsStores;
	}
	
	public Set<String> getCrossFormatShippedToStores(String facilityId) {
		String crossFormatStoreType = OnlineWarehouse.getCrossFormatStoreType(facilityId);
		if(StringUtils.equalsIgnoreCase("kmart", crossFormatStoreType)) {
			return Collections.unmodifiableSet(kmartStsEligibleStores);
		} else if(StringUtils.equalsIgnoreCase("sears", crossFormatStoreType)) {
			return Collections.unmodifiableSet(searsStsEligibleStores);
		}
		return new HashSet<>(); 
	}

}
