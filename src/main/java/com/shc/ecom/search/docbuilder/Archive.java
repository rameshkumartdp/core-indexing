package com.shc.ecom.search.docbuilder;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The archive of searchdocuments and processed ids
 * Used for archiving and reusing the searchdocuments; and ids for recovery.
 *
 * @author rgopala
 */
@Component
public class Archive {

    private List<String> ids;

    private List<SearchDoc> searchDocuments;

    public List<String> getIds() {
        if (ids == null) {
            ids = new ArrayList<>();
        }
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public List<SearchDoc> getSearchDocuments() {
        if (searchDocuments == null) {
            searchDocuments = new ArrayList<>();
        }
        return searchDocuments;
    }

    public void setSearchDocuments(List<SearchDoc> searchDocuments) {
        this.searchDocuments = searchDocuments;
    }


}
