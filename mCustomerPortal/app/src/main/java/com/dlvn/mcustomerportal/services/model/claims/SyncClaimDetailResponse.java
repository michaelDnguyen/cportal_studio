package com.dlvn.mcustomerportal.services.model.claims;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SyncClaimDetailResponse {

    @SerializedName("Response")
    @Expose
    private SyncClaimDetailResult response;

    public SyncClaimDetailResult getResponse() {
        return response;
    }

    public void setResponse(SyncClaimDetailResult response) {
        this.response = response;
    }

}
