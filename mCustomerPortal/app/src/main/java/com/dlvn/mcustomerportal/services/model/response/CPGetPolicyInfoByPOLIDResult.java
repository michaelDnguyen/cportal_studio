package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CPGetPolicyInfoByPOLIDResult {

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
    private List<CPPolicyInfo> cpPolicyInfos = null;

    /**
     * Action PolicyClient
     */
    @SerializedName("PolicyInfo")
    @Expose
    private List<CPPolicyInfoForPayment> policyInfo = null;

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

    public List<CPPolicyInfo> getCpPolicyInfos() {
        return cpPolicyInfos;
    }

    public void setCpPolicyInfos(List<CPPolicyInfo> cpPolicyInfos) {
        this.cpPolicyInfos = cpPolicyInfos;
    }

    /**
     * For Action PolicyClient
     * @return
     */
    public List<CPPolicyInfoForPayment> getPolicyInfo() {
        return policyInfo;
    }

    public void setPolicyInfo(List<CPPolicyInfoForPayment> policyInfo) {
        this.policyInfo = policyInfo;
    }

}
