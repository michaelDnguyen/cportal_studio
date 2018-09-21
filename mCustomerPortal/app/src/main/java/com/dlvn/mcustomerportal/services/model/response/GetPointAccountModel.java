package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPointAccountModel {

    @SerializedName("PointAcountID")
    @Expose
    private Integer pointAcountID;
    @SerializedName("Point")
    @Expose
    private Integer point;
    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @SerializedName("TransTypeCD")
    @Expose
    private String transTypeCD;
    @SerializedName("CreateDate")
    @Expose
    private String createDate;
    @SerializedName("Description")
    @Expose
    private String description;

    public Integer getPointAcountID() {
        return pointAcountID;
    }

    public void setPointAcountID(Integer pointAcountID) {
        this.pointAcountID = pointAcountID;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getTransTypeCD() {
        return transTypeCD;
    }

    public void setTransTypeCD(String transTypeCD) {
        this.transTypeCD = transTypeCD;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
