package com.dlvn.mcustomerportal.services.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author nn.tai
 * @create Nov 6, 2017
 */
public class BaseRequest {

	@SerializedName("jsonDataInput")
	@Expose
	private JsonDataInput jsonDataInput;

	/**
	 * 
	 * @return The jsonDataInput
	 */
	public JsonDataInput getJsonDataInput() {
		return jsonDataInput;
	}

	/**
	 * 
	 * @param jsonDataInput
	 * The jsonDataInput
	 */
	public void setJsonDataInput(JsonDataInput jsonDataInput) {
		this.jsonDataInput = jsonDataInput;
	}

	@Override
	public String toString() {
		return "BaseRequest [jsonDataInput=" + jsonDataInput.toString() + "]";
	}

	
}
