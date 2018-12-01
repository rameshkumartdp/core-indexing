package com.shc.ecom.search.common.price.bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rgopala
 */
public class Indicators {
    private String displayLabel;

    private String displayText;

    private List<String> description;

    private String name;

    private String exists;

    public String getDisplayLabel() {
        return displayLabel;
    }

    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public List<String> getDescription() {
        if (description == null) {
            description = new ArrayList<>();
        }
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExists() {
        return exists;
    }

    public void setExists(String exists) {
        this.exists = exists;
    }

    @Override
    public String toString() {
        return "ClassPojo [displayLabel = " + displayLabel + ", displayText = " + displayText + ", description = " + description + ", name = " + name + ", exists = " + exists + "]";
    }
}
