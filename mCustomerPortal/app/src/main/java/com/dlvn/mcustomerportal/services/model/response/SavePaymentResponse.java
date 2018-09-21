package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SavePaymentResponse {

    @SerializedName("SavePayment")
    @Expose
    private SavePaymentResult savePayment;

    public SavePaymentResult getSavePayment() {
        return savePayment;
    }

    public void setSavePayment(SavePaymentResult savePayment) {
        this.savePayment = savePayment;
    }
}
