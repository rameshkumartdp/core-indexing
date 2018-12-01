/**
 *
 */
package com.shc.ecom.search.common.vo.buybox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author djohn0
 */
public class BuyBoxDomain implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8977730852151164122L;

    private RankMeta metadata;
    private List<RankGrps> groups;

    public RankMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(RankMeta metadata) {
        this.metadata = metadata;
    }

    public List<RankGrps> getGroups() {
        if (groups == null) {
            groups = new ArrayList<>();
        }
        return groups;
    }

    public void setGroups(List<RankGrps> groups) {
        this.groups = groups;
    }

}
