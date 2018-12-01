package com.shc.ecom.search.common.prodstats;

import java.util.ArrayList;
import java.util.List;

public class Response {
    private String start;

    private List<Docs> docs = new ArrayList<>();

    private int numFound;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public List<Docs> getDocs() {
        return docs;
    }

    public void setDocs(List<Docs> docs) {
        this.docs = docs;
    }

    public int getNumFound() {
        return numFound;
    }

    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    @Override
    public String toString() {
        return "ClassPojo [start = " + start + ", docs = " + docs + ", numFound = " + numFound + "]";
    }
}