/**
 *
 */
package com.shc.ecom.search.extract.components.filters;

import com.shc.common.index.rules.Decision;
import com.shc.common.index.rules.ValidationResults;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.nFiltersData.NFiltersDataDto;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.persistence.NFiltersData;
import com.shc.ecom.search.rules.IRule;
import com.shc.ecom.search.util.DecisionUtil;
import com.shc.ecom.search.validator.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author rgopala
 */
@Component
public class FiltersExtractComponent extends ExtractComponent<Map<String, NFiltersDataDto>> {

    private static final long serialVersionUID = 6415494826668611944L;

    @Autowired
    private NFiltersData nFiltersData;

    @Autowired
    private Taxonomy taxonomy;

    @Resource(name = "taxonomyRules")
    private List<IRule<String>> taxonomyRules;

    @Autowired
    private Validator validator;


    @Override
    protected Map<String, NFiltersDataDto> get(WorkingDocument wd, ContextMessage context) {
        return nFiltersData.getAttachedData();
    }


    /**
     * Validation for retired taxonomy paths.
     * Validation is as follows:
     * 1) Check if the hierarchies obtained per site from content collection is available in the attached-data.
     * 2) If the primary-hierarchy taxonomy is unavailable/retired, reject the product
     * 3) If the other-hierarchies are unavailable/retired, remove those hierarchies and index the product.
     */
    @Override
    public Decision validate(Map<String, NFiltersDataDto> source, WorkingDocument wd, ContextMessage context) {
        List<String> categories = taxonomy.getCategory(wd, context);
        ValidationResults results = null;
        if (!categories.isEmpty()) {
            if (StringUtils.equalsIgnoreCase(context.getStoreName(), Stores.COMMERCIAL.getStoreName())){
                results = new ValidationResults();
                results.setPassed(true);
                return getDecision(results, wd, context);
            }
            String primaryHierarchy = categories.get(0);
            primaryHierarchy = primaryHierarchy.replaceAll("\\s", StringUtils.EMPTY);
            results = validator.validate(primaryHierarchy, taxonomyRules, context);
            if (!results.isPassed()) {
                return getDecision(results, wd, context);
            }
            categories.remove(0);
            for (String category : categories) {
                results = validator.validate(primaryHierarchy, taxonomyRules, context);
                if (!results.isPassed()) {
                   //remove matching hierarchy from specific hierarchies.
                    category = category + StringUtils.EMPTY;
                }
            }
            results.setPassed(true);
        } else {
            results = validator.validate("UNAVAILABLE CATEGORY", taxonomyRules, context);
            results.setPassed(false);
        }
        return getDecision(results, wd, context);
    }


    private Decision getDecision(ValidationResults results, WorkingDocument wd, ContextMessage context) {
        Decision decision = wd.getDecision();
        decision.setId(wd.getExtracts().getSsin());
        decision.setIdType("SSIN");
        decision.setRejected(!results.isPassed());
        decision.addValidationResults(results);
        decision.setOfferId(context.getOfferId());
        decision.setStore(context.getStoreName());
        decision.setSites(DecisionUtil.getSitesList(Stores.getStore(context.getStoreName()).getEligibleSites(wd, context)));
        decision.setOpsUuid(context.getTimestamp());
        return decision;
    }


    @Override
    protected Extracts extract(Map<String, NFiltersDataDto> source, WorkingDocument wd, ContextMessage context) {
        return wd.getExtracts();
    }

}
