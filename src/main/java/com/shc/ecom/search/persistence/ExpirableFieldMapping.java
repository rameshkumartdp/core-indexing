package com.shc.ecom.search.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.shc.ecom.search.common.expirablefields.ExpirableFieldDetail;

/**
 * The mapping of service and expirable field detail is stored here.
 * @author vsingh8
 */

@Component
public class ExpirableFieldMapping extends FileDataAccessor {

	private static Map<String,List<ExpirableFieldDetail>> extractWiseFieldMap;

	@Override
	public void save(String value) {
		if (StringUtils.equalsIgnoreCase(value, "EOF")) {
			return;
		}
		addExpirableFieldIntoList(value);
	}

	/** 
	 * @param singleLine
	 */
	private static void addExpirableFieldIntoList(String singleLine) {
		if(extractWiseFieldMap == null){
			extractWiseFieldMap = new HashMap<>();
		}
		
		// Skip lines which are empty or null
		if (StringUtils.isEmpty(singleLine)) {
			return;
		}
		// Skip commented lines starting with '#'
		if (StringUtils.startsWith(singleLine, "#")) {
			return;
		}
		String[] parts = singleLine.split(",");
		// Skip if Mapping is invalid
		if (isInvalidExpirableField(parts)) {
			return;
		}
		// Add values to pidInfoList
		ExpirableFieldDetail expirableField = new ExpirableFieldDetail();
		String extractType = parts[0].trim();
		expirableField.setFieldHierarchy(parts[1].trim());
		expirableField.setExpirableFieldName(parts[2].trim());
		expirableField.setStartDate(parts[3].trim());
		expirableField.setEndDate(parts[4].trim());
		addToMap(extractType , expirableField);
	}
	
	private static void addToMap(String extractType , ExpirableFieldDetail singleFieldInfo){
		List<ExpirableFieldDetail> currentList;
		
		if(extractWiseFieldMap.containsKey(extractType)){
			currentList = extractWiseFieldMap.get(extractType);
			currentList.add(singleFieldInfo);	
		}else{
			currentList = new ArrayList<>();
			currentList.add(singleFieldInfo);
		}
		extractWiseFieldMap.put(extractType, currentList);
	}

	/**
	 * Check if the pid being added is valid pid.
	 * 
	 * @param parts
	 * @return true if line is invalid
	 */
	private static boolean isInvalidExpirableField(String[] parts) {

		// Skip lines without 5 comma separated values
		if (parts.length != 5) {
			return true;
		}
		// Skip line if Extract is invalid.
		if (StringUtils.isEmpty(parts[0])) {
			return true;
		}
		// Skip line if hierarchy is invalid.
		if (StringUtils.isEmpty(parts[1])) {
			return true;
		}
		// Skip line if field name is invalid.
		if (StringUtils.isEmpty(parts[2])) {
			return true;
		}
		
		return isInvalidDate(parts[3],parts[4]);
		   
	}
	
	/**
	 * Return true is startdate , enddate is invalid.
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private static boolean isInvalidDate(String startDate, String endDate){
	   // Skip line if start date is invalid.
       if (StringUtils.isEmpty(startDate)) {
           return true;
       }
       // Skip line if enddate is invalid.
       if (StringUtils.isEmpty(endDate)) {
           return true;
       }
       return false;
	}

	/**
	 * get list of expirable fields for perticular extract type.
	 * @param extractName
	 * @return
	 */
	public static List<ExpirableFieldDetail> getExpirableFieldforExtract(String extractName) {
		if(extractWiseFieldMap == null){
			return new ArrayList<>();
		}
		if(extractWiseFieldMap.isEmpty()){
			return new ArrayList<>();
		}
		if(extractWiseFieldMap.containsKey(extractName)){
			return extractWiseFieldMap.get(extractName);
		} else {
			return new ArrayList<>();
		}
	}
}