package com.dlvn.mcustomerportal.services.model.response;

import java.util.List;

import com.dlvn.mcustomerportal.adapter.model.OfficeAddressModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class getMapMarkerResult {

	@SerializedName("Result")
	@Expose
	private String result;
	@SerializedName("ErrLog")
	@Expose
	private String errLog;
	@SerializedName("dtProposal")
	@Expose
	private List<OfficeAddressModel> dtProposal = null;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrLog() {
		return errLog;
	}

	public void setErrLog(String errLog) {
		this.errLog = errLog;
	}

	public List<OfficeAddressModel> getDtProposal() {
		return dtProposal;
	}

	public void setDtProposal(List<OfficeAddressModel> dtProposal) {
		this.dtProposal = dtProposal;
	}

}
