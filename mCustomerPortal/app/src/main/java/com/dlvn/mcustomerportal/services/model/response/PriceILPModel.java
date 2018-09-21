package com.dlvn.mcustomerportal.services.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PriceILPModel implements Parcelable{

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("ILPF1")
    @Expose
    private String iLPF1;
    @SerializedName("ILPF2")
    @Expose
    private String iLPF2;
    @SerializedName("ILPF3")
    @Expose
    private String iLPF3;
    @SerializedName("ILPF4")
    @Expose
    private String iLPF4;
    @SerializedName("ILPF5")
    @Expose
    private String iLPF5;
    @SerializedName("SubmitDate")
    @Expose
    private String submitDate;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getILPF1() {
        return iLPF1;
    }

    public void setILPF1(String iLPF1) {
        this.iLPF1 = iLPF1;
    }

    public String getILPF2() {
        return iLPF2;
    }

    public void setILPF2(String iLPF2) {
        this.iLPF2 = iLPF2;
    }

    public String getILPF3() {
        return iLPF3;
    }

    public void setILPF3(String iLPF3) {
        this.iLPF3 = iLPF3;
    }

    public String getILPF4() {
        return iLPF4;
    }

    public void setILPF4(String iLPF4) {
        this.iLPF4 = iLPF4;
    }

    public String getILPF5() {
        return iLPF5;
    }

    public void setILPF5(String iLPF5) {
        this.iLPF5 = iLPF5;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public PriceILPModel(Parcel in){
        iD = in.readInt();
        iLPF1 = in.readString();
        iLPF2 = in.readString();
        iLPF3 = in.readString();
        iLPF4 = in.readString();
        iLPF5 = in.readString();
        submitDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(iD);
        dest.writeString(iLPF1);
        dest.writeString(iLPF2);
        dest.writeString(iLPF3);
        dest.writeString(iLPF4);
        dest.writeString(iLPF5);
        dest.writeString(submitDate);
    }

    public static final Creator<PriceILPModel> CREATOR = new Creator<PriceILPModel>() {
        @Override
        public PriceILPModel createFromParcel(Parcel source) {
            return new PriceILPModel(source);
        }

        @Override
        public PriceILPModel[] newArray(int size) {
            return new PriceILPModel[size];
        }
    };
}
