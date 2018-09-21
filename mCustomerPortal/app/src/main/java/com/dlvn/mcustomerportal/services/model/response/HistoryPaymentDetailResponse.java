package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryPaymentDetailResponse {

    @SerializedName("Response")
    @Expose
    private HistoryPaymentDetailResult response;

    public HistoryPaymentDetailResult getResponse() {
        return response;
    }

    public void setResponse(HistoryPaymentDetailResult response) {
        this.response = response;
    }
}
