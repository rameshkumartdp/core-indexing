package com.shc.ecom.search.extract.extracts;

import com.shc.ecom.search.common.tm.TopicModelingDTO;

import java.io.Serializable;
import java.util.Map;

public class TMExtract implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = -2466985350024433548L;
	private Map<String, TopicModelingDTO> maps;

    public Map<String, TopicModelingDTO> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, TopicModelingDTO> maps) {
        this.maps = maps;
    }

}
