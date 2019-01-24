package com.dlvn.mcustomerportal.services.model.claims;

import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimsFromResponse {

    @SerializedName("Response")
    @Expose
    private CPSubmitFormResult response;

    public CPSubmitFormResult getResponse() {
        return response;
    }

    public void setResponse(CPSubmitFormResult response) {
        this.response = response;
    }

}
