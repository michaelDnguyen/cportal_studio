package com.dlvn.mcustomerportal.services.model.response;

import com.dlvn.mcustomerportal.adapter.model.ElectricBillModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetTaxInvoiceResult {

    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ErrLog")
    @Expose
    private String errLog;
    @SerializedName("NewAPIToken")
    @Expose
    private String newAPIToken;
    @SerializedName("ClientProfile")
    @Expose
    private List<ElectricBillModel> electricBill = null;

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

    public List<ElectricBillModel> getElectricBill() {
        return electricBill;
    }

    public void setElectricBill(List<ElectricBillModel> electricBill) {
        this.electricBill = electricBill;
    }
}
