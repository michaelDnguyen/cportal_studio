package com.dlvn.mcustomerportal.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Data model for container document/image
 */
@Entity
public class DocumentEntity implements Parcelable {

    public static final String STATUS_UPLOADING = "UPLOADING";
    public static final String STATUS_UPLOAD_SUCCESS = "UPLOAD-SUCCESS";
    public static final String STATUS_UPLOAD_FAILED = "UPLOAD-FAILED";
    public static final String STATUS_NOT_UPLOAD = "NOT-UPLOAD";

    @PrimaryKey(autoGenerate = true)
    long id;
    //ID của item in table ClaimDocEntity
    @ColumnInfo(name = "DocEntityID")
    long docEntityID;
    @ColumnInfo(name = "Path")
    String Path;
    @ColumnInfo(name = "Status")
    String Status;
    @ColumnInfo(name = "CreateDate")
    String CreateDate;
    @ColumnInfo(name = "UpdateDate")
    String UpdatedDate;
    @ColumnInfo(name = "Note")
    String note;
    @ColumnInfo(name = "NumSort")
    int numSort;

    public DocumentEntity() {
        super();
        this.id = 0;
        this.docEntityID = 0;
        Path = "";
        Status = "";
        CreateDate = "";
        UpdatedDate = "";
        this.note = "";
        numSort = -1;
    }

    public DocumentEntity(int id, int docID, String path, String status, String createDate, String updatedDate,
                          String note, int numeric) {
        super();
        this.id = id;
        this.docEntityID = docID;
        Path = path;
        Status = status;
        CreateDate = createDate;
        UpdatedDate = updatedDate;
        this.note = note;
        this.numSort = numeric;
    }

    public DocumentEntity(Parcel in) {
        id = in.readInt();
        docEntityID = in.readInt();
        Path = in.readString();
        Status = in.readString();
        CreateDate = in.readString();
        UpdatedDate = in.readString();
        note = in.readString();
        numSort = in.readInt();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDocEntityID() {
        return docEntityID;
    }

    public void setDocEntityID(long docEntityID) {
        this.docEntityID = docEntityID;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getNumSort() {
        return numSort;
    }

    public void setNumSort(int numSort) {
        this.numSort = numSort;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeLong(docEntityID);
        dest.writeString(Path);
        dest.writeString(Status);
        dest.writeString(CreateDate);
        dest.writeString(UpdatedDate);
        dest.writeString(note);
        dest.writeInt(numSort);
    }

    public static final Creator<DocumentEntity> CREATOR = new Creator<DocumentEntity>() {
        @Override
        public DocumentEntity createFromParcel(Parcel in) {
            return new DocumentEntity(in);
        }

        @Override
        public DocumentEntity[] newArray(int size) {
            return new DocumentEntity[size];
        }
    };
}
