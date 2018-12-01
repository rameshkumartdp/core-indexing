package com.shc.ecom.search.common.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author rgopala
 *
 */
public enum OnlineWarehouse {

	DARTSEARS("770", "sears", "kmart", "XDRES", "Sears Item Fulfilled From Dart"),
	DARTKMART("780", "kmart", "sears", "XDRES", "Kmart Item Fulfilled From Dart"),
	CDFCSEARS("470", "sears", "kmart", "XCRES", "Sears Item Fulfilled From CDFC");
	
	private final String facilityId;
	private final String itemType;
	private final String xFmtType;
	private final String flag;
	private final String description;

	OnlineWarehouse(String facilityId, String itemType, String xFmtType, String flag, String description) {
		this.facilityId = facilityId;
		this.itemType = itemType;
		this.xFmtType = xFmtType;
		this.flag = flag;
		this.description = description;
	}
	
	public boolean matches(String facilityId) {
		String modId = String.valueOf(Integer.parseInt(facilityId));
		if(StringUtils.equalsIgnoreCase(this.facilityId, modId)) {
			return true;
		}
		return false;
	}
	
	public static boolean matchesAnyFacility(String facilityId) {
		//Remove leading zeros if any
		String modId = String.valueOf(Integer.parseInt(facilityId));
		for(OnlineWarehouse facility : OnlineWarehouse.values()) {
			if(StringUtils.equalsIgnoreCase(facility.facilityId, modId)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getCrossFormatStoreType(String facilityId) {
		String modId = String.valueOf(Integer.parseInt(facilityId));
		for(OnlineWarehouse facility : OnlineWarehouse.values()) {
			if(StringUtils.equalsIgnoreCase(facility.facilityId, modId)) {
				if(StringUtils.equalsIgnoreCase(facility.getItemType(), "sears")) {
					return "kmart";
				} else if(StringUtils.equalsIgnoreCase(facility.getItemType(), "kmart")) {
					return "sears";
				}
			}
		}
		return null;
	}

	public String getDescription() {
		return description;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public String getItemType() {
		return itemType;
	}

	public String getFlag() {
		return flag;
	}
	
	public String getxFmtType() {
		return xFmtType;
	}
}
