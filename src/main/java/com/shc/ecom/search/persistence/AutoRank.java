/**
 *
 */
package com.shc.ecom.search.persistence;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rgopala
 */

@Component
public class AutoRank extends FileDataAccessor {

    private static final long serialVersionUID = -8733998928706096337L;

    private static final String SEPARATOR = "\\|";

    private static final int NUM_OF_FIELDS = 15;

    private static final int DEFAULT_RANK = 1;

    private Map<String, Map<String, Double>> ranking = new HashMap<>();

    private Map<String, Map<String, Double>> temp = new HashMap<>();

    @Override
    public void save(String value) {
        if (StringUtils.equalsIgnoreCase(value, "EOF")) {
            swap();
        }
        String[] data = value.split(SEPARATOR);
        if (data.length < NUM_OF_FIELDS) {
            return;
        }

        String catalogId = data[1];
        Map<String, Double> statsWeightage = buildStats(data);
        temp.put(catalogId, statsWeightage);
    }

    /**
     * Builds & obtains stats (eg: revenue) to its weightage/importance mapping from the autoRank.txt file.
     *
     * @param data
     * @return
     */
    private Map<String, Double> buildStats(String[] data) {
        Map<String, Double> statsWeightage = new HashMap<>();
        for (int i = 3; i < data.length; i = i + 2) {
            if (!NumberUtils.isNumber(data[i + 1])) {
                continue;
            }
            String keyStat = data[i];
            double statRank = Double.parseDouble(data[i + 1]);
            statsWeightage.put(keyStat, statRank);
        }
        return statsWeightage;
    }

    /**
     * Swaps the temporary collection data on to the original (served) collection.
     */
    private void swap() {
        if (temp.size() > 0) {
            ranking.putAll(temp);
            temp.clear();
        }
    }

    /**
     * Get stats ranking (weightage) for each catalog
     *
     * @return
     */
    public Map<String, Map<String, Double>> getRankings() {
        return ranking;
    }

    public Map<String, Double> getRankings(String catalogId) {
        if (ranking.containsKey(catalogId)) {
            return ranking.get(catalogId);
        }
        return new HashMap<String, Double>();
    }

    public double getRank(String catalogId, String stats) {
        Map<String, Double> rankings = getRankings(catalogId);
        if (rankings.containsKey(stats)) {
            return rankings.get(stats);
        }
        return DEFAULT_RANK;
    }
}
