package com.shc.ecom.search.common.constants;

import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.util.ListUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public enum Sites {
    // TODO: Need to get the correct catalog id for pr's and mg
    SEARS("sears", "12605", DapSites.SEARSDAP),
    SEARSPR("puertorico", "26151", DapSites.SEARSPRDAP),
    KMART("kmart", "10104", DapSites.KMARTDAP),
    MYGOFER("mygofer", "27151", DapSites.MYGOFERDAP),
    KENMORE("kenmore", "12604", DapSites.KENMOREDAP),
    CRAFTSMAN("craftsman", "12602", DapSites.CRAFTSMANDAP),
    COMMERCIAL("scom", "12606", DapSites.SEARSDAP);

    private String catalogId;
    private String siteName;
    private DapSites dapSite;

    private Sites(String siteName, String catalogId, DapSites dapSite) {
        this.siteName = siteName;
        this.catalogId = catalogId;
        this.dapSite = dapSite;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public String getSiteName() {
        return siteName;
    }

    public DapSites getDapSite() {
        return dapSite;
    }

    public Sites getCrossFormatSite() {
        if (this == SEARS) {
            return KMART;
        } else if (this == KMART) {
            return SEARS;
        }
        return this;
    }

    public boolean isCrossFormat(WorkingDocument wd, ContextMessage context) {
        String storeName = context.getStoreName();
        String catentrySubType = wd.getExtracts().getContentExtract().getCatentrySubType();

        List<String> soldBy;
        switch (CatentrySubType.get(catentrySubType)) {
            case BUNDLE:
            case OUTFIT:
            case COLLECTION:
                soldBy = wd.getExtracts().getContentExtract().getSoldBy();
                break;
            default:
                soldBy = wd.getExtracts().getOfferExtract().getSoldBy();
        }

        if (Stores.SEARS.matches(storeName) &&
                ListUtil.containsCaseInsensitive(soldBy, Stores.KMART.toString())) {
            return true;
        } else if (Stores.KMART.matches(storeName) &&
                ListUtil.containsCaseInsensitive(soldBy, Stores.SEARS.toString())) {
            return true;
        }
        return false;
    }

    public boolean matches(String siteName) {
        return StringUtils.equalsIgnoreCase(siteName, this.siteName);
    }
}
