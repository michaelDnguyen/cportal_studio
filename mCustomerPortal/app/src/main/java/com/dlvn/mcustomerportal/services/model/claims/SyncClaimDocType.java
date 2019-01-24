package com.dlvn.mcustomerportal.services.model.claims;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SyncClaimDocType {

    @SerializedName("DocTypeID")
    @Expose
    private String docTypeID;
    @SerializedName("DocTypeName")
    @Expose
    private String docTypeName;
    @SerializedName("SubmissionID")
    @Expose
    private String submissionID;
    @SerializedName("lstURL")
    @Expose
    private List<String> lstURL = null;
    @SerializedName("UpdatedDate")
    @Expose
    private String updatedDate;

    public String getDocTypeID() {
        return docTypeID;
    }

    public void setDocTypeID(String docTypeID) {
        this.docTypeID = docTypeID;
    }

    public String getDocTypeName() {
        return docTypeName;
    }

    public void setDocTypeName(String docTypeName) {
        this.docTypeName = docTypeName;
    }

    public String getSubmissionID() {
        return submissionID;
    }

    public void setSubmissionID(String submissionID) {
        this.submissionID = submissionID;
    }

    public List<String> getLstURL() {
        return lstURL;
    }

    public void setLstURL(List<String> lstURL) {
        this.lstURL = lstURL;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}
