package com.dlvn.mcustomerportal.services.model.request;

import com.dlvn.mcustomerportal.services.model.JsonDataInput;
import com.dlvn.mcustomerportal.services.model.response.ProductLoyaltyModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoyaltyPointConformRequest extends JsonDataInput {

    @SerializedName("APIToken")
    @Expose
    private String aPIToken;
    @SerializedName("Authentication")
    @Expose
    private String authentication;
    @SerializedName("ClientID")
    @Expose
    private String clientID;
    @SerializedName("DeviceId")
    @Expose
    private String deviceID;
    @SerializedName("DeviceToken")
    @Expose
    private String deviceToken;
    @SerializedName("OS")
    @Expose
    private String OS;
    @SerializedName("Project")
    @Expose
    private String project;
    @SerializedName("UserLogin")
    @Expose
    private String userLogin;
    @SerializedName("shippingFirstName")
    @Expose
    private String shippingFirstName;
    @SerializedName("shippingLastName")
    @Expose
    private String shippingLastName;
    @SerializedName("shippingPhone")
    @Expose
    private String shippingPhone;
    @SerializedName("shippingAddress")
    @Expose
    private String shippingAddress;
    @SerializedName("shippingWard")
    @Expose
    private String shippingWard;
    @SerializedName("shippingCity")
    @Expose
    private String shippingCity;
    @SerializedName("shippingDistrict")
    @Expose
    private String shippingDistrict;
    @SerializedName("deliveryFeeGros")
    @Expose
    private String deliveryFeeGros;
    @SerializedName("deliveryPhone")
    @Expose
    private String deliveryPhone;
    @SerializedName("emailconfirm")
    @Expose
    private String emailconfirm;
    @SerializedName("smsconfirm")
    @Expose
    private String smsconfirm;
    @SerializedName("productItems")
    @Expose
    private List<ProductLoyaltyModel> productItems = null;
    @SerializedName("policyno")
    @Expose
    private String policyno;
    @SerializedName("category")
    @Expose
    private String category;

    public String getAPIToken() {
        return aPIToken;
    }

    public void setAPIToken(String aPIToken) {
        this.aPIToken = aPIToken;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getShippingFirstName() {
        return shippingFirstName;
    }

    public void setShippingFirstName(String shippingFirstName) {
        this.shippingFirstName = shippingFirstName;
    }

    public String getShippingLastName() {
        return shippingLastName;
    }

    public void setShippingLastName(String shippingLastName) {
        this.shippingLastName = shippingLastName;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingWard() {
        return shippingWard;
    }

    public void setShippingWard(String shippingWard) {
        this.shippingWard = shippingWard;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingDistrict() {
        return shippingDistrict;
    }

    public void setShippingDistrict(String shippingDistrict) {
        this.shippingDistrict = shippingDistrict;
    }

    public String getDeliveryFeeGros() {
        return deliveryFeeGros;
    }

    public void setDeliveryFeeGros(String deliveryFeeGros) {
        this.deliveryFeeGros = deliveryFeeGros;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public String getEmailconfirm() {
        return emailconfirm;
    }

    public void setEmailconfirm(String emailconfirm) {
        this.emailconfirm = emailconfirm;
    }

    public String getSmsconfirm() {
        return smsconfirm;
    }

    public void setSmsconfirm(String smsconfirm) {
        this.smsconfirm = smsconfirm;
    }

    public List<ProductLoyaltyModel> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<ProductLoyaltyModel> productItems) {
        this.productItems = productItems;
    }

    public String getPolicyno() {
        return policyno;
    }

    public void setPolicyno(String policyno) {
        this.policyno = policyno;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }
}
