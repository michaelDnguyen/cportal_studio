package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMasterData_Category_Response {

    @SerializedName("GetMasterDataByTypeResult")
    @Expose
    private GetMaterData_Category_Result getMasterDataByTypeResult;

    public GetMaterData_Category_Result getGetMasterDataByTypeResult() {
        return getMasterDataByTypeResult;
    }

    public void setGetMasterDataByTypeResult(GetMaterData_Category_Result getMasterDataByTypeResult) {
        this.getMasterDataByTypeResult = getMasterDataByTypeResult;
    }
}
