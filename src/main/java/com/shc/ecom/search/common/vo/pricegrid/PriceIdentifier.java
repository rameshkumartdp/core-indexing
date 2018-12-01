/**
 *
 */
package com.shc.ecom.search.common.vo.pricegrid;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author djohn0
 */
public class PriceIdentifier implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8523690885947007221L;


    private String pid;

    @JsonProperty("zip-code")
    private int zipCde;

    @JsonProperty("pid-type")
    private int pidType;


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getZipCde() {
        return zipCde;
    }

    public void setZipCde(int zipCde) {
        this.zipCde = zipCde;
    }

    public int getPidType() {
        return pidType;
    }

    public void setPidType(int pidType) {
        this.pidType = pidType;
    }

}
