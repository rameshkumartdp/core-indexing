package com.shc.ecom.search.transformations;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import com.shc.ecom.search.common.constants.CatentrySubType;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.extracts.WorkingDocument;

/**
 * The logic from Basefield transformation has been moved into this class so that it can be
 * shared between TopicModelingExtractComponent and BasefieldTransformation class
 * Topic Modeling requires the fields NameSearchable and Description which are only available
 * inside BasefieldTrasnformation.
 * To not repeat writing the same code, we moved the code to common place accessible to both
 * the classes
 */
@Component
public class CommonTransformationLogic implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5371105467868167544L;

	protected String getName(WorkingDocument wd) {
        String catentrySubType = wd.getExtracts().getContentExtract().getCatentrySubType();

        //This if is only for topic modeling..
        if (CatentrySubType.NON_VARIATION.matches(catentrySubType)) {
            return wd.getExtracts().getOfferExtract().getName();
        }
        return wd.getExtracts().getContentExtract().getName();
    }

    protected String getBrand(WorkingDocument wd) {
        String brand = null;
        if (StringUtils.isNotEmpty(wd.getExtracts().getContentExtract().getBrandName())) {
            brand = wd.getExtracts().getContentExtract().getBrandName();
        }
        return brand;
    }


    public String getNameSearchableFromContentTransformationLogic(WorkingDocument wd) {
        String brand = getBrand(wd);
        String name = getName(wd);

        name = StringUtils.normalizeSpace(name);
        brand = StringUtils.normalizeSpace(brand);

        // remove brand names if it already exists (as a whole) in name
        name = StringUtils.replace(name, brand, StringUtils.EMPTY);

        String nameSearchable = StringUtils.isEmpty(brand) ? name : brand + " " + name;
        nameSearchable = StringUtils.replaceChars(nameSearchable, ',', ' ');
        nameSearchable = StringUtils.normalizeSpace(nameSearchable);
        nameSearchable = Jsoup.parse(nameSearchable).text();
        return nameSearchable;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    public String getDescriptionFromContentTransformationLogic(WorkingDocument wd, ContextMessage context) {
        if (StringUtils.isNotEmpty(wd.getExtracts().getContentExtract().getLongDescription())) {
            return Jsoup.parse(wd.getExtracts().getContentExtract().getLongDescription()).text();
        }

        if (StringUtils.isNotEmpty(wd.getExtracts().getContentExtract().getShortDescription())) {
            return Jsoup.parse(wd.getExtracts().getContentExtract().getShortDescription()).text();
        }
        return StringUtils.EMPTY;
    }

    public String getNameSearchableFromCommonTransformationLogic(String brand, String name) {
        String normalizedName = StringUtils.normalizeSpace(name);
        String normalizedBrand = StringUtils.normalizeSpace(brand);

        // remove brand names if it already exists (as a whole) in name
        normalizedName = StringUtils.replace(normalizedName, normalizedBrand, StringUtils.EMPTY);

        String nameSearchable = StringUtils.isEmpty(normalizedBrand) ? normalizedName : normalizedBrand + " " + normalizedName;
        nameSearchable = StringUtils.replaceChars(nameSearchable, ',', ' ');
        nameSearchable = StringUtils.normalizeSpace(nameSearchable);
        nameSearchable = Jsoup.parse(nameSearchable).text();
        return nameSearchable;
    }
}
