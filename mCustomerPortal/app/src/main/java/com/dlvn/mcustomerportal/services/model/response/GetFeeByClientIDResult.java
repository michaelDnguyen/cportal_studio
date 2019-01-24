package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetFeeByClientIDResult {

    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ErrLog")
    @Expose
    private String errLog;
    @SerializedName("dtProposal")
    @Expose
    private List<GetFeeByClientIDModel> lstFeeByPolicy = null;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrLog() {
        return errLog;
    }

    public void setErrLog(String errLog) {
        this.errLog = errLog;
    }

    public List<GetFeeByClientIDModel> getLstFeeByPolicy() {
        return lstFeeByPolicy;
    }

    public void setLstFeeByPolicy(List<GetFeeByClientIDModel> lstFeeByPolicy) {
        this.lstFeeByPolicy = lstFeeByPolicy;
    }
}
