package com.shc.ecom.search.extract.extracts;

import com.shc.common.index.rules.Decision;
import com.shc.ecom.search.common.messages.ContextMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgulati on 7/26/16.
 */
@Component
public class WorkingDocumentUtility implements Serializable {

    public WorkingDocument prepareWorkingDocument(ContextMessage context) {
        // Create new working document for each PID/SSIN.
        WorkingDocument wd = new WorkingDocument();
        Extracts extracts = new Extracts();
        Decision decision = new Decision();
        extracts.setSsin(context.getPid());

        if (StringUtils.isNotEmpty(context.getOfferId())) {
            List<String> offerIds = new ArrayList<>();
            offerIds.add(context.getOfferId());
            extracts.setOfferIds(offerIds);
        }

        wd.setExtracts(extracts);
        wd.setDecision(decision);
        return wd;
    }
}
