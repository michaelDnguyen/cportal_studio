package com.dlvn.mcustomerportal.services.model.claims;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SyncCLaimDetailModel {
    @SerializedName("claimSub")
    @Expose
    private SyncClaimSub claimSub;
    @SerializedName("claimRequester")
    @Expose
    private ClaimsFromRequest claimRequester;
    @SerializedName("claimsItem")
    @Expose
    private ClaimsItem claimsItem;
    @SerializedName("TreatmentHistorys")
    @Expose
    private List<TreatmentHistory> treatmentHistorys = null;
    @SerializedName("lstDocType")
    @Expose
    private List<SyncClaimDocType> lstDocType = null;
    @SerializedName("lstDocTypeHold")
    @Expose
    private List<SyncClaimDocTypeHold> lstDocTypeHold = null;

    public SyncClaimSub getClaimSub() {
        return claimSub;
    }

    public void setClaimSub(SyncClaimSub claimSub) {
        this.claimSub = claimSub;
    }

    public ClaimsFromRequest getClaimRequester() {
        return claimRequester;
    }

    public void setClaimRequester(ClaimsFromRequest claimRequester) {
        this.claimRequester = claimRequester;
    }

    public ClaimsItem getClaimsItem() {
        return claimsItem;
    }

    public void setClaimsItem(ClaimsItem claimsItem) {
        this.claimsItem = claimsItem;
    }

    public List<TreatmentHistory> getTreatmentHistorys() {
        return treatmentHistorys;
    }

    public void setTreatmentHistorys(List<TreatmentHistory> treatmentHistorys) {
        this.treatmentHistorys = treatmentHistorys;
    }

    public List<SyncClaimDocType> getLstDocType() {
        return lstDocType;
    }

    public void setLstDocType(List<SyncClaimDocType> lstDocType) {
        this.lstDocType = lstDocType;
    }

    public List<SyncClaimDocTypeHold> getLstDocTypeHold() {
        return lstDocTypeHold;
    }

    public void setLstDocTypeHold(List<SyncClaimDocTypeHold> lstDocTypeHold) {
        this.lstDocTypeHold = lstDocTypeHold;
    }
}
