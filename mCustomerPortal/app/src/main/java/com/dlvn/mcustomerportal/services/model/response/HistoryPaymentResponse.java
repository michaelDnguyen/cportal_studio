package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryPaymentResponse {

    @SerializedName("Response")
    @Expose
    private HistoryPaymentResult response;

    public HistoryPaymentResult getResponse() {
        return response;
    }

    public void setResponse(HistoryPaymentResult response) {
        this.response = response;
    }
}
