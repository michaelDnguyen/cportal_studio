package com.dlvn.mcustomerportal.services.model.claims;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TreatmentHistory implements Parcelable {

    @SerializedName("TreatmentHospital")
    @Expose
    private String treatmentHospital;
    @SerializedName("TreatmentDateFrom")
    @Expose
    private String treatmentDateFrom;
    @SerializedName("TreatmentDateTo")
    @Expose
    private String treatmentDateTo;
    @SerializedName("TreatmentType")
    @Expose
    private String treatmentType;
    @SerializedName("Diagnostic")
    @Expose
    private String diagnostic;
    @SerializedName("PatientType")
    @Expose
    private String patientType;
    @SerializedName("DrugName")
    @Expose
    private String drugName;

    public String getTreatmentHospital() {
        return treatmentHospital;
    }

    public void setTreatmentHospital(String treatmentHospital) {
        this.treatmentHospital = treatmentHospital;
    }

    public String getTreatmentDateFrom() {
        return treatmentDateFrom;
    }

    public void setTreatmentDateFrom(String treatmentDateFrom) {
        this.treatmentDateFrom = treatmentDateFrom;
    }

    public String getTreatmentDateTo() {
        return treatmentDateTo;
    }

    public void setTreatmentDateTo(String treatmentDateTo) {
        this.treatmentDateTo = treatmentDateTo;
    }

    public String getTreatmentType() {
        return treatmentType;
    }

    public void setTreatmentType(String treatmentType) {
        this.treatmentType = treatmentType;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public TreatmentHistory() {
        super();
    }

    public TreatmentHistory(Parcel in) {
        treatmentHospital = in.readString();
        treatmentDateFrom = in.readString();
        treatmentDateTo = in.readString();
        treatmentType = in.readString();
        diagnostic = in.readString();
        patientType = in.readString();
        drugName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(treatmentHospital);
        dest.writeString(treatmentDateFrom);
        dest.writeString(treatmentDateTo);
        dest.writeString(treatmentType);
        dest.writeString(diagnostic);
        dest.writeString(patientType);
        dest.writeString(drugName);
    }

    public static final Creator<TreatmentHistory> CREATOR = new Creator<TreatmentHistory>() {
        @Override
        public TreatmentHistory createFromParcel(Parcel source) {
            return new TreatmentHistory(source);
        }

        @Override
        public TreatmentHistory[] newArray(int size) {
            return new TreatmentHistory[size];
        }
    };
}
