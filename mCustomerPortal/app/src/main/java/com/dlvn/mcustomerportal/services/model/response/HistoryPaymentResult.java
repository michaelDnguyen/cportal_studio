package com.dlvn.mcustomerportal.services.model.response;

import com.dlvn.mcustomerportal.adapter.model.TransactionModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryPaymentResult {

    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ErrLog")
    @Expose
    private String errLog;
    @SerializedName("dtProposal")
    @Expose
    private List<TransactionModel> lstHistory = null;

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

    public List<TransactionModel> getListHistory() {
        return lstHistory;
    }

    public void setListHistory(List<TransactionModel> dtProposal) {
        this.lstHistory = dtProposal;
    }
}
