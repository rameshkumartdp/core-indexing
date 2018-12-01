package com.shc.ecom.search.extract.extracts;

import java.io.Serializable;

/**
 * Created by tgulati on 7/22/16.
 */
public class BCOProducts implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7892391680045358365L;

	private String rank;

    private String id;

    private String catentrySubType;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatentrySubType() {
        return catentrySubType;
    }

    public void setCatentrySubType(String catentrySubType) {
        this.catentrySubType = catentrySubType;
    }
}
