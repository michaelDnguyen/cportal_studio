package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPointAccountByCLIIDResult {

    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("NewAPIToken")
    @Expose
    private String newAPIToken;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("ClientProfile")
    @Expose
    private List<GetPointAccountModel> pointAccounts = null;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public List<GetPointAccountModel> getPointAccounts() {
        return pointAccounts;
    }

    public void setPointAccounts(List<GetPointAccountModel> pointAccounts) {
        this.pointAccounts = pointAccounts;
    }
}
