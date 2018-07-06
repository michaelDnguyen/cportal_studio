package com.dlvn.mcustomerportal.services.model.response;

import com.dlvn.mcustomerportal.services.model.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class loginNewResponse extends BaseResponse {

	@SerializedName("Response")
	@Expose
	private loginNewResult response;

	public loginNewResult getResponse() {
		return response;
	}

	public void setResponse(loginNewResult response) {
		this.response = response;
	}
}
