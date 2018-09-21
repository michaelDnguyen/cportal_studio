package com.dlvn.mcustomerportal.adapter.model;

import com.dlvn.mcustomerportal.services.model.response.CPPolicyInfo;

import java.util.ArrayList;
import java.util.List;

public class ContractDetailModel {

    String title;
    List<HomeItemModel> lstValue;

    List<CPPolicyInfo> lstDetail;

    public ContractDetailModel(String title, List<HomeItemModel> lstDetail) {
        this.title = title;
        this.lstValue = lstDetail;
    }

    public ContractDetailModel() {
        title = "";
        lstDetail = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CPPolicyInfo> getLstDetail() {
        return lstDetail;
    }

    public void setLstDetail(List<CPPolicyInfo> lstDetail) {
        this.lstDetail = lstDetail;
    }

    public List<HomeItemModel> getLstValue() {
        return lstValue;
    }

    public void setLstValue(List<HomeItemModel> lstValue) {
        this.lstValue = lstValue;
    }
}
