package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CPSubmitFormResponse {

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
