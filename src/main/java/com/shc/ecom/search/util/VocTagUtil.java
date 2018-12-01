package com.shc.ecom.search.util;

import com.shc.ecom.gb.doc.offer.VocTags;
import com.shc.ecom.search.common.constants.DateFormat;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Utility class for methods related to vocTags and geoSpotTags
 *
 * @author vsingh8
 */
public final class VocTagUtil {

    /**
     * Hiding public constructor
     */
    private VocTagUtil() {
    }

    /**
     * @param vocTag
     * @return true if tag is valid at the date and time of indexing. For tags
     * without actDate, by default current date is used as actDate. For
     * tags without expDate some future date is used as expDate while
     * creating document.
     */
    public static boolean isValidTagName(VocTags vocTag) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime actDateTime;
        LocalDateTime expDateTime;
        if (StringUtils.isEmpty(vocTag.getName())) {
        	return false;
        }
        
        if (StringUtils.isNotEmpty(vocTag.getActDt())) {
            String actionDate = DateUtil.convertToStandardDateTime(vocTag.getActDt(), false);
            actDateTime = LocalDateTime.parse(actionDate,
                    DateTimeFormat.forPattern(DateFormat.LONGDATE.getDateFormat()));
        } else {
            actDateTime = currentDateTime;
        }
        if (StringUtils.isNotEmpty(vocTag.getExpDt())) {
            String expireDate = DateUtil.convertToStandardDateTime(vocTag.getExpDt(), true);
            expDateTime = LocalDateTime.parse(expireDate,
                    DateTimeFormat.forPattern(DateFormat.LONGDATE.getDateFormat()));
        } else {
            expDateTime = LocalDateTime.parse(DateFormat.FUTUREDATE.getDateFormat(),
                    DateTimeFormat.forPattern(DateFormat.LONGDATE.getDateFormat()));
        }
        if (currentDateTime.compareTo(actDateTime) >= 0 && //
                currentDateTime.compareTo(expDateTime) < 0 && //
                expDateTime.compareTo(actDateTime) > 0) {
            return true;
        }
        return false;
    }

}
