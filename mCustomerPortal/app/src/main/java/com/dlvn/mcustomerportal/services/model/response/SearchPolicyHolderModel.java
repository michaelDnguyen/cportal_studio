package com.dlvn.mcustomerportal.services.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchPolicyHolderModel implements Parcelable {
    @SerializedName("PolicyID")
    @Expose
    private String policyID;
    @SerializedName("ClientID")
    @Expose
    private String clientID;
    @SerializedName("PolicyStatusCD")
    @Expose
    private String policyStatusCD;
    @SerializedName("Policy_CAES_DT")
    @Expose
    private String policyCAESDT;
    @SerializedName("Date_Dif")
    @Expose
    private Integer dateDif;
    @SerializedName("FullName")
    @Expose
    private String fullName;

    public String getPolicyID() {
        return policyID;
    }

    public void setPolicyID(String policyID) {
        this.policyID = policyID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getPolicyStatusCD() {
        return policyStatusCD;
    }

    public void setPolicyStatusCD(String policyStatusCD) {
        this.policyStatusCD = policyStatusCD;
    }

    public String getPolicyCAESDT() {
        return policyCAESDT;
    }

    public void setPolicyCAESDT(String policyCAESDT) {
        this.policyCAESDT = policyCAESDT;
    }

    public Integer getDateDif() {
        return dateDif;
    }

    public void setDateDif(Integer dateDif) {
        this.dateDif = dateDif;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public SearchPolicyHolderModel(Parcel in) {
        policyID = in.readString();
        clientID = in.readString();
        policyStatusCD = in.readString();
        policyCAESDT = in.readString();
        dateDif = in.readInt();
        fullName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(policyID);
        dest.writeString(clientID);
        dest.writeString(policyStatusCD);
        dest.writeString(policyCAESDT);
        dest.writeInt(dateDif);
        dest.writeString(fullName);
    }

    public static final Creator<SearchPolicyHolderModel> CREATOR = new Creator<SearchPolicyHolderModel>() {
        @Override
        public SearchPolicyHolderModel createFromParcel(Parcel source) {
            return new SearchPolicyHolderModel(source);
        }

        @Override
        public SearchPolicyHolderModel[] newArray(int size) {
            return new SearchPolicyHolderModel[size];
        }
    };
}
