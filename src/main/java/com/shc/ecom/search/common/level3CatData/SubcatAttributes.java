package com.shc.ecom.search.common.level3CatData;

import java.util.List;

public class SubcatAttributes {
    private String name;
    private String displayName;
    private String glossaryId;
    private List<Values> values;
    private String sequence;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

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

    public String getGlossaryId() {
        return glossaryId;
    }

    public void setGlossaryId(String glossaryId) {
        this.glossaryId = glossaryId;
    }

    public List<Values> getValues() {
        return values;
    }

    public void setValues(List<Values> values) {
        this.values = values;
    }

}
