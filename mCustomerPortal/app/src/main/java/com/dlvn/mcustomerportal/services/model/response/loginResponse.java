package com.dlvn.mcustomerportal.services.model.response;

import java.util.List;

import com.dlvn.mcustomerportal.services.model.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class loginResponse extends BaseResponse {

	@SerializedName("PDLPadAuthLoginResult")
	@Expose
	private List<loginResult> pDLPadAuthLoginResult = null;

	public List<loginResult> getPDLPadAuthLoginResult() {
		return pDLPadAuthLoginResult;
	}

	public void setPDLPadAuthLoginResult(List<loginResult> pDLPadAuthLoginResult) {
		this.pDLPadAuthLoginResult = pDLPadAuthLoginResult;
	}
}
