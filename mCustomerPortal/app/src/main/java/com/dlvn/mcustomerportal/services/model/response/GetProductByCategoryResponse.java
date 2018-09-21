package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetProductByCategoryResponse {

    @SerializedName("CPGetProductByCategoryResult")
    @Expose
    private GetProductByCategoryResult cPGetProductByCategoryResult;

    public GetProductByCategoryResult getCPGetProductByCategoryResult() {
        return cPGetProductByCategoryResult;
    }

    public void setCPGetProductByCategoryResult(GetProductByCategoryResult cPGetProductByCategoryResult) {
        this.cPGetProductByCategoryResult = cPGetProductByCategoryResult;
    }
}
