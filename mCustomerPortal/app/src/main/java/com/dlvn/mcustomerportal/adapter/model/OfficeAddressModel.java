package com.dlvn.mcustomerportal.adapter.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model data of map screen
 * 
 * @author nn.tai
 * @date Dec 18, 2017
 */
public class OfficeAddressModel {

	@SerializedName("ID")
	@Expose
	private Integer iD;
	@SerializedName("Name")
	@Expose
	private String name;
	@SerializedName("Address")
	@Expose
	private String address;
	@SerializedName("Type")
	@Expose
	private String type;
	@SerializedName("Phone")
	@Expose
	private String phone;
	@SerializedName("Mobile")
	@Expose
	private String mobile;
	@SerializedName("Fax")
	@Expose
	private String fax;
	@SerializedName("Distance")
	@Expose
	private Double distance;
	@SerializedName("Lat")
	@Expose
	private String lat;
	@SerializedName("Lng")
	@Expose
	private String lng;

	public Integer getID() {
		return iD;
	}

	public void setID(Integer iD) {
		this.iD = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

}
