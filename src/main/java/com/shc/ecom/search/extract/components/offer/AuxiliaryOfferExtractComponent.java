/**
 * 
 */
package com.shc.ecom.search.extract.components.offer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.search.common.exception.ErrorCode;
import com.shc.ecom.search.common.greenbox.GBServiceFacade;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;

/**
 * @author rgopala
 *
 *This component makes additional offer calls in which we are interested only in certain cases like indexing offer-ids of other offers in non-variant group.
 */
public class AuxiliaryOfferExtractComponent extends ExtractComponent<List<Offer>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5511337039242238017L;

	private static final Logger LOGGER = LoggerFactory.getLogger(OfferExtractComponent.class);

	@Autowired
	private GBServiceFacade gbServiceFacade;

	@Override
	protected List<Offer> get(WorkingDocument wd, ContextMessage context) {
		return getOfferDocuments(wd.getExtracts().getBuyboxExtract().getKmartOrSearsOfferIds());
	}

	@Override
	protected Extracts extract(List<Offer> offers, WorkingDocument wd, ContextMessage context) {
		Extracts extracts = wd.getExtracts();
		extracts.getAuxiliaryOfferExtract().setGroupedOffersKSNs(getKsn(offers));
		return extracts;
	}

	private List<Offer> getOfferDocuments(Set<String> offerIds) {
		List<Offer> offerDocs = null;
		try {
			offerDocs = gbServiceFacade.getOfferDocsList(new ArrayList<>(offerIds));
		} catch (Exception e) {
			LOGGER.info(ErrorCode.GB_OFFER_UNAVAILABLE.name() + e.getMessage());
		}
		if (offerDocs == null) { // Create an empty list
			offerDocs = new ArrayList<Offer>();
		}
		return offerDocs;
	}

	private List<String> getKsn(List<Offer> offers) {
		List<String> ksns = new ArrayList<>();

		for (Offer offer : offers) {
			if (StringUtils.isNotEmpty(offer.getAltIds().getKsn())) {
				ksns.add(offer.getAltIds().getKsn());
			}
		}
		return ksns;
	}
}
