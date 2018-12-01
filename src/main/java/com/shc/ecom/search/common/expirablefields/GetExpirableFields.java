package com.shc.ecom.search.common.expirablefields;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author vsingh8
 *
 */
public class GetExpirableFields {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetExpirableFields.class.getName());
	/**
	 * Maximum depth allowed for recursion method.
	 */
	private static final int MAXRECURSIONDEPTH = 100;
	private List<ExpirableSolrField> expirableFiledValues;
	private String startDateFieldName;
	private String endDateFieldName;
	private String expirableFieldName;
	private String hierarchy;

	/**
	 * 
	 * @param expirableFieldName
	 * @param startDateFieldName
	 * @param endDateFieldName
	 * @param hierarchy
	 */
	public GetExpirableFields(String expirableFieldName, String startDateFieldName, String endDateFieldName,
			String hierarchy) {
		this.expirableFiledValues = new ArrayList<>();
		this.expirableFieldName = expirableFieldName;
		this.endDateFieldName = endDateFieldName;
		this.startDateFieldName = startDateFieldName;
		this.hierarchy = hierarchy;
	}

	/**
	 * 
	 * @param dom
	 * @return
	 */
	public List<ExpirableSolrField> extract(Object dom) {

		JsonParser parser = new JsonParser();
		JsonElement jsonDom = null;
		try {
			jsonDom = parser.parse((String) dom);
		} catch (Exception e) {
			LOGGER.error("Expirable Error: parsing error", e);
		}
		try {
			getFieldsFromDom(hierarchy, jsonDom, 0);
		} catch (Exception e) {
			LOGGER.error("Expirable Error: recurssion error", e);
		}
		return getExpirableFiledValues();
	}

	public List<ExpirableSolrField> getExpirableFiledValues() {
		return expirableFiledValues;
	}

	public void setExpirableFiledValues(List<ExpirableSolrField> expirableFiledValues) {
		this.expirableFiledValues = expirableFiledValues;
	}

	private void getFieldsFromDom(String hiearchy, JsonElement jsonElement, int depth) {

		if (jsonElement == null || depth > MAXRECURSIONDEPTH) {
			return;
		}
		int newDepth = depth + 1;
		String[] levels = hiearchy.split(">");
		String currentLevel = levels[0].trim();
		String remainingHiearchy = "";
		if (hiearchy.contains(">")) {
			remainingHiearchy = hiearchy.substring(hiearchy.indexOf('>') + 1, hiearchy.length()).trim();
		}
		if (hiearchy.isEmpty()) {
			addFieldsFromObjectOrArray(jsonElement);
		}
		if (jsonElement instanceof JsonObject) {
			JsonElement nextObject = jsonElement.getAsJsonObject().get(currentLevel);
			getFieldsFromDom(remainingHiearchy, nextObject, newDepth);
		}
		if (jsonElement instanceof JsonArray) {
			JsonArray nextObjects = jsonElement.getAsJsonArray();
			for (JsonElement singleObject : nextObjects) {
				JsonElement oneObject = singleObject.getAsJsonObject().get(currentLevel);
				getFieldsFromDom(remainingHiearchy, oneObject, newDepth);
			}
		}

	}

	private void addFieldsFromObjectOrArray(JsonElement singleField) {
		if (singleField instanceof JsonObject) {
			addExpirableFieldValue(singleField);
		} else if (singleField instanceof JsonArray) {
			JsonArray listOfExpirable = singleField.getAsJsonArray();
			for (JsonElement singleElement : listOfExpirable) {
				addExpirableFieldValue(singleElement);
			}
		}
	}

	private void addExpirableFieldValue(JsonElement singleField) {
		JsonObject valueNode = singleField.getAsJsonObject();

		String nodeValue = valueNode.has(expirableFieldName) ? valueNode.get(expirableFieldName).getAsString() : "";
		String startDateValue = valueNode.has(startDateFieldName) ? valueNode.get(startDateFieldName).getAsString()
				: "";
		String endDateValue = valueNode.has(endDateFieldName) ? valueNode.get(endDateFieldName).getAsString() : "";

		if (StringUtils.isEmpty(nodeValue) || StringUtils.isEmpty(startDateValue)
				|| StringUtils.isEmpty(endDateValue)) {
			return;
		}
		ExpirableSolrField singleNode = new ExpirableSolrField();
		singleNode.setStartDate(startDateValue);
		singleNode.setEndDate(endDateValue);
		singleNode.setFieldValue(nodeValue);
		singleNode.setFieldType(expirableFieldName);
		expirableFiledValues.add(singleNode);
		return;
	}
}
