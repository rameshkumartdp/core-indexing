package com.shc.ecom.search.extract.components.localad;

import com.shc.ecom.search.common.localad.LocalAd;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.persistence.ProductLocalAd;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LocalAdExtractComponent extends ExtractComponent<List<String>> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalAdExtractComponent.class.getName());

    @Autowired
    private ProductLocalAd productLocalAd;

    @Override
    protected List<String> get(WorkingDocument wd, ContextMessage context) {
        List<String> localAdIds = new ArrayList<String>();
        LocalAd localAd = productLocalAd.getLocalAdIds().get(wd.getExtracts().getSsin());
        if (localAd != null) {
            ConcurrentHashSet<String> localAdSet = localAd.getLocalAdIds();
            for (String singleLocalAdId : localAdSet) {
                localAdIds.add(singleLocalAdId);
            }
        }
        return localAdIds;

    }

    @Override
    protected Extracts extract(List<String> source, WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();
        if (source != null) {
            extracts.getLocalAdExtract().setLocalAdList(source);
        }
        return extracts;
    }

}
