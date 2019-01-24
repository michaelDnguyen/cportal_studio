package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMasterData_Product_Response {

    @SerializedName("GetMasterDataByTypeResult")
    @Expose
    private GetMaterData_Product_Result getMasterDataByTypeResult;

    public GetMaterData_Product_Result getGetMasterDataByTypeResult() {
        return getMasterDataByTypeResult;
    }

    public void setGetMasterDataByTypeResult(GetMaterData_Product_Result getMasterDataByTypeResult) {
        this.getMasterDataByTypeResult = getMasterDataByTypeResult;
    }
}
