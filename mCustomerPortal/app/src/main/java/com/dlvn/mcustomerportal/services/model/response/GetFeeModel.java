package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFeeModel {

    @SerializedName("PolSndryAmt")
    @Expose
    private String polSndryAmt;
    @SerializedName("Apl")
    @Expose
    private String apl;
    @SerializedName("Opl")
    @Expose
    private String opl;

    public String getPolSndryAmt() {
        return polSndryAmt;
    }

    public void setPolSndryAmt(String polSndryAmt) {
        this.polSndryAmt = polSndryAmt;
    }

    public String getApl() {
        return apl;
    }

    public void setApl(String apl) {
        this.apl = apl;
    }

    public String getOpl() {
        return opl;
    }

    public void setOpl(String opl) {
        this.opl = opl;
    }

}
