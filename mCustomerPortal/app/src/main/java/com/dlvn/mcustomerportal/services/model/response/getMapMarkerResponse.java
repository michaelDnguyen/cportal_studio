package com.dlvn.mcustomerportal.services.model.response;

import com.dlvn.mcustomerportal.services.model.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author nn.tai
 * @date Dec 20, 2017
 */
public class getMapMarkerResponse extends BaseResponse {
	
	@SerializedName("GetMapMarkerResult")
	@Expose
	private getMapMarkerResult getMapMarkerResult;

	public getMapMarkerResult getGetMapMarkerResult() {
		return getMapMarkerResult;
	}

	public void setGetMapMarkerResult(getMapMarkerResult getMapMarkerResult) {
		this.getMapMarkerResult = getMapMarkerResult;
	}
}
