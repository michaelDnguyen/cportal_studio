package com.dlvn.mcustomerportal.services.model.claims;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SyncClaimDetailResult {

    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ErrLog")
    @Expose
    private String errLog;
    @SerializedName("NewAPIToken")
    @Expose
    private String newAPIToken;
    @SerializedName("ClientProfile")
    @Expose
    private List<SyncCLaimDetailModel> lsClaimDetail = null;

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

    public String getNewAPIToken() {
        return newAPIToken;
    }

    public void setNewAPIToken(String newAPIToken) {
        this.newAPIToken = newAPIToken;
    }

    public List<SyncCLaimDetailModel> getLsClaimDetail() {
        return lsClaimDetail;
    }

    public void setLsClaimDetail(List<SyncCLaimDetailModel> lsClaimDetail) {
        this.lsClaimDetail = lsClaimDetail;
    }
}
