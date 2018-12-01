package com.shc.ecom.search.common.level3CatData;

import java.util.List;

public class Level3CatDataDto {
    private String sequence;
    private List<SubcatAttributes> subcatAttributes;
    private List<RelatedLinks> relatedLinks;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public List<SubcatAttributes> getSubcatAttributes() {
        return subcatAttributes;
    }

    public void setSubcatAttributes(List<SubcatAttributes> subcatAttributes) {
        this.subcatAttributes = subcatAttributes;
    }

    public List<RelatedLinks> getRelatedLinks() {
        return relatedLinks;
    }

    public void setRelatedLinks(List<RelatedLinks> relatedLinks) {
        this.relatedLinks = relatedLinks;
    }

}
