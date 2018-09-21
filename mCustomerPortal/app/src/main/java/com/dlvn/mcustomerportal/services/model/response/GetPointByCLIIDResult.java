package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPointByCLIIDResult {

    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ErrLog")
    @Expose
    private String errLog;
    @SerializedName("NewAPIToken")
    @Expose
    private String newAPIToken;
    @SerializedName("Point")
    @Expose
    private String point;
    @SerializedName("HDPoint")
    @Expose
    private String hDPoint;
    @SerializedName("ClassPO")
    @Expose
    private String classPO;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrLog() {
        return errLog;
    }

    public void setErrLog(String errLog) {
        this.errLog = errLog;
    }

    public String getNewAPIToken() {
        return newAPIToken;
    }

    public void setNewAPIToken(String newAPIToken) {
        this.newAPIToken = newAPIToken;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getHDPoint() {
        return hDPoint;
    }

    public void setHDPoint(String hDPoint) {
        this.hDPoint = hDPoint;
    }

    public String getClassPO() {
        return classPO;
    }

    public void setClassPO(String classPO) {
        this.classPO = classPO;
    }
}
