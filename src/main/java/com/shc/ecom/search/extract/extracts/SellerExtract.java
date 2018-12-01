package com.shc.ecom.search.extract.extracts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.shc.ecom.search.common.seller.OrderAmountChargeRange;
import com.shc.ecom.search.common.seller.WeightRange;

/**
 * @author rgopala
 */
public class SellerExtract implements Serializable {

    private static final long serialVersionUID = -8536014326324068683L;
    private String sellerTier;
    private boolean trustedSeller;
    private Map<String, String> offerToStoreFrontNames;
    private Map<String, List<String>> sfNameToFeaturedOffers;
    private Map<String, List<WeightRange>> offerWeightRange;
    private Map<String, List<OrderAmountChargeRange>> offerOrderAmountChargeRange;

    /**
     * @return the sellerTier
     */
    public String getSellerTier() {
        return sellerTier;
    }

    /**
     * @param sellerTier the sellerTier to set
     */
    public void setSellerTier(String sellerTier) {
        this.sellerTier = sellerTier;
    }

    public boolean getTrustedSeller() {
        return trustedSeller;
    }

    public void setTrustedSeller(boolean trustedSeller) {
        this.trustedSeller = trustedSeller;
    }

    public Map<String, String> getOfferToStoreFrontNames() {
        if (offerToStoreFrontNames == null) {
            offerToStoreFrontNames = new HashMap<>();
        }
        return offerToStoreFrontNames;
    }

    public void setOfferToStoreFrontNames(Map<String, String> offerToStoreFrontNames) {
        this.offerToStoreFrontNames = offerToStoreFrontNames;
    }

    public Map<String, List<String>> getSfNameToFeaturedOffers() {
        if (sfNameToFeaturedOffers == null) {
            sfNameToFeaturedOffers = new HashMap<>();
        }
        return sfNameToFeaturedOffers;
    }

    public void setSfNameToFeaturedOffers(Map<String, List<String>> sfNameToFeaturedOffers) {
        this.sfNameToFeaturedOffers = sfNameToFeaturedOffers;
    }

    /**
	 * @return the offerWeightRange
	 */
	public Map<String, List<WeightRange>> getOfferWeightRange() {
		if(offerWeightRange==null) {
			offerWeightRange = new HashMap<>();
		}
		return offerWeightRange;
	}

	/**
	 * @param offerWeightRange the offerWeightRange to set
	 */
	public void setOfferWeightRange(Map<String, List<WeightRange>> offerWeightRange) {
		this.offerWeightRange = offerWeightRange;
	}

	/**
	 * @return the offerOrderAmountChargeRange
	 */
	public Map<String, List<OrderAmountChargeRange>> getOfferOrderAmountChargeRange() {
		if(offerOrderAmountChargeRange==null) {
			offerOrderAmountChargeRange = new HashMap<>();
		}
		return offerOrderAmountChargeRange;
	}

	/**
	 * @param offerOrderAmountChargeRange the offerOrderAmountChargeRange to set
	 */
	public void setOfferOrderAmountChargeRange(Map<String, List<OrderAmountChargeRange>> offerOrderAmountChargeRange) {
		this.offerOrderAmountChargeRange = offerOrderAmountChargeRange;
	}

	public enum SellerTierRank {

        PLATINUM(1), //
        GOLD(2), //
        SILVER(3), //
        REGULAR(4), //
        BLACK(5);//

        private int rank;

        private SellerTierRank(int rank) {
            this.rank = rank;
        }

        public static int getRank(String sellerTier) {
            int rank = -1;
            for (SellerTierRank sellerTierRank : SellerTierRank.values()) {
                if (StringUtils.equalsIgnoreCase(sellerTierRank.name(), sellerTier)) {
                    rank = sellerTierRank.rank;
                    break;
                }
            }
            return rank;
        }

        public static String getSellerTier(int rank) {
            String sellerTier = null;
            for (SellerTierRank sellerTierRank : SellerTierRank.values()) {
                if (rank == sellerTierRank.rank) {
                    sellerTier = sellerTierRank.name();
                    break;
                }
            }
            return sellerTier;
        }

        public int getRank() {
            return this.rank;
        }
    }


}
