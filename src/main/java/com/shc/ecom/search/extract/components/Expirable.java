package com.shc.ecom.search.extract.components;

import java.util.List;

import com.shc.ecom.search.common.expirablefields.ExpirableSolrField;
import com.shc.ecom.search.common.expirablefields.GenerateExpFieldExtractWise;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
/**
 * Expirable fields are extracted in this interface as default method,
 * override this default method in extract component where the source 
 * is map type or some business logic is required before extracting expirable 
 * field. See promoExtractComponent overridden implementation.
 * @author vsingh8
 *
 * @param <T>
 */
public interface Expirable<T> {
	/**
	 * 
	 * @param source
	 * @param wd
	 * @param context
	 * @return
	 */
	public default Extracts extractExpirableFields(T source, WorkingDocument wd) {
		Extracts extracts = wd.getExtracts();
		GenerateExpFieldExtractWise generateExpirableField = new GenerateExpFieldExtractWise();
		List<ExpirableSolrField> expireableFields = generateExpirableField
				.getExpirableFieldsMapping(this.getClass().getSimpleName(), source);
		extracts.addExpirableFieldList(expireableFields);
		return extracts;
	}
}
