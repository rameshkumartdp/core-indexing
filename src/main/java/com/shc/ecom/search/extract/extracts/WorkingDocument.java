package com.shc.ecom.search.extract.extracts;

import com.shc.common.index.rules.Decision;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rgopala
 *         Jul 27, 2015 search-doc-builder
 */
@Component
@Scope("prototype")
public class WorkingDocument implements Serializable {

    private static final long serialVersionUID = -8095603549173268538L;

    private Decision decision;

    private Extracts extracts;

    private List<WorkingDocument> subWorkingDocumentList;

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public Extracts getExtracts() {
        return extracts;
    }

    public void setExtracts(Extracts extracts) {
        this.extracts = extracts;
    }

    public List<WorkingDocument> getSubWorkingDocumentList() {
        return subWorkingDocumentList.isEmpty() ? new ArrayList<WorkingDocument>() : subWorkingDocumentList;
    }

    public void setSubWorkingDocumentList(List<WorkingDocument> subWorkingDocumentList) {
        this.subWorkingDocumentList = subWorkingDocumentList;
    }

}
