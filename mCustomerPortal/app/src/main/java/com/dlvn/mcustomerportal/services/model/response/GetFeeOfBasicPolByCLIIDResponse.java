package com.dlvn.mcustomerportal.services.model.response;

import com.dlvn.mcustomerportal.services.model.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFeeOfBasicPolByCLIIDResponse extends BaseResponse {

    @SerializedName("CPGetFeeOfBasicPolByCLIIDResult")
    @Expose
    private GetFeeOfBasicPolByCLIIDResult response;

    public GetFeeOfBasicPolByCLIIDResult getResponse() {
        return response;
    }

    public void setResponse(GetFeeOfBasicPolByCLIIDResult response) {
        this.response = response;
    }

}
