package com.dlvn.mcustomerportal.services.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CPPolicy implements Parcelable {

    @SerializedName("PolicyID")
    @Expose
    private String policyID;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("PolicyStatus")
    @Expose
    private String policyStatus;
    @SerializedName("PolicyStatusCode")
    @Expose
    private String policyStatusCode;
    @SerializedName("FaceAmount")
    @Expose
    private String faceAmount;
    @SerializedName("PolExpiryDate")
    @Expose
    private String polExpiryDate;
    @SerializedName("PolIssEffDate")
    @Expose
    private String polIssEffDate;

    public String getPolicyID() {
        return policyID;
    }

    public void setPolicyID(String policyID) {
        this.policyID = policyID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPolicyStatus() {
        return policyStatus;
    }

    public void setPolicyStatus(String policyStatus) {
        this.policyStatus = policyStatus;
    }

    public String getFaceAmount() {
        return faceAmount;
    }

    public void setFaceAmount(String faceAmount) {
        this.faceAmount = faceAmount;
    }

    public String getPolExpiryDate() {
        return polExpiryDate;
    }

    public void setPolExpiryDate(String polExpiryDate) {
        this.polExpiryDate = polExpiryDate;
    }

    public String getPolicyStatusCode() {
        return policyStatusCode;
    }

    public void setPolicyStatusCode(String policyStatusCode) {
        this.policyStatusCode = policyStatusCode;
    }

    public String getPolIssEffDate() {
        return polIssEffDate;
    }

    public void setPolIssEffDate(String polIssEffDate) {
        this.polIssEffDate = polIssEffDate;
    }

    /**
     * Contructor for implement Parcelable
     *
     * @param in
     */
    public CPPolicy(Parcel in) {
        policyID = in.readString();
        policyStatus = in.readString();
        policyStatusCode = in.readString();
        productName = in.readString();
        faceAmount = in.readString();
        polExpiryDate = in.readString();
        polIssEffDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(policyID);
        dest.writeString(policyStatus);
        dest.writeString(policyStatusCode);
        dest.writeString(productName);
        dest.writeString(faceAmount);
        dest.writeString(polExpiryDate);
        dest.writeString(polIssEffDate);
    }

    public static final Creator<CPPolicy> CREATOR = new Creator<CPPolicy>() {
        @Override
        public CPPolicy createFromParcel(Parcel source) {
            return new CPPolicy(source);
        }

        @Override
        public CPPolicy[] newArray(int size) {
            return new CPPolicy[size];
        }
    };
}
