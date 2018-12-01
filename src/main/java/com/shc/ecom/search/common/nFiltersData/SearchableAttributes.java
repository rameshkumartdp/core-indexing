package com.shc.ecom.search.common.nFiltersData;

import java.util.List;

public class SearchableAttributes {
    private String name;
    private String displayName;
    private int sequence;
    private String sortBy;
    private String renderType;
    private List<Values> values;
    private List<OmnitureValues> omnitureValues;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getRenderType() {
        return renderType;
    }

    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }

    public List<Values> getValues() {
        return values;
    }

    public void setValues(List<Values> values) {
        this.values = values;
    }

    public List<OmnitureValues> getOmnitureValues() {
        return omnitureValues;
    }

    public void setOmnitureValues(List<OmnitureValues> omnitureValues) {
        this.omnitureValues = omnitureValues;
    }

}
