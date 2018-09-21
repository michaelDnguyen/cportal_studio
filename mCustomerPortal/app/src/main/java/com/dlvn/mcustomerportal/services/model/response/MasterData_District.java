package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MasterData_District {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Vung_Xa")
    @Expose
    private Integer vungXa;
    @SerializedName("Ma_Quan_Huyen")
    @Expose
    private String maQuanHuyen;
    @SerializedName("Ten_Quan_Huyen")
    @Expose
    private String tenQuanHuyen;
    @SerializedName("STT_Tinh_TP")
    @Expose
    private Integer sTTTinhTP;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public Integer getVungXa() {
        return vungXa;
    }

    public void setVungXa(Integer vungXa) {
        this.vungXa = vungXa;
    }

    public String getMaQuanHuyen() {
        return maQuanHuyen;
    }

    public void setMaQuanHuyen(String maQuanHuyen) {
        this.maQuanHuyen = maQuanHuyen;
    }

    public String getTenQuanHuyen() {
        return tenQuanHuyen;
    }

    public void setTenQuanHuyen(String tenQuanHuyen) {
        this.tenQuanHuyen = tenQuanHuyen;
    }

    public Integer getSTTTinhTP() {
        return sTTTinhTP;
    }

    public void setSTTTinhTP(Integer sTTTinhTP) {
        this.sTTTinhTP = sTTTinhTP;
    }
}
