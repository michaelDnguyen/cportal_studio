package com.dlvn.mcustomerportal.services.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFeeByClientIDRequest {

    @SerializedName("ClientID")
    @Expose
    private String clientID;

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }
}
