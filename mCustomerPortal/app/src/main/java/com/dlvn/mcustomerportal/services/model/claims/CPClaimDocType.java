package com.dlvn.mcustomerportal.services.model.claims;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CPClaimDocType {

    @SerializedName("DocID")
    @Expose
    private String docID;
    @SerializedName("DocTypeID")
    @Expose
    private String docTypeID;
    @SerializedName("DocTypeName")
    @Expose
    private String docTypeName;
    @SerializedName("DocName")
    @Expose
    private String docName;
    @SerializedName("Order")
    @Expose
    private String order;
    @SerializedName("SubDocOrder")
    @Expose
    private String subDocOrder;
    @SerializedName("Used")
    @Expose
    private String used;
    @SerializedName("SubDocId")
    @Expose
    private String subDocId;

    /**
     * admin comment
     *
     * @return
     */
    @SerializedName("ClaimAdminComment")
    @Expose
    private String claimAdminComment;

    @SerializedName("WFAddDocID")
    @Expose
    private String WFAddDocID;

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

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

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSubDocOrder() {
        return subDocOrder;
    }

    public void setSubDocOrder(String subDocOrder) {
        this.subDocOrder = subDocOrder;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getSubDocId() {
        return subDocId;
    }

    public void setSubDocId(String subDocId) {
        this.subDocId = subDocId;
    }

    public String getClaimAdminComment() {
        return claimAdminComment;
    }

    public void setClaimAdminComment(String claimAdminComment) {
        this.claimAdminComment = claimAdminComment;
    }

    public String getWFAddDocID() {
        return WFAddDocID;
    }

    public void setWFAddDocID(String WFAddDocID) {
        this.WFAddDocID = WFAddDocID;
    }
}
