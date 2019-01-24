package com.dlvn.mcustomerportal.services.model.claims;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocTypeComment {

    @SerializedName("DocTypeID")
    @Expose
    private String docTypeID;
    @SerializedName("WFAddDocID")
    @Expose
    private String wfAddDocID;
    @SerializedName("SubDocID")
    @Expose
    private String subDocID;
    @SerializedName("ClientComment")
    @Expose
    private String clientComment;
    @SerializedName("Status")
    @Expose
    private String Status;

    public String getDocTypeID() {
        return docTypeID;
    }

    public void setDocTypeID(String docTypeID) {
        this.docTypeID = docTypeID;
    }

    public String getWfAddDocID() {
        return wfAddDocID;
    }

    public void setWfAddDocID(String wfAddDocID) {
        this.wfAddDocID = wfAddDocID;
    }

    public String getSubDocID() {
        return subDocID;
    }

    public void setSubDocID(String subDocID) {
        this.subDocID = subDocID;
    }

    public String getClientComment() {
        return clientComment;
    }

    public void setClientComment(String clientComment) {
        this.clientComment = clientComment;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public DocTypeComment(String docTypeID, String wfAddDocID, String subDocID, String clientComment) {
        this.docTypeID = docTypeID;
        this.wfAddDocID = wfAddDocID;
        this.subDocID = subDocID;
        this.clientComment = clientComment;
    }

    public DocTypeComment(String docTypeID, String wfAddDocID, String subDocID, String clientComment, String status) {
        this.docTypeID = docTypeID;
        this.wfAddDocID = wfAddDocID;
        this.subDocID = subDocID;
        this.clientComment = clientComment;
        Status = status;
    }
}
