package com.dlvn.mcustomerportal.adapter.model;

public class QuaTrinhTichLuyModel {
    private String date = "no data", code = "no data", status = "no data", point = "no data";

    public QuaTrinhTichLuyModel() {

    }

    public QuaTrinhTichLuyModel(String date, String code, String status, String point) {
        this.date = date;
        this.code = code;
        this.status = status;
        this.point = point;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
