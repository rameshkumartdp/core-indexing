package com.shc.ecom.search.common.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum BucketFilters {
    SEARS(Arrays.asList("Sears", "Kmart", "UVD"), Arrays.asList("B", "C", "O")),
    KMART(Arrays.asList("Sears", "Kmart"), Arrays.asList("B", "C", "O")),
    AUTO(Arrays.asList("Sears", "Kmart", "UVD"), Arrays.asList("B", "C", "O")),
    FBM(Arrays.asList("FBM", "FBS", "DSS"), Collections.<String>emptyList()),
    CPC(Arrays.asList("CPC"), Collections.<String>emptyList()),
    MYGOFER(Arrays.asList("FBM", "FBS", "DSS", "Sears", "Kmart"), Collections.<String>emptyList()),
    SEARSPR(Arrays.asList("Sears", "Kmart"), Arrays.asList("B", "C", "O")),
    COMMERCIAL(Arrays.asList("Sears"), Arrays.asList("B", "C", "O")),
    NOFILTERS(Collections.<String>emptyList(), Collections.<String>emptyList());

    private final List<String> pgrmTypeFilters;
    private final List<String> catentrySubTypeFilters;

    private BucketFilters(List<String> pgrmTypeFilters, List<String> catentrySubTypeFilters) {
        this.pgrmTypeFilters = pgrmTypeFilters;
        this.catentrySubTypeFilters = catentrySubTypeFilters;
    }

    public List<String> getPgrmTypeFilters() {
        return Collections.unmodifiableList(pgrmTypeFilters);
    }

    public List<String> getCatentrySubTypeFilters() {
        return Collections.unmodifiableList(catentrySubTypeFilters);
    }

}
