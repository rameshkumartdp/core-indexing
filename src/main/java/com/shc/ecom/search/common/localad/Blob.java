package com.shc.ecom.search.common.localad;

import java.io.Serializable;

public class Blob implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4258272401708012016L;
	private Localadpage localadpage;

    public Localadpage getLocaladpage() {
        return localadpage;
    }

    public void setLocaladpage(Localadpage localadpage) {
        this.localadpage = localadpage;
    }
}
