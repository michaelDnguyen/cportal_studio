package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPointAccountByCLIIDResponse {

    @SerializedName("CPGetPointAccountByCLIIDResult")
    @Expose
    private GetPointAccountByCLIIDResult cPGetPointAccountByCLIIDResult;

    public GetPointAccountByCLIIDResult getCPGetPointAccountByCLIIDResult() {
        return cPGetPointAccountByCLIIDResult;
    }

    public void setCPGetPointAccountByCLIIDResult(GetPointAccountByCLIIDResult cPGetPointAccountByCLIIDResult) {
        this.cPGetPointAccountByCLIIDResult = cPGetPointAccountByCLIIDResult;
    }

}
