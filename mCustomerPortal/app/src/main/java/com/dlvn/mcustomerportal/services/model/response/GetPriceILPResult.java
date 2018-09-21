package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPriceILPResult {

    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ErrLog")
    @Expose
    private String errLog;
    @SerializedName("dtProposal")
    @Expose
    private List<PriceILPModel> dtProposal = null;

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

    public List<PriceILPModel> getDtProposal() {
        return dtProposal;
    }

    public void setDtProposal(List<PriceILPModel> dtProposal) {
        this.dtProposal = dtProposal;
    }
}
