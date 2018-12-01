package com.shc.ecom.search.extract.extracts;

import java.io.Serializable;
import java.util.List;

public class LocalAdExtract implements Serializable {

    private List<String> localAdList;

    public List<String> getLocalAdList() {
        return localAdList;
    }

    public void setLocalAdList(List<String> localAdList) {
        this.localAdList = localAdList;
    }
}
