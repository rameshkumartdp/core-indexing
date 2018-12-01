/**
 *
 */
package com.shc.ecom.search.persistence;

import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.search.config.GlobalConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author rgopala
 */
@Component
public class Incremental extends FileDataAccessor {

    public static final int TIME_DELTA_IN_HOURS = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.TIME_DELTA_IN_HOURS));
    /**
     *
     */
    private static final long serialVersionUID = 6091247851025225768L;
    private static final String KEY_VALUE_SEPARATOR = "=";
    private static final String START_TIME_TOKEN = "startTime=";
    private static final Logger LOGGER = LoggerFactory.getLogger(Incremental.class);
    private long startTime;

    public long getStartTime() {
        if (startTime == 0) {
            LOGGER.info("Valid start-time for incremental is either unavailable or zero.");
        }
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long subtractDeltaHours(long endTime) {
        LOGGER.info("Subtracting " + TIME_DELTA_IN_HOURS + " hours from end-time " + endTime);
        long endTimeEpoch = endTime * 1000;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(endTimeEpoch);
        calendar.add(Calendar.HOUR, -TIME_DELTA_IN_HOURS);
        long startTime = calendar.getTimeInMillis() / 1000;
        return startTime;
    }

    public String getUTCTime(long epochTime) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long epochTimeInMillis = epochTime * 1000;
        String dateTime = dateFormat.format(epochTimeInMillis);
        String[] splitDateTime = dateTime.split(" ");
        String dateTimeUTC = splitDateTime[0].concat(" ").concat("T").concat(" ").concat(splitDateTime[1]).concat(" ").concat("UTC");
        return dateTimeUTC;

    }

    @Override
    public void save(String value) {
        if (StringUtils.startsWithIgnoreCase(value, START_TIME_TOKEN) && StringUtils.isNotEmpty(value.split(KEY_VALUE_SEPARATOR, 2)[1])) {
            String startTimeStr = value.split(KEY_VALUE_SEPARATOR, 2)[1];
            try {
                startTime = Long.parseLong(startTimeStr);
            } catch (NumberFormatException e) {
                startTime = 0L;
            }
        }

    }

}
