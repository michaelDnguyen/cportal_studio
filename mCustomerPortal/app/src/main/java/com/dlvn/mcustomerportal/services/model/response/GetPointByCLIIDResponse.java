package com.dlvn.mcustomerportal.services.model.response;

import com.dlvn.mcustomerportal.services.model.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPointByCLIIDResponse extends BaseResponse{

    @SerializedName("CPGetPointByCLIIDResult")
    @Expose
    private GetPointByCLIIDResult response;

    public GetPointByCLIIDResult getResponse() {
        return response;
    }

    public void setResponse(GetPointByCLIIDResult response) {
        this.response = response;
    }
}
