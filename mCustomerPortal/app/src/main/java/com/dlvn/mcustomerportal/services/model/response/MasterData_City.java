package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MasterData_City {

    @SerializedName("ID_Tinh_TP")
    @Expose
    private Integer iDTinhTP;
    @SerializedName("STT_Tinh_TP")
    @Expose
    private Integer sTTTinhTP;
    @SerializedName("Ten_Tinh_TP")
    @Expose
    private String tenTinhTP;
    @SerializedName("TEN_BD")
    @Expose
    private String tENBD;
    @SerializedName("lsQuanHuyen")
    @Expose
    private List<MasterData_District> lsQuanHuyen = null;

    public Integer getIDTinhTP() {
        return iDTinhTP;
    }

    public void setIDTinhTP(Integer iDTinhTP) {
        this.iDTinhTP = iDTinhTP;
    }

    public Integer getSTTTinhTP() {
        return sTTTinhTP;
    }

    public void setSTTTinhTP(Integer sTTTinhTP) {
        this.sTTTinhTP = sTTTinhTP;
    }

    public String getTenTinhTP() {
        return tenTinhTP;
    }

    public void setTenTinhTP(String tenTinhTP) {
        this.tenTinhTP = tenTinhTP;
    }

    public String getTENBD() {
        return tENBD;
    }

    public void setTENBD(String tENBD) {
        this.tENBD = tENBD;
    }

    public List<MasterData_District> getLsQuanHuyen() {
        return lsQuanHuyen;
    }

    public void setLsQuanHuyen(List<MasterData_District> lsQuanHuyen) {
        this.lsQuanHuyen = lsQuanHuyen;
    }
}
