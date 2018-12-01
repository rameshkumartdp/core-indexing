package com.shc.ecom.search.common.uas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vsonthy on 3/13/17.
 */
public class UasDtoOfferChannels implements Serializable {

    private static final long serialVersionUID = -2526419720123906110L;

    private UasDto uasDto;
    private Map<String, String> viewOnlyoffers;

    public UasDto getUasDto() {
        if(uasDto == null){
            return new UasDto();
        }
        return uasDto;
    }

    public void setUasDto(UasDto uasDto) {
        this.uasDto = uasDto;
    }

    public Map<String, String> getViewOnlyoffers() {
        if(viewOnlyoffers == null){
            return new HashMap<>();
        }
        return viewOnlyoffers;
    }

    public void setViewOnlyoffers(Map<String, String> viewOnlyoffers) {
        this.viewOnlyoffers = viewOnlyoffers;
    }
}
