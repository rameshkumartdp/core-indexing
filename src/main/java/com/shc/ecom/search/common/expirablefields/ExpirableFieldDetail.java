package com.shc.ecom.search.common.expirablefields;

/**
 * Expirable field details are stored here.
 * @author vsingh8
 */
public class ExpirableFieldDetail {
	
	/**
	 * Field name with which expirable field will be stored.
	 */
	private String expirableFieldName;
	/**
	 * Start date time of expirable field.
	 */
	private String startDateTime;
	/**
	 * End date time of expirable field.
	 */
	private String endDateTime;
	/**
	 * Hierarchy in response where expirable fields are present.
	 */
	private String fieldHierarchy;
	
	public String getExpirableFieldName() {
		return expirableFieldName;
	}
	public void setExpirableFieldName(String expirableFieldName) {
		this.expirableFieldName = expirableFieldName;
	}
	public String getStartDate() {
		return startDateTime;
	}
	public void setStartDate(String startDate) {
		this.startDateTime = startDate;
	}
	public String getEndDate() {
		return endDateTime;
	}
	public void setEndDate(String endDate) {
		this.endDateTime = endDate;
	}
	public String getFieldHierarchy() {
		return fieldHierarchy;
	}
	public void setFieldHierarchy(String  fieldHierarchy) {
		this.fieldHierarchy = fieldHierarchy;
	}
}
