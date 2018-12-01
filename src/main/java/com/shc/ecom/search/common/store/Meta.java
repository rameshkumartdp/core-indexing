package com.shc.ecom.search.common.store;

public class Meta {
	private String createdTs;

	private String modifiedTs;

	private String lastModifiedBy;

	private String schemaVer;

	public String getCreatedTs() {
		return createdTs;
	}

	public void setCreatedTs(String createdTs) {
		this.createdTs = createdTs;
	}

	public String getModifiedTs() {
		return modifiedTs;
	}

	public void setModifiedTs(String modifiedTs) {
		this.modifiedTs = modifiedTs;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getSchemaVer() {
		return schemaVer;
	}

	public void setSchemaVer(String schemaVer) {
		this.schemaVer = schemaVer;
	}

	@Override
	public String toString() {
		return "Meta [createdTs = " + createdTs + ", modifiedTs = " + modifiedTs + ", lastModifiedBy = "
				+ lastModifiedBy + ", schemaVer = " + schemaVer + "]";
	}
}