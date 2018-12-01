package com.shc.ecom.search.common.pidmapping;

/**
 * This PidInfo class is used as Bean for mapping with 1 Json node, after reading the data from datafile in the form of Json , we
 * convert the json node to PidInfo Bean for further usage
 */
public class PidInfo {

    /**
     * OldPid is nothing but the Ssin of the Old product which will be given by merchant
     */
    private String oldPid;

    /**
     * OldPid is nothing but the Ssin of the new product which will be given by merchant
     */
    private String newPid;

    /**
     * expiryDate: till the date the BSO Data should be Inherited
     */
    private String expDate;

    public String getOldPid() {
        return oldPid;
    }

    public void setOldPid(String oldPid) {
        this.oldPid = oldPid;
    }

    public String getNewPid() {
        return newPid;
    }

    public void setNewPid(String newPid) {
        this.newPid = newPid;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    @Override
    public String toString() {
        return "PidInfo [oldPid=" + oldPid + ", newPid=" + newPid + ", expDate=" + expDate + "]";
    }

}
