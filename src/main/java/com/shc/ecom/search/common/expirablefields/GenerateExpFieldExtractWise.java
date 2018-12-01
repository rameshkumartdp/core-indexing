package com.shc.ecom.search.common.expirablefields;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shc.ecom.search.persistence.ExpirableFieldMapping;
import com.shc.ecom.search.persistence.ServiceMapping;


/**
 *
 * @author vsingh8
 *
 */
@Component
public class GenerateExpFieldExtractWise implements Serializable {

	private static final long serialVersionUID = 5001255577790954378L;
	private static final Logger LOGGER = LoggerFactory.getLogger(GenerateExpFieldExtractWise.class.getName());
	/**
	 *
	 * @param extractName
	 * @param dom
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	public List<ExpirableSolrField> getExpirableFieldsMapping(String extractName , Object dom){
		String serviceName = ServiceMapping.getServiceName(extractName);
		List<ExpirableSolrField> expirableFields = new ArrayList<>();
		List<ExpirableFieldDetail> fieldForPerticularExtract = ExpirableFieldMapping.getExpirableFieldforExtract(serviceName);
		if(fieldForPerticularExtract.isEmpty() || dom == null || dom instanceof Map){
			return expirableFields;
		}
		if(dom instanceof Collection<?>){
			Iterator itr = ((Collection) dom).iterator();
			while (itr.hasNext()) {
				Object singleBlob = itr.next();
				expirableFields.addAll(getExpirableFieldsForSingleBlob(singleBlob, fieldForPerticularExtract));
			}
		} else {
			expirableFields.addAll(getExpirableFieldsForSingleBlob(dom, fieldForPerticularExtract));
		}
		return expirableFields;
	}


	private List<ExpirableSolrField> getExpirableFieldsForSingleBlob(Object dom ,List<ExpirableFieldDetail> fieldForParticularExtract){

		Gson gson = new Gson();
		List<ExpirableSolrField> expirableFields = new ArrayList<>();
		String domString = StringUtils.EMPTY;
		try {
			domString = gson.toJson(dom);
		} catch (Exception e) {
			LOGGER.error("Expirable Error: conversion error", e);
		}
		for(ExpirableFieldDetail singleField: fieldForParticularExtract){
			expirableFields.addAll(getExpirableFieldSingle( singleField , domString));
		}
		return expirableFields;
	}

	private List<ExpirableSolrField> getExpirableFieldSingle(ExpirableFieldDetail fieldDetail , Object dom){
		GetExpirableFields getExpirableFields = new GetExpirableFields(fieldDetail.getExpirableFieldName(), fieldDetail.getStartDate(), fieldDetail.getEndDate(),fieldDetail.getFieldHierarchy());
		return getExpirableFields.extract(dom);
	}
}
