package com.dlvn.mcustomerportal.services.model.response;

import com.dlvn.mcustomerportal.adapter.model.TransactionDetailModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryPaymentDetailResult {

    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ErrLog")
    @Expose
    private String errLog;
    @SerializedName("dtProposal")
    @Expose
    private List<TransactionDetailModel> lstData = null;

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

    public List<TransactionDetailModel> getListHistoryDetail() {
        return lstData;
    }

    public void setListHistoryDetail(List<TransactionDetailModel> dtProposal) {
        this.lstData = dtProposal;
    }
}
