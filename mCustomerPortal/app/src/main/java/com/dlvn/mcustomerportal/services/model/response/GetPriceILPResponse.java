package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPriceILPResponse {

    @SerializedName("Response")
    @Expose
    private GetPriceILPResult response;

    public GetPriceILPResult getResponse() {
        return response;
    }

    public void setResponse(GetPriceILPResult response) {
        this.response = response;
    }
}
