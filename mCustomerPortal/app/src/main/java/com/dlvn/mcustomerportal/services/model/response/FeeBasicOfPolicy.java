package com.dlvn.mcustomerportal.services.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeeBasicOfPolicy implements Parcelable {

    @SerializedName("POL_SNDRY_AMT")
    @Expose
    private String pOLSNDRYAMT;
    @SerializedName("APL")
    @Expose
    private String aPL;
    @SerializedName("OPL")
    @Expose
    private String oPL;
    @SerializedName("POL_ID")
    @Expose
    private String pOLID;
    @SerializedName("CLI_ID")
    @Expose
    private String cLIID;
    @SerializedName("POL_SNDRY_AMT_CODE")
    @Expose
    private String pOLSNDRYAMTCODE;
    @SerializedName("APL_CODE")
    @Expose
    private String aPLCODE;
    @SerializedName("OPL_CODE")
    @Expose
    private String oPLCODE;

    public String getPOLSNDRYAMT() {
        return pOLSNDRYAMT;
    }

    public void setPOLSNDRYAMT(String pOLSNDRYAMT) {
        this.pOLSNDRYAMT = pOLSNDRYAMT;
    }

    public String getAPL() {
        return aPL;
    }

    public void setAPL(String aPL) {
        this.aPL = aPL;
    }

    public String getOPL() {
        return oPL;
    }

    public void setOPL(String oPL) {
        this.oPL = oPL;
    }

    public String getPOLID() {
        return pOLID;
    }

    public void setPOLID(String pOLID) {
        this.pOLID = pOLID;
    }

    public String getCLIID() {
        return cLIID;
    }

    public void setCLIID(String cLIID) {
        this.cLIID = cLIID;
    }

    public String getPOLSNDRYAMTCODE() {
        return pOLSNDRYAMTCODE;
    }

    public void setPOLSNDRYAMTCODE(String pOLSNDRYAMTCODE) {
        this.pOLSNDRYAMTCODE = pOLSNDRYAMTCODE;
    }

    public String getAPLCODE() {
        return aPLCODE;
    }

    public void setAPLCODE(String aPLCODE) {
        this.aPLCODE = aPLCODE;
    }

    public String getOPLCODE() {
        return oPLCODE;
    }

    public void setOPLCODE(String oPLCODE) {
        this.oPLCODE = oPLCODE;
    }

    public FeeBasicOfPolicy(Parcel in) {
        this.cLIID = in.readString();
        this.pOLID = in.readString();
        this.pOLSNDRYAMTCODE = in.readString();
        this.pOLSNDRYAMT = in.readString();
        this.aPLCODE = in.readString();
        this.aPL = in.readString();
        this.oPLCODE = in.readString();
        this.oPL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cLIID);
        dest.writeString(pOLID);
        dest.writeString(pOLSNDRYAMTCODE);
        dest.writeString(pOLSNDRYAMT);
        dest.writeString(aPLCODE);
        dest.writeString(aPL);
        dest.writeString(oPLCODE);
        dest.writeString(oPL);
    }

    public static final Creator<FeeBasicOfPolicy> CREATOR = new Creator<FeeBasicOfPolicy>() {
        @Override
        public FeeBasicOfPolicy createFromParcel(Parcel source) {
            return new FeeBasicOfPolicy(source);
        }

        @Override
        public FeeBasicOfPolicy[] newArray(int size) {
            return new FeeBasicOfPolicy[size];
        }
    };
}
