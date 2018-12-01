package com.shc.ecom.search.persistence;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * The Mapping of Extract Name and service used in that extract is stored here.
 * @author vsingh8
 */

@Component
public class ServiceMapping extends FileDataAccessor implements Serializable {

	private static final long serialVersionUID = 8636182264566663954L;

	private static Map<String, String> extractServiceMap;

	@Override
	public void save(String value) {
		if (StringUtils.equalsIgnoreCase(value, "EOF")) {
			return;
		}
		addServiceExtractMapIntoList(value);
	}

	/**
	 * @param singleLine
	 */
	private static void addServiceExtractMapIntoList(String singleLine) {
		if (extractServiceMap == null) {
			extractServiceMap = new HashMap<>();
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
		if (isInvalidServiceMapping(parts)) {
			return;
		}
		// Add values to pidInfoList

		String extractName = parts[0].trim();
		String serviceName = parts[1].trim();
		addToMap(extractName, serviceName);
	}

	private static void addToMap(String extractName, String serviceName) {

		extractServiceMap.put(extractName, serviceName);
	}

	/**
	 * @param parts
	 * @return true if line is invalid
	 */
	private static boolean isInvalidServiceMapping(String[] parts) {

		// Skip lines without 2 comma separated values
		if (parts.length != 2) {
			return true;
		}
		// Skip line if Service is invalid.
		if (StringUtils.isEmpty(parts[0])) {
			return true;
		}
		// Skip line if Extract is invalid.
		if (StringUtils.isEmpty(parts[1])) {
			return true;
		}
		return false;
	}

	/**
	 * get list of service for perticular extract .
	 * 
	 * @param extractName
	 * @return
	 */
	public static String getServiceName(String extract) {
		if (extractServiceMap == null) {
			return "";
		}
		if (extractServiceMap.isEmpty()) {
			return "";
		}
		if (extractServiceMap.containsKey(extract)) {
			return extractServiceMap.get(extract);
		} else {
			return "";
		}
	}
}