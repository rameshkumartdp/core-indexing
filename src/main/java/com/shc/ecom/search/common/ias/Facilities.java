package com.shc.ecom.search.common.ias;

/**
 * @author rgopala
 */
public class Facilities {
    private String id;

    private boolean resAvailable;

    private Dispositions dispositions;

    private Resv resv;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Dispositions getDispositions() {
        if (this.dispositions == null) {
            this.dispositions = new Dispositions();
        }
        return dispositions;
    }

    public void setDispositions(Dispositions dispositions) {
        this.dispositions = dispositions;
    }

    public Resv getResv() {
        if (this.resv == null) {
            resv = new Resv();
        }
        return resv;
    }

    public void setResv(Resv resv) {
        this.resv = resv;
    }

    public boolean isResAvailable() {
        return resAvailable;
    }

    public void setResAvailable(boolean resAvailable) {
        this.resAvailable = resAvailable;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", dispositions = " + dispositions + "]";
    }
}
