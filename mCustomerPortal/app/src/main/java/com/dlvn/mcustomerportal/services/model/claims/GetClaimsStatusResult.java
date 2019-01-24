package com.dlvn.mcustomerportal.services.model.claims;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetClaimsStatusResult {

    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ErrLog")
    @Expose
    private String errLog;
    @SerializedName("NewAPIToken")
    @Expose
    private String newAPIToken;
    @SerializedName("Message")
    @Expose
    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
