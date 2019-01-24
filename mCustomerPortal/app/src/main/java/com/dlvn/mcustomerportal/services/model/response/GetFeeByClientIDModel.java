package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFeeByClientIDModel {
    @SerializedName("PolicyID")
    @Expose
    private String policyID;
    @SerializedName("Fee")
    @Expose
    private GetFeeModel fee;

    public String getPolicyID() {
        return policyID;
    }

    public void setPolicyID(String policyID) {
        this.policyID = policyID;
    }

    public GetFeeModel getFee() {
        return fee;
    }

    public void setFee(GetFeeModel fee) {
        this.fee = fee;
    }

}
