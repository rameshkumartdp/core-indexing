package com.shc.ecom.search.indexer;

import com.beust.jcommander.Parameter;
import com.shc.ecom.search.indexer.validator.ModeValidator;
import com.shc.ecom.search.indexer.validator.ServerValidator;
import com.shc.ecom.search.indexer.validator.StoreValidator;

/**
 * Created by tgulati on 8/25/16.
 */
public class Arguments {

    @Parameter(names = "-mode", description = "the run mode", required = true, validateWith = ModeValidator.class)
    private String mode = "";

    @Parameter(names = "-message", description = "the message type", required = false)
    private String message = "";

    @Parameter(names = "-store", description = "the store to index for", required = true, validateWith = StoreValidator.class)
    private String store = "";

    @Parameter(names = "-ssin", description = "the ssin to index", required = false)
    private String ssin = "";

    @Parameter(names = "-opsUuid", description = "the time stamp for when indexing began", required = true)
    private String opsUuid = "";

    @Parameter(names = "-propertyFile", description = "the property file to use", required = true)
    private String propertyFile = "";

    @Parameter(names = "-server", description = "the server indexing the ssin", required = true, validateWith = ServerValidator.class)
    private String server = "";

    @Parameter(names = "-json", description = "the json document to be indexed", required = false)
    private String json = "";

    @Parameter(names = "-bucket", description = "the bucket id/number to be indexed", required = false)
    private String bucketId = "";

    @Parameter(names = "-offerId", description = "the offerID to be indexed", required = false)
    private String offerId = "";

    @Parameter(names = "-indexCommercial", description = "index commercial docs", required = true)
    private String indexCommercial = "";

    public String getOpsUuid() {
        return opsUuid;
    }

    public void setOpsUuid(String opsUuid) {
        this.opsUuid = opsUuid;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getSsin() {
        return ssin;
    }

    public void setSsin(String ssin) {
        this.ssin = ssin;
    }

    public String getPropertyFile() {
        return propertyFile;
    }

    public void setPropertyFile(String propertyFile) {
        this.propertyFile = propertyFile;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIndexCommercial() {
        return indexCommercial;
    }

    public void setIndexCommercial(String indexCommercial) {
        this.indexCommercial = indexCommercial;
    }

    @Override
    public String toString() {
        return message + " " + mode + " " + ssin + " " + store + " " + opsUuid + " " + server + " " + propertyFile + " " + bucketId + " " + offerId + " " + json;
    }

}
