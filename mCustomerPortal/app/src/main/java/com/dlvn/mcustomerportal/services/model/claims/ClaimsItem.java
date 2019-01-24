package com.dlvn.mcustomerportal.services.model.claims;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimsItem implements Parcelable {

    @SerializedName("LossDate")
    @Expose
    private String lossDate;
    @SerializedName("LossPlace")
    @Expose
    private String lossPlace;
    @SerializedName("AccidentReason")
    @Expose
    private String accidentReason;
    @SerializedName("AccidentDate")
    @Expose
    private String accidentDate;
    @SerializedName("AccidentPlace")
    @Expose
    private String accidentPlace;
    @SerializedName("AccidentCausers")
    @Expose
    private String accidentCausers;
    @SerializedName("CausersPhone")
    @Expose
    private String causersPhone;
    @SerializedName("AccidentIllnessDesc")
    @Expose
    private String accidentIllnessDesc;
    @SerializedName("StimulantStatus")
    @Expose
    private String stimulantStatus;
    @SerializedName("IllnessProgression")
    @Expose
    private String illnessProgression;

    private String claimReason;

    public String getLossDate() {
        return lossDate;
    }

    public void setLossDate(String lossDate) {
        this.lossDate = lossDate;
    }

    public String getLossPlace() {
        return lossPlace;
    }

    public void setLossPlace(String lossPlace) {
        this.lossPlace = lossPlace;
    }

    public String getAccidentReason() {
        return accidentReason;
    }

    public void setAccidentReason(String accidentReason) {
        this.accidentReason = accidentReason;
    }

    public String getAccidentDate() {
        return accidentDate;
    }

    public void setAccidentDate(String accidentDate) {
        this.accidentDate = accidentDate;
    }

    public String getAccidentPlace() {
        return accidentPlace;
    }

    public void setAccidentPlace(String accidentPlace) {
        this.accidentPlace = accidentPlace;
    }

    public String getAccidentCausers() {
        return accidentCausers;
    }

    public void setAccidentCausers(String accidentCausers) {
        this.accidentCausers = accidentCausers;
    }

    public String getCausersPhone() {
        return causersPhone;
    }

    public void setCausersPhone(String causersPhone) {
        this.causersPhone = causersPhone;
    }

    public String getAccidentIllnessDesc() {
        return accidentIllnessDesc;
    }

    public void setAccidentIllnessDesc(String accidentIllnessDesc) {
        this.accidentIllnessDesc = accidentIllnessDesc;
    }

    public String getStimulantStatus() {
        return stimulantStatus;
    }

    public void setStimulantStatus(String stimulantStatus) {
        this.stimulantStatus = stimulantStatus;
    }

    public String getIllnessProgression() {
        return illnessProgression;
    }

    public void setIllnessProgression(String illnessProgression) {
        this.illnessProgression = illnessProgression;
    }

    public String getClaimReason() {
        return claimReason;
    }

    public void setClaimReason(String claimReason) {
        this.claimReason = claimReason;
    }

    public ClaimsItem() {
        super();
    }

    public ClaimsItem(Parcel in) {
        claimReason = in.readString();
        lossDate = in.readString();
        lossPlace = in.readString();
        accidentReason = in.readString();
        accidentDate = in.readString();
        accidentPlace = in.readString();
        accidentCausers = in.readString();
        causersPhone = in.readString();
        accidentIllnessDesc = in.readString();
        stimulantStatus = in.readString();
        illnessProgression = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(claimReason);
        dest.writeString(lossDate);
        dest.writeString(lossPlace);
        dest.writeString(accidentReason);
        dest.writeString(accidentDate);
        dest.writeString(accidentPlace);
        dest.writeString(accidentCausers);
        dest.writeString(causersPhone);
        dest.writeString(accidentIllnessDesc);
        dest.writeString(stimulantStatus);
        dest.writeString(illnessProgression);
    }

    public static final Creator<ClaimsItem> CREATOR = new Creator<ClaimsItem>() {
        @Override
        public ClaimsItem createFromParcel(Parcel source) {
            return new ClaimsItem(source);
        }

        @Override
        public ClaimsItem[] newArray(int size) {
            return new ClaimsItem[size];
        }
    };
}
