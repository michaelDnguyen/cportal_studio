package com.dlvn.mcustomerportal.services.model.response;

import java.util.List;

import com.dlvn.mcustomerportal.services.model.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTaxInvoiceResponse extends BaseResponse {

    @SerializedName("Response")
    @Expose
    private GetTaxInvoiceResult response;

    public GetTaxInvoiceResult getResponse() {
        return response;
    }

    public void setResponse(GetTaxInvoiceResult response) {
        this.response = response;
    }
}
