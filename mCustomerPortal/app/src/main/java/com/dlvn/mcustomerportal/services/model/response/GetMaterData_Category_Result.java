package com.dlvn.mcustomerportal.services.model.response;

import com.dlvn.mcustomerportal.utils.myLog;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetMaterData_Category_Result {

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
    private List<MasterData_Category> lstItem = null;

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

    public List<MasterData_Category> getLstItem() {
        return lstItem;
    }

    public void setLstItem(List<MasterData_Category> lstItem) {
        this.lstItem = lstItem;
    }

}
