package com.shc.ecom.search.common.constants;

public enum DapSites {
    //TODO: Get the DAP catalog IDs for Kmart and searspr
    SEARSDAP("searsDap", "12607"),
    KMARTDAP("kmartDap", ""),
    SEARSPRDAP("puertoricoDap", ""),
    MYGOFERDAP("mygoferDap", "27151"),
    KENMOREDAP("kemoreDap", "14602"),
    CRAFTSMANDAP("craftsmanDap", "14601");

    private String siteName;
    private String catalogId;

    private DapSites(String siteName, String catalogId) {
        this.siteName = siteName;
        this.catalogId = catalogId;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getCatalogId() {
        return catalogId;
    }

}
