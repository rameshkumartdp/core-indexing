package com.shc.ecom.search.classifications;

import com.shc.ecom.search.common.constants.CatentrySubType;
import com.shc.ecom.search.common.exception.ErrorCode;
import com.shc.ecom.search.common.exception.SearchCommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author rgopala
 */
@Component
public class ClassificationFactory implements Serializable {

    private static final long serialVersionUID = -3062483788518375572L;

    @Autowired
    private NonVariation nonVariation;

    @Autowired
    private Variation variation;

    @Autowired
    private Bundle bundle;

    @Autowired
    private Collection collection;

    public Classification classify(String type) throws SearchCommonException {

        switch (CatentrySubType.get(type)) {
            case NON_VARIATION:
                return nonVariation;
            case VARIATION:
                return variation;
            case BUNDLE:
            case OUTFIT:
                return bundle;
            case COLLECTION:
                return collection;
            default:
                throw new SearchCommonException(ErrorCode.UNKNOWN_CLASSIFICATION, type);
        }
    }

}
