package com.shc.ecom.search.common.behavioral;

import java.io.Serializable;

/**
 * @author pchauha
 */
public class BehavioralDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1724043457123217433L;

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

}
