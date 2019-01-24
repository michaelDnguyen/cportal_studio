package com.dlvn.mcustomerportal.adapter.model;

import com.dlvn.mcustomerportal.services.model.response.CPPolicyInfo;

import java.util.ArrayList;
import java.util.List;

public class ContractDetailModel {

    String title;
    List<PolicyItemDetailModel> lstValue;

    List<CPPolicyInfo> lstDetail;

    public ContractDetailModel(String title, List<PolicyItemDetailModel> lstDetail) {
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

    public List<PolicyItemDetailModel> getLstValue() {
        return lstValue;
    }

    public void setLstValue(List<PolicyItemDetailModel> lstValue) {
        this.lstValue = lstValue;
    }
}
