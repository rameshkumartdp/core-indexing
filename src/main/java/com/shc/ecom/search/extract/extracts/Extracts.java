/**
 *
 */
package com.shc.ecom.search.extract.extracts;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.shc.ecom.search.common.expirablefields.ExpirableSolrField;
/**
 * @author rgopala Jul 27, 2015 search-doc-builder
 *         <p>
 *         This should be the document that contain all the extracted
 *         information/fields from different component sources.
 */
public class Extracts implements Serializable {

    private static final long serialVersionUID = -7855678436118172221L;

    private ContentExtract contentExtract = new ContentExtract();
    private OfferExtract offerExtract = new OfferExtract();
    private BuyboxExtract buyboxExtract = new BuyboxExtract();
    private UasExtract uasExtract = new UasExtract();
    private IASExtract iasExtract = new IASExtract();
    private PASExtract pasExtract = new PASExtract();
    private PriceExtract priceExtract = new PriceExtract();
    private SellerExtract sellerExtract = new SellerExtract();
    private VarAttrExtract varAttrExtract = new VarAttrExtract();
    private ProdstatsExtract prodstatsExtract = new ProdstatsExtract();
    private BSOExtract bsoExtract = new BSOExtract();
    private PromoExtract promoExtract = new PromoExtract();
    private ImpressionExtract impressionExtract = new ImpressionExtract();
    private FitmentExtract fitmentExtract = new FitmentExtract();
    private LocalAdExtract localAdExtract = new LocalAdExtract();
    private TMExtract tmExtract = new TMExtract();
    private AuxiliaryOfferExtract auxiliaryOfferExtract = new AuxiliaryOfferExtract();
    
    private String ssin;

    private List<String> offerIds;

    // Expirable fields are stored here.
    private List<ExpirableSolrField> expirableFields;

    /**
     * Add expirable field in extract of particular document. The format is
     * <field name>_<startDate/endDate>_<fieldValue>_<Datetime>
     * 
     * @param expirableField
     */
    public void addExpirableField(ExpirableSolrField expirableField) {
        if (expirableFields == null) {
            expirableFields = new ArrayList<>();
        }
        expirableFields.add(expirableField);
    }

    /**
     * Add list of expirable fields.
     * 
     * @param expirableFieldList
     */
    public void addExpirableFieldList(List<ExpirableSolrField> expirableFieldList) {
        if (expirableFields == null) {
            expirableFields = new ArrayList<>();
        }
        expirableFields.addAll(expirableFieldList);
    }

    public List<ExpirableSolrField> getExpirableFields() {
        if (expirableFields == null){
            return new ArrayList<>();
        }
        return expirableFields;
    }
    
    /**
     * @return the offerExtract
     */
    public OfferExtract getOfferExtract() {
        return offerExtract;
    }

    /**
     * @param offerExtract the offerExtract to set
     */
    public void setOfferExtract(OfferExtract offerExtract) {
        this.offerExtract = offerExtract;
    }

    /**
     * @return the contentExtract
     */
    public ContentExtract getContentExtract() {
        return contentExtract;
    }

    /**
     * @param contentExtract the contentExtract to set
     */
    public void setContentExtract(ContentExtract contentExtract) {
        this.contentExtract = contentExtract;
    }

    /**
     * @return the buyboxExtract
     */
    public BuyboxExtract getBuyboxExtract() {
        return buyboxExtract;
    }

    /**
     * @param buyboxExtract the buyboxExtract to set
     */
    public void setBuyboxExtract(BuyboxExtract buyboxExtract) {
        this.buyboxExtract = buyboxExtract;
    }

    /**
     * @return the iasExtract
     */
    public IASExtract getIasExtract() {
        return iasExtract;
    }

    /**
     * @param iasExtract the iasExtract to set
     */
    public void setIasExtract(IASExtract iasExtract) {
        this.iasExtract = iasExtract;
    }

    /**
     * @return the pasExtract
     */
    public PASExtract getPasExtract() {
        return pasExtract;
    }

    /**
     * @param pasExtract the pasExtract to set
     */
    public void setPasExtract(PASExtract pasExtract) {
        this.pasExtract = pasExtract;
    }

    /**
     * @return the priceExtract
     */
    public PriceExtract getPriceExtract() {
        return priceExtract;
    }

    /**
     * @param priceExtract the priceExtract to set
     */
    public void setPriceExtract(PriceExtract priceExtract) {
        this.priceExtract = priceExtract;
    }

    /**
     * @return the sellerExtract
     */
    public SellerExtract getSellerExtract() {
        return sellerExtract;
    }

    /**
     * @param sellerExtract the sellerExtract to set
     */
    public void setSellerExtract(SellerExtract sellerExtract) {
        this.sellerExtract = sellerExtract;
    }

    /**
     * @return the varAttrExtract
     */
    public VarAttrExtract getVarAttrExtract() {
        return varAttrExtract;
    }

    /**
     * @param varAttrExtract the varAttrExtract to set
     */
    public void setVarAttrExtract(VarAttrExtract varAttrExtract) {
        this.varAttrExtract = varAttrExtract;
    }

    /**
     * @return the prodstatsExtract
     */
    public ProdstatsExtract getProdstatsExtract() {
        return prodstatsExtract;
    }

    /**
     * @param prodstatsExtract the prodstatsExtract to set
     */
    public void setProdstatsExtract(ProdstatsExtract prodstatsExtract) {
        this.prodstatsExtract = prodstatsExtract;
    }

    /**
     * @return the bsoExtract
     */
    public BSOExtract getBsoExtract() {
        return bsoExtract;
    }

    /**
     * @param bsoExtract the bsoExtract to set
     */
    public void setBsoExtract(BSOExtract bsoExtract) {
        this.bsoExtract = bsoExtract;
    }

    public List<String> getOfferIds() {
        if (offerIds == null) {
            offerIds = new ArrayList<>();
        }
        return offerIds;
    }

    public void setOfferIds(List<String> offerIds) {
        this.offerIds = offerIds;
    }

    /**
     * @return the ssin
     */
    public String getSsin() {
        return ssin;
    }

    /**
     * @param ssin the ssin to set
     */
    public void setSsin(String ssin) {
        this.ssin = ssin;
    }

    public PromoExtract getPromoExtract() {
        return promoExtract;
    }

    public void setPromoExtract(PromoExtract promoExtract) {
        this.promoExtract = promoExtract;
    }

    public UasExtract getUasExtract() {
        return uasExtract;
    }

    public void setUasExtract(UasExtract uasExtract) {
        this.uasExtract = uasExtract;
    }

    public ImpressionExtract getImpressionExtract() {
        return impressionExtract;
    }

    public void setImpressionExtract(ImpressionExtract impressionExtract) {
        this.impressionExtract = impressionExtract;
    }

    public FitmentExtract getFitmentExtract() {
        return fitmentExtract;
    }

    public void setFitmentExtract(FitmentExtract fitmentExtract) {
        this.fitmentExtract = fitmentExtract;
    }

    public TMExtract getTMExtract() {
        return tmExtract;
    }

    public LocalAdExtract getLocalAdExtract() {
        return localAdExtract;
    }

    public void setLocalAdExtract(LocalAdExtract localAdExtract) {
        this.localAdExtract = localAdExtract;
    }

	/**
	 * @return the auxiliaryOfferExtract
	 */
	public AuxiliaryOfferExtract getAuxiliaryOfferExtract() {
		return auxiliaryOfferExtract;
	}

	/**
	 * @param auxiliaryOfferExtract the auxiliaryOfferExtract to set
	 */
	public void setAuxiliaryOfferExtract(AuxiliaryOfferExtract auxiliaryOfferExtract) {
		this.auxiliaryOfferExtract = auxiliaryOfferExtract;
	}

}
