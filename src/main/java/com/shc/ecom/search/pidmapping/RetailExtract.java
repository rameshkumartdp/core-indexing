package com.shc.ecom.search.pidmapping;

import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.search.common.pidmapping.PidInfo;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.indexer.SpringContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class RetailExtract implements Serializable {

	private static final long serialVersionUID = -3051736374805659193L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RetailExtract.class);

	public void retailextract(List<PidInfo> pidInfos) {

		if (CollectionUtils.isEmpty(pidInfos)) {
			return;
		} else {
			Integer mappingThreadPoolCount = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.MAPPING_THREAD_POOL_COUNT));
			ExecutorService executor = Executors
					.newFixedThreadPool(mappingThreadPoolCount);
			List<RetailDta> retailDtalist = new ArrayList<>();
			for (int i = 0; i < pidInfos.size(); i++) {
				PidInfo info = pidInfos.get(i);
				String expDate = info.getExpDate();
				String oldPid = info.getOldPid();
				String newPid = info.getNewPid();

				if (!isExpired(expDate) && !StringUtils.isEmpty(oldPid)
						&& !StringUtils.isEmpty(newPid)) {
					RetailDta retaildata = SpringContext
							.getApplicationContext().getBean(RetailDta.class);
					retaildata.configure("Extractor-" + i, oldPid, newPid);
					retailDtalist.add(retaildata);
				}

			}
			for (RetailDta retailDta : retailDtalist) {
				try {
					Future<String> future = executor.submit(retailDta);
					LOGGER.info("RetailExtract:futureLoop:future status:"
							+ future.get());
				} catch (InterruptedException | ExecutionException e) {
					LOGGER.error("Interrupted Thread Exception while waiting ",
							e);
				}
			}
			executor.shutdown();
		}
	}

	private boolean isExpired(String futureTime) {

		if (StringUtils.isEmpty(futureTime)) {
			return true;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		formatter.setLenient(false);

		Date expDate;
		try {
			expDate = formatter.parse(futureTime.concat(" 23:59:59"));
			long futureTimeInMillis = expDate.getTime();
			long currentTimeInMillis = System.currentTimeMillis();
			if (currentTimeInMillis < futureTimeInMillis) {
				return false;
			}

		} catch (Exception e) {
			LOGGER.debug("Invalid Date format", e);
			LOGGER.error("Invalid Date format", futureTime);
			return true;
		}
		return true;
	}

}
