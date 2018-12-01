package com.shc.ecom.search.ssinproducer;

import com.shc.ecom.gb.doc.common.DomainObject;
import com.shc.ecom.search.common.constants.CatentrySubType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * That annexes!
 * Helps in annexing additional stores necessary to the document
 */
@Component
public class Annexer {

    public List<String> getSites(DomainObject domainObject) {
        List<String> typesList = domainObject.get_ft().getCatentrySubType();
        switch (CatentrySubType.findCatentrySubType(typesList)) {
            case BUNDLE:
            case COLLECTION:
                return domainObject.get_blob().getBundle().getSites();
            default:
                return domainObject.get_blob().getContent().getSites();

        }
    }
}
