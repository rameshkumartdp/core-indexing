package com.shc.ecom.search.extract.components.tm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.tm.TopicModelingDTO;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.transformations.CommonTransformationLogic;

/**
 *
 */

@Component
public class TopicModelingExtractComponent extends ExtractComponent<Map<String, TopicModelingDTO>> implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1169084442903985965L;

	private static final Logger LOGGER = Logger.getLogger(TopicModelingExtractComponent.class.getName());

    @Autowired
    protected TopicModelingService topicModelingService;

    @Autowired
    private CommonTransformationLogic commonTransformationLogic;

    @Override
    protected Map<String, TopicModelingDTO> get(WorkingDocument wd, ContextMessage context) {
        String name = commonTransformationLogic.getNameSearchableFromContentTransformationLogic(wd);

        long timeBeforeInference = System.currentTimeMillis();
        Map<String, TopicModelingDTO> extractDtoForTM = new HashMap<>();
        TopicModelingDTO tmDtoForNameSearchable = topicModelingService.getInferenceAndConvertToDto(name);

        if((System.currentTimeMillis() - timeBeforeInference) > 100){
            LOGGER.info("Time for inference " + name+" in extract "  + (System.currentTimeMillis() - timeBeforeInference));
        }

        if (tmDtoForNameSearchable != null) {
            extractDtoForTM.put(GlobalConstants.NAME_SEARCHABLE, tmDtoForNameSearchable);
        }
        return extractDtoForTM;
    }

    @Override
    protected Extracts extract(Map<String, TopicModelingDTO> source, WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();
        extracts.getTMExtract().setMaps(source);
        return extracts;
    }


}
