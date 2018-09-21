package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMasterData_City_Response {

    @SerializedName("GetMasterDataByTypeResult")
    @Expose
    private GetMasterData_City_Result getMasterDataByTypeResult;

    public GetMasterData_City_Result getGetMasterDataByTypeResult() {
        return getMasterDataByTypeResult;
    }

    public void setGetMasterDataByTypeResult(GetMasterData_City_Result getMasterDataByTypeResult) {
        this.getMasterDataByTypeResult = getMasterDataByTypeResult;
    }
}
