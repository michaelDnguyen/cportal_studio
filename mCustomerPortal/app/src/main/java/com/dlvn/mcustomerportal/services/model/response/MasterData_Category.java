package com.dlvn.mcustomerportal.services.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MasterData_Category implements Parcelable {

    @SerializedName("PRODUCTCATEGORYCD")
    @Expose
    private String pRODUCTCATEGORYCD;
    @SerializedName("PRODUCTCATEGORYNAME")
    @Expose
    private String pRODUCTCATEGORYNAME;
    @SerializedName("PCP_Code")
    @Expose
    private String pCPCode;

    private String productTitle;

    private String productContent;

    private int resourceID;

    /**
     * @param _title
     * @param _content
     * @param resourceID
     * @param category
     */
    public MasterData_Category(String _title, String _content, int resourceID, String category) {
        super();
        this.productTitle = _title;
        this.productContent = _content;
        this.resourceID = resourceID;
        this.pRODUCTCATEGORYCD = category;
    }

    public String getPRODUCTCATEGORYCD() {
        return pRODUCTCATEGORYCD;
    }

    public void setPRODUCTCATEGORYCD(String pRODUCTCATEGORYCD) {
        this.pRODUCTCATEGORYCD = pRODUCTCATEGORYCD;
    }

    public String getPRODUCTCATEGORYNAME() {
        return pRODUCTCATEGORYNAME;
    }

    public void setPRODUCTCATEGORYNAME(String pRODUCTCATEGORYNAME) {
        this.pRODUCTCATEGORYNAME = pRODUCTCATEGORYNAME;
    }

    public String getPCPCode() {
        return pCPCode;
    }

    public void setPCPCode(String pCPCode) {
        this.pCPCode = pCPCode;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductContent() {
        return productContent;
    }

    public void setProductContent(String productContent) {
        this.productContent = productContent;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public MasterData_Category() {
    }

    public MasterData_Category(Parcel in) {
        this.pRODUCTCATEGORYCD = in.readString();
        this.pRODUCTCATEGORYNAME = in.readString();
        this.pCPCode = in.readString();
        this.productTitle = in.readString();
        this.productContent = in.readString();
        this.resourceID = in.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pRODUCTCATEGORYCD);
        dest.writeString(pRODUCTCATEGORYNAME);
        dest.writeString(pCPCode);
        dest.writeString(productTitle);
        dest.writeString(productContent);
        dest.writeInt(resourceID);
    }

    public static final Creator<MasterData_Category> CREATOR = new Creator<MasterData_Category>() {
        @Override
        public MasterData_Category createFromParcel(Parcel source) {
            return new MasterData_Category(source);
        }

        @Override
        public MasterData_Category[] newArray(int size) {
            return new MasterData_Category[size];
        }
    };
}
