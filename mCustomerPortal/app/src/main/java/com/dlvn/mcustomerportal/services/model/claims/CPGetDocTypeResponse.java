package com.dlvn.mcustomerportal.services.model.claims;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CPGetDocTypeResponse {

    @SerializedName("Response")
    @Expose
    private CPGetDocTypeResult response;

    public CPGetDocTypeResult getResponse() {
        return response;
    }

    public void setResponse(CPGetDocTypeResult response) {
        this.response = response;
    }
}
