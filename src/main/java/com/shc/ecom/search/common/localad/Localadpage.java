package com.shc.ecom.search.common.localad;

import java.io.Serializable;
import java.util.List;

public class Localadpage implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -796977588011155897L;
	private List<LocalAdsGBElement> elements;

    public List<LocalAdsGBElement> getElements() {
        return elements;
    }

    public void setElements(List<LocalAdsGBElement> elements) {
        this.elements = elements;
    }
}
