package com.dlvn.mcustomerportal.services.model.response;

import com.dlvn.mcustomerportal.adapter.model.ProductDetailModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetMaterData_Product_Result {

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
    private List<ProductDetailModel> lstItem = null;

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

    public List<ProductDetailModel> getLstItem() {
        return lstItem;
    }

    public void setLstItem(List<ProductDetailModel> lstItem) {
        this.lstItem = lstItem;
    }

}
