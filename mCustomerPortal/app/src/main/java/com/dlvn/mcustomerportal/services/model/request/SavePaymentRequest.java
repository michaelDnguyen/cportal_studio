package com.dlvn.mcustomerportal.services.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SavePaymentRequest {

    @SerializedName("ClientId")
    @Expose
    private String clientID;
    @SerializedName("MerchTxnRef")
    @Expose
    private String merchTxnRef;
    @SerializedName("POName")
    @Expose
    private String pOName;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("PolId")
    @Expose
    private String polId;
    @SerializedName("Frequency")
    @Expose
    private String frequency;
    @SerializedName("APL")
    @Expose
    private String aPL;
    @SerializedName("OPL")
    @Expose
    private String oPL;
    @SerializedName("TotalAmount")
    @Expose
    private String totalAmount;

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getMerchTxnRef() {
        return merchTxnRef;
    }

    public void setMerchTxnRef(String merchTxnRef) {
        this.merchTxnRef = merchTxnRef;
    }

    public String getPOName() {
        return pOName;
    }

    public void setPOName(String pOName) {
        this.pOName = pOName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPolId() {
        return polId;
    }

    public void setPolId(String polId) {
        this.polId = polId;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getAPL() {
        return aPL;
    }

    public void setAPL(String aPL) {
        this.aPL = aPL;
    }

    public String getOPL() {
        return oPL;
    }

    public void setOPL(String oPL) {
        this.oPL = oPL;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
