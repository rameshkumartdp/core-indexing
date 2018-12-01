package com.shc.ecom.search.extract.components.tm;

import com.shc.ecom.search.common.tm.TopicModelingDTO;
import com.shc.ecom.search.config.GlobalConstants;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
public class TopicModelingService implements Serializable {

    private static final long serialVersionUID = -2794732336111961397L;

    @Autowired
    private TopicModelingComponent topicModelingComponent;

    public TopicModelingDTO getInferenceAndConvertToDto(String inputText) {
        String topicDistribution = topicModelingComponent.getInference(inputText);
        return convertToDto(topicDistribution);
    }


    public TopicModelingDTO convertToDto(String topicDistribution) {
        TopicModelingDTO topicModelingDTO = new TopicModelingDTO();

        if (StringUtils.isNotBlank(topicDistribution)) {
            String[] tokens = topicDistribution.split("\\s+");
            Map<String, Float> topicIdsWithProbability = new LinkedHashMap<>();
            int index = 0;

            while (index <= tokens.length - 2) {
                topicIdsWithProbability.put(tokens[index], Float.parseFloat(tokens[++index]));
                index++;
            }

            topicModelingDTO.setTopicDistribution(topicIdsWithProbability);
        }

        return topicModelingDTO;
    }

    /**
     * The transformation logic in one place that can be used by c2c and base field transformation
     *
     * @param topicMaps
     * @return
     */
    public List<String> getTopicIdWithRank(Map<String, TopicModelingDTO> topicMaps) {
        List<String> topicIdRankList = new ArrayList<>();
        if (MapUtils.isNotEmpty(topicMaps) && topicMaps.containsKey(GlobalConstants.NAME_SEARCHABLE)) {
            TopicModelingDTO topicModelingDTO = topicMaps.get(GlobalConstants.NAME_SEARCHABLE);
            int rank = 1;
            for (Map.Entry<String, Float> topicDistributionEntry : topicModelingDTO.getTopicDistribution().entrySet()) {
                float probability = topicDistributionEntry.getValue();
                if (probability > topicModelingComponent.getThresholdProbability()) {
                    topicIdRankList.add(topicDistributionEntry.getKey() + "_rank" + rank++);
                }
            }
        }
        //will return null when no topic ids are extracted
        return topicIdRankList;
    }

    public String getTopicModelingWithNameSearchable(Map<String, TopicModelingDTO> topicMaps) {
        String replicationOfTopicIds = null;
        if (MapUtils.isNotEmpty(topicMaps) && topicMaps.containsKey(GlobalConstants.NAME_SEARCHABLE)) {
            TopicModelingDTO topicModelingDTO = topicMaps.get(GlobalConstants.NAME_SEARCHABLE);
            StringBuilder replicatedIds = new StringBuilder();
            for (Map.Entry<String, Float> topicDistributionEntry : topicModelingDTO.getTopicDistribution().entrySet()) {
                float probability = topicDistributionEntry.getValue();
                String topicId = topicDistributionEntry.getKey();
                int topicIDReplication = (int) Math.ceil(probability * topicModelingComponent.getReplicationFactor());
                replicatedIds.append(org.apache.commons.lang3.StringUtils.repeat(topicId, " ", topicIDReplication) + " ");
            }
            replicationOfTopicIds = replicatedIds.toString().trim();
        }
        return replicationOfTopicIds;
    }


}
