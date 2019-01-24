package com.dlvn.mcustomerportal.services.model.claims;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SyncClaimSub {

    @SerializedName("SubmissionID")
    @Expose
    private String submissionID;
    @SerializedName("ClaimID")
    @Expose
    private String claimID;
    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @SerializedName("PolicyNo")
    @Expose
    private String policyNo;
    @SerializedName("ClaimType")
    @Expose
    private String claimType;
    @SerializedName("ClaimAmt")
    @Expose
    private String claimAmt;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("DateSubmission")
    @Expose
    private String dateSubmission;
    @SerializedName("LIFullName")
    @Expose
    private String LIFullName;

    public String getSubmissionID() {
        return submissionID;
    }

    public void setSubmissionID(String submissionID) {
        this.submissionID = submissionID;
    }

    public String getClaimID() {
        return claimID;
    }

    public void setClaimID(String claimID) {
        this.claimID = claimID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getClaimAmt() {
        return claimAmt;
    }

    public void setClaimAmt(String claimAmt) {
        this.claimAmt = claimAmt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateSubmission() {
        return dateSubmission;
    }

    public void setDateSubmission(String dateSubmission) {
        this.dateSubmission = dateSubmission;
    }

    public String getLIFullName() {
        return LIFullName;
    }

    public void setLIFullName(String LIFullName) {
        this.LIFullName = LIFullName;
    }
}
