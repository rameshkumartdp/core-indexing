package com.shc.ecom.search.extract.components.promo;

import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import org.apache.commons.lang.StringUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rgopala
 */
@Component
public class Promotions {

    public static final String TIERED = "Tiered";
    public static final String BMGMSYWR_POINTS = "BMGMSYWRPoints";

    public static final String KMART_STORESALE_DISCOUNT_PROMOTION = "KmartStoresaleDiscountPromotion";
    public static final String KMART_BMGM_DISCOUNT_PROMOTION = "KmartBMGMDiscountPromotion";

    public static final String SEARSSTORESALE_DISCOUNT_PROMOTION = "SearsStoresaleDiscountPromotion";
    public static final String SEARS_BMGM_DISCOUNT_PROMOTION = "SearsBMGMDiscountPromotion";

    public static final String SRS_PUERTORICO_STORESALE_DISCOUNT_PROMOTION = "SRSPuertoRicoStoresaleDiscountPromotion";
    public static final String SRS_PUERTORICO_BMGM_DISCOUNT_PROMOTION = "SRSPuertoRicoBMGMDiscountPromotion";

    public static final String SHIPPING = "Shipping";
    public static final String COMPANION = "Companion";
    public static final String DELIVERY = "Delivery";
    public static final String PRICE = "Price";
    public static final String QUANTITY = "Quantity";
    public static final String SHIPPING_INFORMATION = "Ship To Store Ground,UPS";

    public boolean isTiered(String promoType) {
        boolean tiered = StringUtils.equalsIgnoreCase(promoType, TIERED) ? true : false;
        return tiered;
    }

    public boolean isBuyMoreGiveMoreSYWRPoints(String promoType) {
        boolean bmgmsywrPoints = StringUtils.equalsIgnoreCase(promoType, BMGMSYWR_POINTS) ? true : false;
        return bmgmsywrPoints;
    }

    public boolean searsStoreSalePromotion(String grpName) {
        boolean searsStoreSalePromo = StringUtils.equalsIgnoreCase(grpName, SEARSSTORESALE_DISCOUNT_PROMOTION) ? true : false;
        return searsStoreSalePromo;
    }

    public boolean kmartStoreSalePromotion(String grpName) {
        boolean kmartStoreSalePromo = StringUtils.equalsIgnoreCase(grpName, KMART_STORESALE_DISCOUNT_PROMOTION) ? true : false;
        return kmartStoreSalePromo;
    }

    public boolean searsPrStoreSalePromotion(String grpName) {
        boolean searsPrStoreSalePromo = StringUtils.equalsIgnoreCase(grpName, SRS_PUERTORICO_STORESALE_DISCOUNT_PROMOTION) ? true : false;
        return searsPrStoreSalePromo;
    }

    public boolean searsBMGMPromotion(String grpName) {
        boolean searsBmgmPromo = StringUtils.equalsIgnoreCase(grpName, SEARS_BMGM_DISCOUNT_PROMOTION) ? true : false;
        return searsBmgmPromo;
    }

    public boolean kmartBMGMPromotion(String grpName) {
        boolean kmartBmgmPromo = StringUtils.equalsIgnoreCase(grpName, KMART_BMGM_DISCOUNT_PROMOTION) ? true : false;
        return kmartBmgmPromo;
    }

    public boolean searsPrBMGMPromotion(String grpName) {
        boolean searsPrBmgmPromo = StringUtils.equalsIgnoreCase(grpName, SRS_PUERTORICO_BMGM_DISCOUNT_PROMOTION) ? true : false;
        return searsPrBmgmPromo;
    }

    public List<String> getSywPromotionTxt(WorkingDocument wd, ContextMessage context) {

        List<String> storePromotionTxtSyw = new ArrayList<>();
        List<Integer> storeIds = Stores.getStoreId(wd, context);
        for (Integer storeId : storeIds) {
            List<PromoSywMbr> promoSywMbrList = new ArrayList<>();
            if (wd.getExtracts().getPromoExtract().getStorePromoExtracts().containsKey(Stores.getStore(storeId))) {
                promoSywMbrList = wd.getExtracts().getPromoExtract().getStorePromoExtracts().get(Stores.getStore(storeId)).getPromoSywMbr();

            }
            String promotionTxtSyw = null;

            for (PromoSywMbr promo : promoSywMbrList) {

                boolean dateFlag = new Promotions().isValidPromotion(promo.getStartDt(), promo.getEndDt());

                if (promo.getPromoType() == null || !dateFlag) {
                    continue;
                }

                if ((Stores.SEARS.matches(context.getStoreName()) || Stores.KMART.matches(context.getStoreName())
                        || Stores.FBM.matches(context.getStoreName()))
                        && promo.getPromoType().equalsIgnoreCase(COMPANION)
                        && promo.getShipInfo().equalsIgnoreCase(DELIVERY)
                        && (promo.getThresholdCond().equalsIgnoreCase(PRICE) || (promo.getThresholdCond().equalsIgnoreCase(QUANTITY) && Float.parseFloat(promo.getMinPurchaseVal()) >= 1))) {
                    promotionTxtSyw = "SYWFREE";
                }
            }
            if (promotionTxtSyw != null) {
                storePromotionTxtSyw.add(Integer.toString(storeId) + "_" + promotionTxtSyw);
            }
        }
        return storePromotionTxtSyw;
    }

    public String getFreeDeliveryPromoTxt(PromoRegular promo, String minPurchase, String prmTxtFreeDelivery) {
        String programTextFreeDelivery = prmTxtFreeDelivery;

        if (promo.getPromoType().equalsIgnoreCase(COMPANION)
                && "Dollar Off".equalsIgnoreCase(promo.getDiscType())
                && (promo.getDiscAmt() != null && !promo.getDiscAmt().isEmpty())) {

            if (programTextFreeDelivery == null) {
                programTextFreeDelivery = "D";
            }

            programTextFreeDelivery = programTextFreeDelivery + "_" + minPurchase + "_" + promo.getDiscAmt();
        }

        return programTextFreeDelivery;
    }

    public String getDeliveryPromoTxt(PromoRegular promo, String minPurchase, String prmTxtDelivery) {

        String prmType = promo.getPromoType().substring(0, 1).trim();
        String programTextDelivery = prmTxtDelivery;

        if (promo.getPromoType().equalsIgnoreCase(COMPANION) &&
                promo.getFreePromoFlag() &&
                !promo.getShipInfo().equalsIgnoreCase(SHIPPING_INFORMATION) &&
                !(Float.parseFloat(minPurchase) >= 1.0 &&
                        promo.getThresholdCond().equalsIgnoreCase(QUANTITY))) {

            if (programTextDelivery == null) {
                programTextDelivery = prmType;
            }

            programTextDelivery = programTextDelivery + "_" + minPurchase;

        }

        return programTextDelivery;
    }

    public String getShippingPromoTxt(PromoRegular promo, String minPurchase, String prmTxtShipping) {

        String programTypeTxtShipping = prmTxtShipping;
        String prmType = promo.getPromoType().substring(0, 1).trim();

        if (promo.getPromoType().equalsIgnoreCase(SHIPPING)) {

            if (promo.getFreePromoFlag() && Float.parseFloat(minPurchase) >= 1.0 &&
                    !promo.getShipInfo().equalsIgnoreCase(SHIPPING_INFORMATION)) {

                if (programTypeTxtShipping == null) {
                    programTypeTxtShipping = prmType;
                }

                programTypeTxtShipping = programTypeTxtShipping + "_" + minPurchase;
            }
        }

        return programTypeTxtShipping;
    }

    public void appendPromotion(String textField, StringBuilder promotionTxt) {

        if (StringUtils.isNotEmpty(textField)) {
            if (promotionTxt.length() > 0) {
                promotionTxt.append(";");
            }

            promotionTxt.append(textField);
        }
    }

    public boolean isValidPromotion(LocalDate startDate, LocalDate endDate) {

        boolean dateFlag = false;
        LocalDate today = new LocalDate();

        if (startDate != null) {
            int numberOfDaysFromStart = Days.daysBetween(startDate, today).getDays();
            if (numberOfDaysFromStart >= 0) {
                dateFlag = true;
            }
        }

        if (endDate != null) {
            int numberOfDaysTillEnd = Days.daysBetween(today, endDate).getDays();
            if (numberOfDaysTillEnd < 0) {
                dateFlag = false;
            }
        }

        return dateFlag;

    }

}
