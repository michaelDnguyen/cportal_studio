package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFeeByClientIDResponse {

    @SerializedName("GetFeeByClientID")
    @Expose
    private GetFeeByClientIDResult getFeeByClientID;

    public GetFeeByClientIDResult getGetFeeByClientID() {
        return getFeeByClientID;
    }

    public void setGetFeeByClientID(GetFeeByClientIDResult getFeeByClientID) {
        this.getFeeByClientID = getFeeByClientID;
    }

}
