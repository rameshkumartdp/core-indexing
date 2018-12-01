package com.shc.ecom.search.common.price.bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rgopala
 */
public class Savings {
    private String displayLabel;

    private String displayText;

    private List<String> description;

    private String treatment;

    private String numericValue;

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

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(String numericValue) {
        this.numericValue = numericValue;
    }

    @Override
    public String toString() {
        return "ClassPojo [displayLabel = " + displayLabel + ", displayText = " + displayText + ", description = " + description + ", treatment = " + treatment + ", numericValue = " + numericValue + "]";
    }
}