package com.shc.ecom.search.common.behavioral;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pchauha
 */
public class Response implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 9218287763320799483L;

    private int numFound;
    private List<Docs> docs;

    public int getNumFound() {
        return numFound;
    }

    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    public List<Docs> getDocs() {
        if (docs == null) {
            docs = new ArrayList<>();
        }
        return docs;
    }

    public void setDocs(List<Docs> docs) {
        this.docs = docs;
    }

}
