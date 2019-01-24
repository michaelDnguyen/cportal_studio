package com.dlvn.mcustomerportal.services.model.claims;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetClaimsStatusResponse {

    @SerializedName("GetProposalStatusResult")
    @Expose
    private GetClaimsStatusResult response;

    public GetClaimsStatusResult getResponse() {
        return response;
    }

    public void setResponse(GetClaimsStatusResult response) {
        this.response = response;
    }

}
