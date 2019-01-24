package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetClientProfileByCLIIDResponse {

    @SerializedName("Response")
    @Expose
    private GetClientProfileByCLIIDResult response;

    public GetClientProfileByCLIIDResult getResponse() {
        return response;
    }

    public void setResponse(GetClientProfileByCLIIDResult response) {
        this.response = response;
    }
}
