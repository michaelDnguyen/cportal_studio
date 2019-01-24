package com.dlvn.mcustomerportal.services.model.claims;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SyncClaimDocTypeHold {

    @SerializedName("SubmissionID")
    @Expose
    private String submissionID;
    @SerializedName("lstDocType")
    @Expose
    private List<SyncClaimDocType> lstDocType = null;

    public String getSubmissionID() {
        return submissionID;
    }

    public void setSubmissionID(String submissionID) {
        this.submissionID = submissionID;
    }

    public List<SyncClaimDocType> getLstDocType() {
        return lstDocType;
    }

    public void setLstDocType(List<SyncClaimDocType> lstDocType) {
        this.lstDocType = lstDocType;
    }
}
