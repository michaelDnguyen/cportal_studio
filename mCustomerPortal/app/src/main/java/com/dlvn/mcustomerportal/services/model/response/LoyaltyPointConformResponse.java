package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoyaltyPointConformResponse {

    @SerializedName("CPLoyaltyPointConfirmationResult")
    @Expose
    private LoyaltyPointConformResult cPLoyaltyPointConfirmationResult;

    public LoyaltyPointConformResult getCPLoyaltyPointConfirmationResult() {
        return cPLoyaltyPointConfirmationResult;
    }

    public void setCPLoyaltyPointConfirmationResult(LoyaltyPointConformResult cPLoyaltyPointConfirmationResult) {
        this.cPLoyaltyPointConfirmationResult = cPLoyaltyPointConfirmationResult;
    }
}
