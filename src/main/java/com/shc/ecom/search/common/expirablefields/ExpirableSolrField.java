package com.shc.ecom.search.common.expirablefields;

import java.io.Serializable;

/**
 * The details of expirable field which will be added in solr for indexing.
 * 
 * @author vsingh8
 *
 */
public class ExpirableSolrField implements Serializable {

	private static final long serialVersionUID = 890521468447411705L;

	public static final String STARTDATECONST = "StartDate";
	public static final String ENDDATECONST = "EndDate";
	private String startDate;
	private String endDate;
	private String fieldType;
	private String fieldValue;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

}
