package com.dlvn.mcustomerportal.services.model.response;

import com.dlvn.mcustomerportal.services.model.BaseResponse;
import com.dlvn.mcustomerportal.services.model.request.CPGetPolicyInfoByPOLIDRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CPGetPolicyInfoByPOLIDResponse extends BaseResponse {

	@SerializedName("Response")
	@Expose
	private CPGetPolicyInfoByPOLIDResult response;

	public CPGetPolicyInfoByPOLIDResult getResponse() {
		return response;
	}

	public void setResponse(CPGetPolicyInfoByPOLIDResult response) {
		this.response = response;
	}
}
