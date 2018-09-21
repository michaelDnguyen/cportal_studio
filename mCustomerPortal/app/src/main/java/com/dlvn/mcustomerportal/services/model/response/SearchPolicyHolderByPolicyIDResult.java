package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchPolicyHolderByPolicyIDResult {
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
    private List<SearchPolicyHolderModel> lsSearchPolicyHolder = null;

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

    public List<SearchPolicyHolderModel> getLsSearchPolicyHolder() {
        return lsSearchPolicyHolder;
    }

    public void setLsSearchPolicyHolder(List<SearchPolicyHolderModel> lsSearchPolicyHolder) {
        this.lsSearchPolicyHolder = lsSearchPolicyHolder;
    }

}
