package com.shc.ecom.search.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import com.shc.ecom.search.common.constants.DateFormat;
import com.shc.ecom.search.common.expirablefields.ExpirableSolrField;

/**
 * Utility class to handle expirable fields.
 * 
 * @author vsingh8
 *
 */
public class ExpirableFieldsUtil {
	/**
	 * Hiding public constructor
	 */
	private ExpirableFieldsUtil() {
	}

	/**
	 * @param expirableField
	 * @return true if field is valid at the date and time of indexing. For tags
	 *         without actDate, by default current date is used as actDate. For
	 *         tags without expDate some future date is used as expDate while
	 *         creating document.
	 */
	public static boolean isValidExpirableField(ExpirableSolrField expirableField) {

		LocalDateTime startDateTime;
		LocalDateTime endDateTime;

		if (isInValidName(expirableField.getFieldType(), expirableField.getFieldValue())) {
			return false;
		}
		if (isInValidDate(expirableField.getStartDate(), expirableField.getEndDate())) {
			return false;
		}

		String startDateStd = DateUtil.convertToStandardDateTime(expirableField.getStartDate(), false);
		startDateTime = LocalDateTime.parse(startDateStd,
				DateTimeFormat.forPattern(DateFormat.LONGDATE.getDateFormat()));

		String endDateStd = DateUtil.convertToStandardDateTime(expirableField.getEndDate(), true);
		endDateTime = LocalDateTime.parse(endDateStd, DateTimeFormat.forPattern(DateFormat.LONGDATE.getDateFormat()));

		return compareDatesWithCurrentDateTime(startDateTime, endDateTime);

	}
	
	/**
	 * Returns the standard name of field removing special characters with underscore.
	 * @param name
	 * @return
	 */
	public static String getStandardFieldName(String name){
		return name.replaceAll("[^A-Za-z0-9_]", "_").replaceAll("[_]+", "_");
	}

	/**
	 * Checks if the field name and type is invalid.
	 * @param fieldType
	 * @param fieldValue
	 * @return
	 */
	private static boolean isInValidName(String fieldType, String fieldValue) {
		if (StringUtils.isEmpty(fieldType)) {
			return true;
		}
		if (StringUtils.isEmpty(fieldValue)) {
			return true;
		}
		return false;
	}

	/**
	 * checks if the dates are invalid.
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private static boolean isInValidDate(String startDate, String endDate) {
		if (StringUtils.isEmpty(startDate)) {
			return true;
		}
		if (StringUtils.isEmpty(endDate)) {
			return true;
		}
		return false;
	}

	/**
	 * compares the start date and end date of expirable field. The fields which are going to be valid after 3 days are also 
	 * indexed. The fields which are expired are not indexed.
	 * @param startDateTime
	 * @param endDateTime
	 * @return
	 */
	private static boolean compareDatesWithCurrentDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		if (currentDateTime.compareTo(startDateTime) >= 0 && //
				currentDateTime.compareTo(endDateTime) < 0 && //
				endDateTime.compareTo(startDateTime) > 0) {
			return true;
		}

		LocalDateTime futureStartDate = startDateTime.minusDays(3);
		
		if (currentDateTime.compareTo(futureStartDate) >= 0 && //
				currentDateTime.compareTo(endDateTime) < 0 && //
				endDateTime.compareTo(startDateTime) > 0) {
			return true;
		}
		return false;
	}

}
