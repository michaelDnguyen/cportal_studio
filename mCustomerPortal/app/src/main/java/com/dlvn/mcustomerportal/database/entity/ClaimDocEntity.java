package com.dlvn.mcustomerportal.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class ClaimDocEntity implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    long id;
    /**
     * Claims ID is id in tb ClaimsEntity, not claimID in server
     */
    @ColumnInfo(name = "ClaimEntityID")
    long claimsEntityID;
    @ColumnInfo(name = "SubDocID")
    String SubDocID;
    @ColumnInfo(name = "DocTypeID")
    String DocTypeID;
    @ColumnInfo(name = "DocTypeName")
    String DocTypeName;
    @ColumnInfo(name = "DocID")
    String DocID;
    @ColumnInfo(name = "DocName")
    String DocName;
    @ColumnInfo(name = "CreateDate")
    String createDate;
    @ColumnInfo(name = "UpdateDate")
    String updateDate;
    @ColumnInfo(name = "Note")
    String note;
    @ColumnInfo(name = "Order")
    String order;
    @ColumnInfo(name = "SubOrder")
    String subOrder;
    @ColumnInfo(name = "Used")
    String used;

    /**
     * Column for comment
     */
    @ColumnInfo(name = "ClaimAdminComment")
    String claimAdminComment;
    @ColumnInfo(name = "ClaimClientComment")
    String claimClientComment;
    /**
     * For supplement claim
     */
    @ColumnInfo(name = "WFAddDocID")
    String wfAddDocID;

    public ClaimDocEntity() {
        super();
        this.id = 0;
        this.claimsEntityID = 0;
        this.SubDocID = "";
        this.DocTypeID = "";
        this.DocTypeName = "";
        this.DocID = "";
        this.DocName = "";
        this.createDate = "";
        this.updateDate = "";
        this.note = "";
        this.order = "0";
        this.subOrder = "0";
        this.used = "0";
        this.claimAdminComment = "";
        this.claimClientComment = "";
        this.wfAddDocID = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClaimsEntityID() {
        return claimsEntityID;
    }

    public void setClaimsEntityID(long claimsID) {
        this.claimsEntityID = claimsID;
    }

    public String getSubDocID() {
        return SubDocID;
    }

    public void setSubDocID(String subDocID) {
        SubDocID = subDocID;
    }

    public String getDocTypeID() {
        return DocTypeID;
    }

    public void setDocTypeID(String docTypeID) {
        DocTypeID = docTypeID;
    }

    public String getDocTypeName() {
        return DocTypeName;
    }

    public void setDocTypeName(String docTypeName) {
        DocTypeName = docTypeName;
    }

    public String getDocID() {
        return DocID;
    }

    public void setDocID(String docID) {
        DocID = docID;
    }

    public String getDocName() {
        return DocName;
    }

    public void setDocName(String docName) {
        DocName = docName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSubOrder() {
        return subOrder;
    }

    public void setSubOrder(String subOrder) {
        this.subOrder = subOrder;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getClaimAdminComment() {
        return claimAdminComment;
    }

    public void setClaimAdminComment(String claimAdminComment) {
        this.claimAdminComment = claimAdminComment;
    }

    public String getClaimClientComment() {
        return claimClientComment;
    }

    public void setClaimClientComment(String claimClientComment) {
        this.claimClientComment = claimClientComment;
    }

    public String getWfAddDocID() {
        return wfAddDocID;
    }

    public void setWfAddDocID(String wfAddDocID) {
        this.wfAddDocID = wfAddDocID;
    }

    /**
     * Parcelling part
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(claimsEntityID);
        dest.writeString(SubDocID);
        dest.writeString(DocTypeID);
        dest.writeString(DocTypeName);
        dest.writeString(DocID);
        dest.writeString(DocName);
        dest.writeString(createDate);
        dest.writeString(updateDate);
        dest.writeString(note);
        dest.writeString(order);
        dest.writeString(subOrder);
        dest.writeString(used);
        dest.writeString(claimAdminComment);
        dest.writeString(claimClientComment);
        dest.writeString(wfAddDocID);
    }

    public ClaimDocEntity(Parcel in) {
        this.id = in.readInt();
        this.claimsEntityID = in.readInt();
        this.SubDocID = in.readString();
        this.DocTypeID = in.readString();
        this.DocID = in.readString();
        this.DocName = in.readString();
        this.createDate = in.readString();
        this.updateDate = in.readString();
        this.note = in.readString();
        this.order = in.readString();
        this.subOrder = in.readString();
        this.used = in.readString();
        this.claimAdminComment = in.readString();
        this.claimClientComment = in.readString();
        this.wfAddDocID = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ClaimDocEntity createFromParcel(Parcel in) {
            return new ClaimDocEntity(in);
        }

        public ClaimDocEntity[] newArray(int size) {
            return new ClaimDocEntity[size];
        }
    };
}
