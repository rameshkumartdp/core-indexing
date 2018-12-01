package com.shc.ecom.search.extract.components.fitments;

import com.shc.ecom.search.common.fitment.FitmentDTO;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @author rgopala
 */
@Component
public class FitmentExtractComponent extends ExtractComponent<List<AutomotiveOffer>> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5243891287782524873L;

    @Autowired
    private FitmentService fitmentService;


    @Override
    protected List<AutomotiveOffer> get(WorkingDocument wd, ContextMessage context) {

        List<AutomotiveOffer> automotiveOffers = wd.getExtracts().getOfferExtract().getAutomotiveOffers();
        if (wd.getExtracts().getContentExtract().isUvd()) {
            for (AutomotiveOffer automotiveOffer : automotiveOffers) {
                // If brandCodeId from GB Offer is unavailable, the offer-id of an UVD can be used to get brandCodeId. The first 4 digits of an UVD's offer-id is its brandCodeId.
                String brandCodeId = StringUtils.isNotEmpty(automotiveOffer.getBrandCodeId()) ? automotiveOffer.getBrandCodeId() : automotiveOffer.getOfferId().substring(0, 4);

                // If manufacturing partno. from GB Offer is unavailable, the offer-id of an UVD can be used to get this. The remaining digits after brandCodeId of an UVD's offer-id are mfrPartNo.
                String mfrPartno = StringUtils.isNotEmpty(automotiveOffer.getMfrPartno()) ? automotiveOffer.getMfrPartno() : automotiveOffer.getOfferId().replaceFirst(brandCodeId, "");

                //Override if brandCodeId or mfrPartNo are not set.
                automotiveOffer.setBrandCodeId(brandCodeId);
                automotiveOffer.setMfrPartno(mfrPartno);

                // Make fitment calls for each brandCodeId or mfrPartno combination. Duplicates are not expected!
                automotiveOffer.fitmentDTO(fitmentService.process(brandCodeId, mfrPartno));

            }
        } else {
            // For Non-UVDs, use brandCodeId and mfrPartNo. from GB Content.
            String brandCodeId = wd.getExtracts().getContentExtract().getBrandCodeId();
            String mfrPartno = wd.getExtracts().getContentExtract().getModelNo();

            // Since brandCodeId and mfrPartNo. will be same for all offers, make one fitment call.
            FitmentDTO fitmentDTO = fitmentService.process(brandCodeId, mfrPartno);

            // Nevertheless, set same fitment details for each offer/uid so the fitment can be queried using any offers of this product
            for (AutomotiveOffer automotiveOffer : automotiveOffers) {
                automotiveOffer.setBrandCodeId(brandCodeId);
                automotiveOffer.setMfrPartno(mfrPartno);
                automotiveOffer.fitmentDTO(fitmentDTO);
            }
        }
        return automotiveOffers;
    }

    @Override
    protected Extracts extract(List<AutomotiveOffer> source, WorkingDocument wd,
                               ContextMessage context) {
        wd.getExtracts().getFitmentExtract().setAutomotiveOffers(source);
        return wd.getExtracts();
    }


}
