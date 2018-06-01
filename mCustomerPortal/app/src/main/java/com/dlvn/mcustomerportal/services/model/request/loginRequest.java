package com.dlvn.mcustomerportal.services.model.request;

import com.dlvn.mcustomerportal.services.model.JsonDataInput;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class loginRequest extends JsonDataInput {

	@SerializedName("AgentId")
	@Expose
	private String agentId;
	@SerializedName("Password")
	@Expose
	private String password;
	@SerializedName("DeviceId")
	@Expose
	private String deviceId;
	@SerializedName("Project")
	@Expose
	private String project;
	@SerializedName("DeviceToken")
	@Expose
	private String deviceToken;

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
}
