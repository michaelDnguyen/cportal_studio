package com.dlvn.mcustomerportal.services.model.claims;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CPGetClaimIDResponse {

    @SerializedName("GetSubmissionIDResult")
    @Expose
    private CPGetClaimIDResult response;

    public CPGetClaimIDResult getResponse() {
        return response;
    }

    public void setResponse(CPGetClaimIDResult response) {
        this.response = response;
    }
}
