package com.shc.ecom.search.common.prodstats;

public class Retail {

    private Response response;

    public Response getResponse() {
        if (response == null) {
            response = new Response();
        }
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ClassPojo [response = " + response + "]";
    }
}
