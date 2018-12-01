/**
 *
 */
package com.shc.ecom.search.common.vo.buybox;

import java.io.Serializable;

/**
 * @author djohn0
 */
public class RankMeta implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5808062853336856098L;

    private Long timestamp;
    private Integer groups_found;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getGroups_found() {
        return groups_found;
    }

    public void setGroups_found(Integer groups_found) {
        this.groups_found = groups_found;
    }

}
