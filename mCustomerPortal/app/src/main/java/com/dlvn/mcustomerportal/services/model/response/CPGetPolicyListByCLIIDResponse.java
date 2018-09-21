package com.dlvn.mcustomerportal.services.model.response;

import com.dlvn.mcustomerportal.services.model.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CPGetPolicyListByCLIIDResponse extends BaseResponse {

	@SerializedName("Response")
	@Expose
	private CPGetPolicyListByCLIIDResult response;

	public CPGetPolicyListByCLIIDResult getResponse() {
		return response;
	}

	public void setResponse(CPGetPolicyListByCLIIDResult response) {
		this.response = response;
	}
}
