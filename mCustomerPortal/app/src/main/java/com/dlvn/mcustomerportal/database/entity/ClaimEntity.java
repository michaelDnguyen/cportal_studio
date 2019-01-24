package com.dlvn.mcustomerportal.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.dlvn.mcustomerportal.adapter.PointAccountAdapter;

@Entity
public class ClaimEntity implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    long id;
    @ColumnInfo(name = "ClaimsID")
    String claimsID;
    @ColumnInfo(name = "SubmissionID")
    long submissionID;
    @ColumnInfo(name = "ClaimsType")
    String claimsType;
    @ColumnInfo(name = "ClaimsName")
    String claimsName;
    @ColumnInfo(name = "mainClaimID")
    long mainClaimID;
    @ColumnInfo(name = "PolicyNo")
    String policyNo;

    /**
     * Status of claims item follow by @Constant.StatusSubmit
     */
    @ColumnInfo(name = "Status")
    String Status;
    @ColumnInfo(name = "CreateDate")
    String createDate;
    @ColumnInfo(name = "UpdateDate")
    String updateDate;
    @ColumnInfo(name = "Note")
    String note;

    private String titleGroup;
    private int codeGroup = PointAccountAdapter.CODE_ITEM;

    public ClaimEntity() {
        //default is no supplement
        mainClaimID = 0;
    }

    public ClaimEntity(String title, int code) {
        this.titleGroup = title;
        this.codeGroup = code;
    }

    public ClaimEntity(Parcel in) {
        id = in.readInt();
        claimsID = in.readString();
        submissionID = in.readLong();
        claimsType = in.readString();
        claimsName = in.readString();
        mainClaimID = in.readLong();
        policyNo = in.readString();
        Status = in.readString();
        createDate = in.readString();
        updateDate = in.readString();
        note = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(claimsID);
        dest.writeLong(submissionID);
        dest.writeString(claimsType);
        dest.writeString(claimsName);
        dest.writeLong(mainClaimID);
        dest.writeString(policyNo);
        dest.writeString(Status);
        dest.writeString(createDate);
        dest.writeString(updateDate);
        dest.writeString(note);
    }

    public static final Creator<ClaimEntity> CREATOR = new Creator<ClaimEntity>() {
        @Override
        public ClaimEntity createFromParcel(Parcel source) {
            return new ClaimEntity(source);
        }

        @Override
        public ClaimEntity[] newArray(int size) {
            return new ClaimEntity[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClaimsID() {
        return claimsID;
    }

    public void setClaimsID(String claimsID) {
        this.claimsID = claimsID;
    }

    public long getSubmissionID() {
        return submissionID;
    }

    public void setSubmissionID(long submissionID) {
        this.submissionID = submissionID;
    }

    public String getClaimsType() {
        return claimsType;
    }

    public void setClaimsType(String claimsType) {
        this.claimsType = claimsType;
    }

    public String getClaimsName() {
        return claimsName;
    }

    public void setClaimsName(String claimsName) {
        this.claimsName = claimsName;
    }

    public long getMainClaimID() {
        return mainClaimID;
    }

    public void setMainClaimID(long mainClaimID) {
        this.mainClaimID = mainClaimID;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
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

    /**
     * For title group / code group in list adapter
     *
     * @return
     */
    public String getTitleGroup() {
        return titleGroup;
    }

    public void setTitleGroup(String titleGroup) {
        this.titleGroup = titleGroup;
    }

    public int getCodeGroup() {
        return codeGroup;
    }

    public void setCodeGroup(int codeGroup) {
        this.codeGroup = codeGroup;
    }
}
