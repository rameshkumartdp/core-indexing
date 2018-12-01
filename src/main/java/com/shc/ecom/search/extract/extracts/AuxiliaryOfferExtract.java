/**
 * 
 */
package com.shc.ecom.search.extract.extracts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rgopala
 *
 */
public class AuxiliaryOfferExtract implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5269020259859027863L;
	
	private List<String> groupedOffersKSNs = new ArrayList<>();

	public List<String> getGroupedOffersKSNs() {
		return groupedOffersKSNs;
	}

	public void setGroupedOffersKSNs(List<String> groupedOffersKSNs) {
		this.groupedOffersKSNs = groupedOffersKSNs;
	}

}
