package com.dlvn.mcustomerportal.services.model.request;

import com.dlvn.mcustomerportal.services.model.JsonDataInput;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author nn.tai
 * @date Dec 20, 2017
 */
public class getMapMarkerRequest extends JsonDataInput {
	
	@SerializedName("AgentID")
	@Expose
	private String agentId;
	@SerializedName("Password")
	@Expose
	private String password;
	@SerializedName("DeviceID")
	@Expose
	private String deviceId;
	@SerializedName("Lat")
	@Expose
	private String lat;
	@SerializedName("DeviceName")
	@Expose
	private String deviceName;
	@SerializedName("Lng")
	@Expose
	private String lng;
	@SerializedName("TypeOffice")
	@Expose
	private String typeOffice;
	@SerializedName("APIToken")
	@Expose
	private String aPIToken;

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

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getTypeOffice() {
		return typeOffice;
	}

	public void setTypeOffice(String typeOffice) {
		this.typeOffice = typeOffice;
	}

	public String getAPIToken() {
		return aPIToken;
	}

	public void setAPIToken(String aPIToken) {
		this.aPIToken = aPIToken;
	}
}
