package com.shc.ecom.search.common.behavioral;

import java.io.Serializable;
import java.util.List;

public class Docs implements Serializable {

    private static final long serialVersionUID = -6503275907452608449L;

    private String id;
    private List<String> behavioral;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getBehavioral() {
        return behavioral;
    }

    public void setBehavioral(List<String> behavioral) {
        this.behavioral = behavioral;
    }
}