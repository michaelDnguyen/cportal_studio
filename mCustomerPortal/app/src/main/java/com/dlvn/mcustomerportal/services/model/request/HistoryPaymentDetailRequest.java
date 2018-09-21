package com.dlvn.mcustomerportal.services.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryPaymentDetailRequest {

    @SerializedName("TransactionDetailID")
    @Expose
    private String transactionDetailID;

    public String getTransactionDetailID() {
        return transactionDetailID;
    }

    public void setTransactionDetailID(String transactionDetailID) {
        this.transactionDetailID = transactionDetailID;
    }
}
