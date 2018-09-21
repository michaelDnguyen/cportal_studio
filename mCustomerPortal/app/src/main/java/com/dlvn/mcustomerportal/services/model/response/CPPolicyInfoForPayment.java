package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CPPolicyInfoForPayment {
    @SerializedName("PolicyID")
    @Expose
    private String policyID;
    @SerializedName("PolSndryAmt")
    @Expose
    private String polSndryAmt;
    @SerializedName("APL")
    @Expose
    private String aPL;
    @SerializedName("OPL")
    @Expose
    private String oPL;

    public String getPolicyID() {
        return policyID;
    }

    public void setPolicyID(String policyID) {
        this.policyID = policyID;
    }

    public String getPolSndryAmt() {
        return polSndryAmt;
    }

    public void setPolSndryAmt(String polSndryAmt) {
        this.polSndryAmt = polSndryAmt;
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

}
