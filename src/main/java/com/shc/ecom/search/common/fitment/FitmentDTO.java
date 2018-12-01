package com.shc.ecom.search.common.fitment;

public class FitmentDTO {
    private String status;

    private String requestId;

    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Data getData() {
        if (data == null) {
            data = new Data();
        }
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ClassPojo [status = " + status + ", requestId = " + requestId + ", data = " + data + "]";
    }
}